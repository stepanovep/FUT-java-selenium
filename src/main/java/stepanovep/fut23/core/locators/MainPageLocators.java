package stepanovep.fut23.core.locators;

import org.openqa.selenium.By;

public final class MainPageLocators {

    private MainPageLocators() {
    }

    public static By GO_TO_TRANSFERS = By.cssSelector("button.icon-transfer");
    public static By GO_TO_TRANSFER_TARGETS = By.cssSelector(".ut-tile-transfer-targets");
    public static By GO_TO_TRANSFER_MARKET = By.cssSelector(".ut-tile-transfer-market");
    public static By GO_TO_TRANSFER_LIST = By.cssSelector(".ut-tile-transfer-list");

    public static By GO_TO_CLUB = By.cssSelector("button.icon-club");
    public static By GO_TO_CLUB_PLAYERS = By.cssSelector(".tile.players-tile");

    public static final By COINS_ELEM_LOCATOR = By.cssSelector(".view-navbar-currency > .view-navbar-currency-coins");

    public static final By DIALOG_SUBMIT_BUTTON = By.cssSelector(".dialog-body button");
    public static final By LIVE_MESSAGE_POPUP = By.cssSelector(".ut-livemessage");
    public static final By LIVE_MESSAGE_CLOSE_BUTTON = By.cssSelector(".ut-livemessage > div > button");

}
