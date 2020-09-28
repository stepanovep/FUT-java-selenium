package stepanovep.fut21.core.entity;

import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * FUT item element: player, consumable, coaches, etc.
 * TODO 30 september
 */
public class FutItem {

    private final WebElement webElement;

    public FutItem(WebElement webElement) {
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

    public List<FutItem> comparePrice() {
        return List.of();
    }

    public void unwatch() {

    }

    public void discard() {

    }

}
