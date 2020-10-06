package stepanovep.fut21.core.entity;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import stepanovep.fut21.core.driver.FutWebDriver;
import stepanovep.fut21.core.locators.FutElementLocators;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static stepanovep.fut21.core.locators.FutElementLocators.COMPARE_PRICE_BUTTON;
import static stepanovep.fut21.core.locators.FutElementLocators.COMPARE_PRICE_ELEMENTS;
import static stepanovep.fut21.core.locators.FutElementLocators.COMPARE_PRICE_ELEMENT_BUY_NOW_VALUE;
import static stepanovep.fut21.core.locators.FutElementLocators.COMPARE_PRICE_NEXT_BUTTON;

/**
 * FUT item element: player, consumable, coaches, etc.
 */
public class FutElement {

    private final FutWebDriver driver;
    private final WebElement webElement;

    public FutElement(FutWebDriver driver, WebElement webElement) {
        this.driver = driver;
        this.webElement = webElement;
    }

    public void focus() {
        webElement.click();
    }

    public BidResult makeBid() {
        if (!isSelected()) {
            focus();
        }
        if (!driver.clickElement(FutElementLocators.BID_BUTTON)) {
            return BidResult.BID_BUTTON_DISABLED;
        }
        driver.sleep(200, 300);
        if (isBid()) {
            return BidResult.SUCCESS;
        } else {
            return BidResult.BID_CHANGED_ERROR;
        }
    }

    public void buyNow() {

    }

    public void sendToClub() {

    }

    public void sendToTransferMarket() {

    }

    public void listToTransferMarket(Integer startPrice, Integer buyNowPrice) {

    }

    public List<Integer> comparePrice() {
        driver.clickElement(COMPARE_PRICE_BUTTON);
        int pages = 0;
        List<Integer> prices = new ArrayList<>();
        do {
            driver.sleep(1500, 2500);
            List<WebElement> compareElements = driver.findElementsWithWait(COMPARE_PRICE_ELEMENTS);
            compareElements.forEach(element -> {
                String buyNowStr = element.findElement(COMPARE_PRICE_ELEMENT_BUY_NOW_VALUE).getText();
                prices.add(Integer.parseInt(buyNowStr.replace(",", "")));
            });
            driver.clickElement(COMPARE_PRICE_NEXT_BUTTON);

        } while (driver.isElementPresent(COMPARE_PRICE_NEXT_BUTTON) && ++pages < 4);

        return prices;
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

}
