package stepanovep.fut21.core.locators;

import org.openqa.selenium.By;

public final class MainPageLocators {

    private MainPageLocators() {
    }

    public static By GO_TO_TRANSFERS = By.cssSelector("button.icon-transfer");
    public static By GO_TO_TRANSFER_TARGETS = By.cssSelector(".ut-tile-transfer-targets");
    public static By SEARCH_IN_TRANSFER_MARKET_FORM = By.cssSelector(".ut-tile-transfer-market");

    public static final String LOGIN_BUTTON_LOCATOR = "div.ut-login-content > .btn-standard";

}
