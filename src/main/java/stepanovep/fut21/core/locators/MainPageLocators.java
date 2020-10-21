package stepanovep.fut21.core.locators;

import org.openqa.selenium.By;

public final class MainPageLocators {

    private MainPageLocators() {
    }

    public static By GO_TO_TRANSFERS = By.cssSelector("button.icon-transfer");
    public static By GO_TO_TRANSFER_TARGETS = By.cssSelector(".ut-tile-transfer-targets");
    public static By GO_TO_TRANSFER_MARKET = By.cssSelector(".ut-tile-transfer-market");
    public static By GO_TO_TRANSFER_LIST = By.cssSelector(".ut-tile-transfer-list");

    public static final By COINS_ELEM_LOCATOR = By.cssSelector(".view-navbar-currency > .view-navbar-currency-coins");

    public static final By DIALOG_SUBMIT_BUTTON = By.cssSelector(".dialog-body button");

}
