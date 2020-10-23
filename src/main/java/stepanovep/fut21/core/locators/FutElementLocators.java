package stepanovep.fut21.core.locators;

import org.openqa.selenium.By;
import stepanovep.fut21.utils.LocatorsUtils;

public class FutElementLocators {

    public static By COMPARE_PRICE_BUTTON = LocatorsUtils.byText("Compare Price");
    public static By COMPARE_PRICE_ELEMENTS = By.cssSelector(".ui-layout-right ul.paginated > li");
    public static By COMPARE_PRICE_ELEMENT_BUY_NOW_VALUE = By.cssSelector("div.auction > div:nth-child(3) > span.value");
    public static By COMPARE_PRICE_NEXT_BUTTON = By.cssSelector(".ui-layout-right button.next");

    public static By LIST_TO_TRANSFER_MARKET_OPEN_MENU = LocatorsUtils.byText("List on Transfer Market");
    public static By LIST_TO_TRANSFER_MARKET_START_PRICE = By.cssSelector(".panelActions.open > div:nth-child(2) > .ut-numeric-input-spinner-control > input");
    public static By LIST_TO_TRANSFER_MARKET_BIN_PRICE = By.cssSelector(".panelActions.open > div:nth-child(3) > .ut-numeric-input-spinner-control > input");
    public static By LIST_TO_TRANSFER_MARKET_SUBMIT_BUTTON = By.cssSelector(".panelActions.open > button");

    public static By SEND_TO_TRANSFER_MARKET_BUTTON = LocatorsUtils.byText("Send to Transfer List");

    public static By BID_BUTTON = By.cssSelector(".bidButton");
    public static By BUY_NOW_BUTTON = By.cssSelector(".buyButton");

    public static By BOUGHT_PRICE = By.cssSelector(".auctionInfo .currency-coins");
}

