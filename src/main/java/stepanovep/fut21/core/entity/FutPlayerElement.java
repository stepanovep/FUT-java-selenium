package stepanovep.fut21.core.entity;

import net.gcardone.junidecode.Junidecode;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stepanovep.fut21.core.driver.FutWebDriver;
import stepanovep.fut21.core.locators.FutElementLocators;
import stepanovep.fut21.utils.FutPriceUtils;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static stepanovep.fut21.core.locators.FutElementLocators.COMPARE_PRICE_BACK_TO_LAYOUT_BUTTON;
import static stepanovep.fut21.core.locators.FutElementLocators.COMPARE_PRICE_BUTTON;
import static stepanovep.fut21.core.locators.FutElementLocators.COMPARE_PRICE_ELEMENTS;
import static stepanovep.fut21.core.locators.FutElementLocators.COMPARE_PRICE_ELEMENT_BUY_NOW_VALUE;
import static stepanovep.fut21.core.locators.FutElementLocators.COMPARE_PRICE_NEXT_BUTTON;

/**
 * FUT player element
 */
public class FutPlayerElement {

    private static final Logger log = LoggerFactory.getLogger(FutPlayerElement.class);

    private final FutWebDriver driver;
    private final WebElement webElement;
    private final WebElement entityContainer;
    private final WebElement auctionContainer;

    private static final Pattern EXPIRATION_TIME_REGEX = Pattern.compile("(<?)(\\d+)(.+)");

    public FutPlayerElement(FutWebDriver driver, WebElement webElement) {
        this.driver = driver;
        this.webElement = webElement;
        this.entityContainer = ((RemoteWebElement) webElement).findElementByCssSelector(".entityContainer");
        this.auctionContainer = ((RemoteWebElement) webElement).findElementByCssSelector(".auction");
    }

    public File screenshot() {
        return webElement.getScreenshotAs(OutputType.FILE);
    }

    public void focus() {
        driver.clickElement(webElement);
    }

    public BidResult makeBid() {
        focus();
        driver.sleep(250, 500);
        if (!driver.findElement(FutElementLocators.BID_BUTTON).isEnabled()) {
            return BidResult.BID_BUTTON_INACTIVE;
        }
        driver.clickElement(FutElementLocators.BID_BUTTON);
        for (int tries = 0; tries < 3; tries++) {
            driver.sleep(400, 750);

            Optional<BidResult> dialogBidResult = handleDialogIfPresent();
            if (dialogBidResult.isPresent()) {
                return dialogBidResult.get();
            }

            if (isBid()) {
                return BidResult.SUCCESS;
            }
            if (isOutbid()) {
                return BidResult.OUTBID;
            }
            List<WebElement> errorPopups = driver.findElements(By.cssSelector("#NotificationLayer .negative"));
            if (errorPopups.size() > 0) {
                String errorText = errorPopups.get(0).findElement(By.cssSelector("p")).getText();
                if (errorText.contains("Bid status changed")) {
                    return BidResult.BID_CHANGED_ERROR;
                }
                return BidResult.TOO_MANY_ACTIONS_ERROR;
            }
        }

        return BidResult.IGNORED;
    }

    private Optional<BidResult> handleDialogIfPresent() {
        Optional<WebElement> dialogOpt = driver.getDialog();
        if (dialogOpt.isPresent()) {
            WebElement dialog = dialogOpt.get();
            String dialogTitle = dialog.findElement(By.cssSelector(".dialog-title")).getText();
            if (dialogTitle.toUpperCase().equals("LIMIT REACHED")) {
                log.warn("Transfer targets limit reached");
                driver.acceptDialogMessage();
                return Optional.of(BidResult.LIMIT_REACHED);
            }
            if (dialogTitle.toUpperCase().equals("ALREADY HIGHEST BIDDER")) {
                log.warn("Already highest bidder");
                driver.declineDialogMessage();
                return Optional.of(BidResult.SUCCESS);
            }
            if (dialogTitle.toUpperCase().equals("BID TOO LOW")) {
                log.warn("Bid too low");
                driver.acceptDialogMessage();
            }
        }

        return Optional.empty();
    }

    public void buyNow() {

    }

    public void sendToClub() {

    }

    public void sendToTransferMarket() {
        driver.sleep(500);
        driver.clickElement(FutElementLocators.SEND_TO_TRANSFER_MARKET_BUTTON);
    }

    public void listToTransferMarket(Integer startPrice, Integer buyNowPrice) {
        driver.clickElement(FutElementLocators.LIST_TO_TRANSFER_MARKET_OPEN_MENU);
        new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(5))
                .ignoring(NoSuchElementException.class)
                .until(webdriver -> {
                    WebElement startPriceInput = driver.findElement(FutElementLocators.LIST_TO_TRANSFER_MARKET_START_PRICE);
                    WebElement buyNowPriceInput = driver.findElement(FutElementLocators.LIST_TO_TRANSFER_MARKET_BIN_PRICE);

                    driver.sendKeys(startPriceInput, String.valueOf(startPrice));
                    driver.sendKeys(buyNowPriceInput, String.valueOf(buyNowPrice));

                    return true;
                });

