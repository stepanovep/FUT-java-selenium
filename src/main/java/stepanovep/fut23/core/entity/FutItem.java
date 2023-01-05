package stepanovep.fut23.core.entity;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import stepanovep.fut23.core.driver.FutWebDriver;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static stepanovep.fut23.core.entity.FutItemType.BADGE;
import static stepanovep.fut23.core.entity.FutItemType.BALL;
import static stepanovep.fut23.core.entity.FutItemType.CONSUMABLE;
import static stepanovep.fut23.core.entity.FutItemType.GIFT;
import static stepanovep.fut23.core.entity.FutItemType.KIT;
import static stepanovep.fut23.core.entity.FutItemType.OTHER;
import static stepanovep.fut23.core.entity.FutItemType.PLAYER;
import static stepanovep.fut23.core.entity.FutItemType.STADIUM;
import static stepanovep.fut23.core.entity.FutItemType.STAFF;
import static stepanovep.fut23.core.entity.FutItemType.VANITY;
import static stepanovep.fut23.core.locators.FutElementLocators.BUY_NOW_BUTTON;
import static stepanovep.fut23.core.locators.FutElementLocators.COMPARE_PRICE_BACK_TO_LAYOUT_BUTTON;
import static stepanovep.fut23.core.locators.FutElementLocators.COMPARE_PRICE_BUTTON;
import static stepanovep.fut23.core.locators.FutElementLocators.COMPARE_PRICE_ELEMENTS;
import static stepanovep.fut23.core.locators.FutElementLocators.COMPARE_PRICE_ELEMENT_BUY_NOW_VALUE;
import static stepanovep.fut23.core.locators.FutElementLocators.COMPARE_PRICE_NEXT_BUTTON;
import static stepanovep.fut23.core.locators.FutElementLocators.ITEM_RIGHT_LAYOUT_CONTAINER;
import static stepanovep.fut23.core.locators.FutElementLocators.LIST_TO_TRANSFER_MARKET_BIN_PRICE;
import static stepanovep.fut23.core.locators.FutElementLocators.LIST_TO_TRANSFER_MARKET_OPEN_MENU;
import static stepanovep.fut23.core.locators.FutElementLocators.LIST_TO_TRANSFER_MARKET_START_PRICE;
import static stepanovep.fut23.core.locators.FutElementLocators.LIST_TO_TRANSFER_MARKET_SUBMIT_BUTTON;
import static stepanovep.fut23.core.locators.FutElementLocators.QUICK_SELL_BUTTON;
import static stepanovep.fut23.core.locators.FutElementLocators.REDEEM_BUTTON;
import static stepanovep.fut23.core.locators.FutElementLocators.SEND_TO_CLUB_BUTTON;
import static stepanovep.fut23.core.locators.FutElementLocators.SEND_TO_TRANSFER_MARKET_BUTTON;
import static stepanovep.fut23.core.locators.FutElementLocators.SWAP_DUPLICATE_BUTTON;
import static stepanovep.fut23.core.locators.FutElementLocators.UNTRADABLE_FLAG;

@Slf4j
public class FutItem {

    protected final FutWebDriver driver;
    protected final WebElement webElement;
    protected final WebElement entityContainer;

    public FutItem(FutWebDriver driver, WebElement webElement) {
        this.driver = driver;
        this.webElement = webElement;
        this.entityContainer = webElement.findElement(By.cssSelector(".entityContainer"));
    }

    public File screenshot() {
        return webElement.getScreenshotAs(OutputType.FILE);
    }

    public void focus() {
        driver.clickElement(webElement);
    }

    public void buyNow() {
        driver.clickElement(BUY_NOW_BUTTON);
        driver.sleep(50, 100);
        driver.acceptDialogMessage();
    }

    public void sendToClub() {
        driver.clickElement(SEND_TO_CLUB_BUTTON);
    }

    public void sendToTransferMarket() {
        driver.sleep(500);
        driver.clickElement(ITEM_RIGHT_LAYOUT_CONTAINER, SEND_TO_TRANSFER_MARKET_BUTTON);
        driver.acceptDialogMessage();
    }

    public void listToTransferMarket(Integer startPrice, Integer buyNowPrice) {
        driver.clickElement(LIST_TO_TRANSFER_MARKET_OPEN_MENU);
        new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(5))
                .ignoring(NoSuchElementException.class)
                .until(webdriver -> {
                    WebElement startPriceInput = driver.findElement(LIST_TO_TRANSFER_MARKET_START_PRICE);
                    WebElement buyNowPriceInput = driver.findElement(LIST_TO_TRANSFER_MARKET_BIN_PRICE);

                    driver.sendKeys(startPriceInput, String.valueOf(startPrice));
                    driver.sendKeys(buyNowPriceInput, String.valueOf(buyNowPrice));

                    return true;
                });

