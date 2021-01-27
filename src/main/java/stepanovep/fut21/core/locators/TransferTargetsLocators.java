package stepanovep.fut21.core.locators;

import org.openqa.selenium.By;

public final class TransferTargetsLocators {

    private TransferTargetsLocators() {
    }

    public static By ACTIVE_BIDS_SECTION = By.cssSelector(".ut-content section:nth-child(1)");
    public static By WATCHED_BIDS_SECTION = By.cssSelector(".ut-content section:nth-child(2)");
    public static By WON_BIDS_SECTION = By.cssSelector(".ut-content section:nth-child(3)");
    public static By SEND_WON_ITEMS_TO_CLUB = By.cssSelector(".ut-content section:nth-child(3) > header > button");
    public static By EXPIRED_BIDS_SECTION = By.cssSelector(".ut-content section:nth-child(4)");
    public static By CLEAR_EXPIRED_ITEMS_BUTTON = By.cssSelector(".ut-content section:nth-child(4) > header > button");
    public static By SECTION_ELEMENTS = By.cssSelector(".itemList > li");

}