        driver.clickElement(FutElementLocators.LIST_TO_TRANSFER_MARKET_SUBMIT_BUTTON);
        driver.sleep(1000, 2000);
    }

    public List<Integer> comparePrice() {
        driver.clickElement(COMPARE_PRICE_BUTTON);
        int pages = 0;
        List<Integer> prices = new ArrayList<>();
        while (pages < 3) {
            driver.sleep(1250, 2000);
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
    }

    public void discard() {

    }

    public boolean isSelected() {
        return webElement.getAttribute("class").contains("selected");
    }

    public boolean isBid() {
        return webElement.getAttribute("class").contains("highest-bid");
    }

    public boolean isOutbid() {
        return webElement.getAttribute("class").contains("outbid");
    }

    public boolean isWon() {
        return webElement.getAttribute("class").contains("won");
    }

    public String getName() {
        return Junidecode.unidecode(entityContainer.findElement(By.cssSelector(".name")).getText());
    }

    public int getNextBid() {
        WebElement startPriceDiv = auctionContainer.findElements(By.cssSelector(".auctionValue")).get(0);
        if (!startPriceDiv.findElement(By.cssSelector(".label")).getText().contains("START PRICE")) {
            log.error("Start price element is incorrect: auctionContainer={}", auctionContainer.getText());
            throw new IllegalStateException("Start price element is incorrect");
        }

        WebElement currentBidDiv = auctionContainer.findElements(By.cssSelector(".auctionValue")).get(1);
        if (!currentBidDiv.findElement(By.cssSelector(".label")).getText().contains("BID")) {
            log.error("Start price element is incorrect: auctionContainer={}", auctionContainer.getText());
            throw new IllegalStateException("Bid price element is incorrect");
        }

        String startPriceStr = startPriceDiv.findElement(By.cssSelector(".value")).getText();
        String currentBidStr = currentBidDiv.findElement(By.cssSelector(".value")).getText();
        if (currentBidStr.equals("---")) {
            return Integer.parseInt(startPriceStr.replace(",", ""));
        }

        return FutPriceUtils.getNextBid(Integer.parseInt(currentBidStr.replace(",", "")));
    }

    public int getBuyNowPrice() {
        WebElement buyNowElement = auctionContainer.findElements(By.cssSelector(".auctionValue")).get(2);
        if (!buyNowElement.findElement(By.cssSelector(".label")).getText().contains("BUY NOW")) {
            log.error("Buy bow price element is incorrect: auctionContainer={}", auctionContainer.getText());
            throw new IllegalStateException("Bid price element is incorrect");
        }

        return Integer.parseInt(buyNowElement.findElement(By.cssSelector(".value")).getText().replace(",", ""));
    }

    public int getRating() {
        return Integer.parseInt(entityContainer.findElement(By.cssSelector(".rating")).getText());
    }

    public String getPosition() {
        return entityContainer.findElement(By.cssSelector(".position")).getText();
    }

    public int getBoughtPrice() {
        WebElement boughtPriceElement = driver.findElement(FutElementLocators.BOUGHT_PRICE);
        return Integer.parseInt(boughtPriceElement.getText().replace(",", ""));
    }

    public Duration getExpirationTime() {
        WebElement expirationTimeElement = auctionContainer.findElement(By.cssSelector(".auction-state > .time"));
        String expirationTimeStr = expirationTimeElement.getText();
        if (expirationTimeStr.equals("Processing...") || expirationTimeStr.equals("Expired")) {
            return Duration.ZERO;
        }

        Matcher matcher = EXPIRATION_TIME_REGEX.matcher(expirationTimeStr);
        if (!matcher.find()) {
            throw new IllegalStateException("Expiration time cannot be parsed");
        }

        long time = Long.parseLong(matcher.group(2));

        if (expirationTimeStr.contains("Second")) {
            return Duration.ofSeconds(time);

        } else if (expirationTimeStr.contains("Minute")) {
            if (matcher.group(1).equals("<")) {
                return Duration.ofMinutes(1);
            }
            return Duration.ofMinutes(time+1);

        } else if (expirationTimeStr.contains("Hour")) {
            return Duration.ofHours(time);

        } else {
            return Duration.ofDays(1L);
        }
    }

    public String getPlayerAsString() {
        return String.join("#", getName(), String.valueOf(getRating()), getPosition());
        //todo использовать статы игрока для генерации строки, чтобы избежать коллизий спец карточек одинакового рейтинга и позиции
    }
}
