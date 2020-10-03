package stepanovep.fut21.core.entity;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import stepanovep.fut21.core.driver.FutWebDriver;

import java.util.List;

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

    public void bid() {

    }

    public void buyNow() {

    }

    public void sendToClub() {

    }

    public void sendToTransferMarket() {

    }

    public void listToTransferMarket() {

    }

    public List<FutElement> comparePrice() {
        return List.of();
    }

    public void toggleWatch() {
        driver.clickElement(By.cssSelector("button.watch"));
    }

    public void discard() {

    }

}