        driver.clickElement(LIST_TO_TRANSFER_MARKET_SUBMIT_BUTTON);
        driver.sleep(1000, 2000);
    }

    public List<Integer> comparePrice() {
        driver.clickElement(COMPARE_PRICE_BUTTON);
        int pages = 0;
        List<Integer> prices = new ArrayList<>();
        while (pages < 1) {
            driver.sleep(1500, 2500);
            List<WebElement> compareElements = driver.findElementsWithWait(COMPARE_PRICE_ELEMENTS);
            compareElements.forEach(element -> {
                String buyNowStr = element.findElement(COMPARE_PRICE_ELEMENT_BUY_NOW_VALUE).getText();
                prices.add(Integer.parseInt(buyNowStr.replace(",", "")));
            });
            try {
                driver.clickElement(COMPARE_PRICE_NEXT_BUTTON);
            } catch (Exception exc) {
                break;
            }
            pages++;
        }

        prices.sort(Integer::compareTo);
        driver.clickElement(COMPARE_PRICE_BACK_TO_LAYOUT_BUTTON);
        return prices;
    }

    public Optional<Integer> getBinPrice() {
        List<Integer> binPrices = comparePrice();
        if (binPrices.isEmpty()) {
            return Optional.empty();
        }

        if (binPrices.size() == 1) {
            return Optional.of(binPrices.get(0));
        }

        Collections.sort(binPrices);
        return Optional.of(binPrices.get(1));
    }

    public void toggleWatch() {
        driver.clickElement(By.cssSelector("button.watch"));
        handleDialogIfPresent();
    }

    protected Optional<BidResult> handleDialogIfPresent() {
        Optional<WebElement> dialogOpt = driver.getDialog();
        if (dialogOpt.isPresent()) {
            WebElement dialog = dialogOpt.get();
            String dialogTitle = dialog.findElement(By.cssSelector(".ea-dialog-view--title")).getText();
            switch (dialogTitle.toUpperCase()) {
                case "LIMIT REACHED" -> {
                    log.warn("Transfer targets limit reached");
                    driver.acceptDialogMessage();
                    return Optional.of(BidResult.LIMIT_REACHED);
                }
                case "ALREADY HIGHEST BIDDER" -> {
                    log.warn("Already highest bidder");
                    driver.declineDialogMessage();
                    return Optional.of(BidResult.SUCCESS);
                }
                case "BID TOO LOW" -> {
                    log.warn("Bid too low");
                    driver.acceptDialogMessage();
                }
                case "CANNOT UNWATCH" -> {
                    log.warn("Cannot unwatch an item bidding on");
                    driver.acceptDialogMessage();
                }
            }
        }

        return Optional.empty();
    }


    public void quickSell() {
        driver.clickElement(QUICK_SELL_BUTTON);
        driver.acceptDialogMessage();
    }

    public void redeem() {
        driver.clickElement(REDEEM_BUTTON);
    }

    public FutItemType getType() {
        if (doesEntityHasClass("player")) {
            return PLAYER;
        }
        if (doesEntityHasClass("staff")) {
            return STAFF;
        }
        if (doesEntityHasClass("vanity")) {
            return VANITY;
        }
        if (doesEntityHasClass("badge")) {
            return BADGE;
        }
        if (doesEntityHasClass("kit")) {
            return KIT;
        }
        if (doesEntityHasClass("misc")) {
            return GIFT;
        }
        if (doesEntityHasClass("consumable")) {
            return CONSUMABLE;
        }
        if (doesEntityHasClass("ball")) {
            return BALL;
        }
        if (doesEntityHasClass("stadium")) {
            return STADIUM;
        }
        return OTHER;
    }

    private boolean doesEntityHasClass(String classValue) {
        String selector = String.format(".entityContainer > .%s", classValue);
        return entityContainer.findElements(By.cssSelector(selector)).size() > 0;
    }

    public boolean isDuplicate() {
        return driver.isElementPresent(SWAP_DUPLICATE_BUTTON);
    }

    public boolean isSelected() {
        return webElement.getAttribute("class").contains("selected");
    }

    public boolean isUntradable() {
        try {
            driver.findElement(UNTRADABLE_FLAG);
            return true;
        } catch (NoSuchElementException exc) {
            return false;
        }
    }

    public boolean isTradable() {
        return !isUntradable();
    }
}
