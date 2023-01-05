package stepanovep.fut23.core.locators;

import lombok.NoArgsConstructor;
import org.openqa.selenium.By;
import stepanovep.fut23.utils.LocatorsUtils;

@NoArgsConstructor
public final class FutElementLocators {

    public static final By ITEM_RIGHT_LAYOUT_CONTAINER = By.cssSelector(".ut-navigation-container-view.ui-layout-right");

    public static final By COMPARE_PRICE_BUTTON = LocatorsUtils.byText("Compare Price");
    public static final By COMPARE_PRICE_ELEMENTS = By.cssSelector(".paginated-item-list > ul > li.listFUTItem");
    public static final By COMPARE_PRICE_ELEMENT_BUY_NOW_VALUE = By.cssSelector("div.auction > div:nth-child(3) > span.value");
    public static final By COMPARE_PRICE_NEXT_BUTTON = By.cssSelector(".ui-layout-right button.next");
    public static final By COMPARE_PRICE_BACK_TO_LAYOUT_BUTTON = By.cssSelector(".ui-layout-right .ut-navigation-button-control");

    public static final By LIST_TO_TRANSFER_MARKET_OPEN_MENU = LocatorsUtils.byText("List on Transfer Market");
    public static final By LIST_TO_TRANSFER_MARKET_START_PRICE = By.cssSelector(".panelActions.open > div:nth-child(2) > .ut-numeric-input-spinner-control > input");
    public static final By LIST_TO_TRANSFER_MARKET_BIN_PRICE = By.cssSelector(".panelActions.open > div:nth-child(3) > .ut-numeric-input-spinner-control > input");
    public static final By LIST_TO_TRANSFER_MARKET_SUBMIT_BUTTON = By.cssSelector(".panelActions.open > button");

    public static final By SEND_TO_CLUB_BUTTON = LocatorsUtils.byText("Send to My Club");
    public static final By SEND_TO_TRANSFER_MARKET_BUTTON = LocatorsUtils.byText("Send to Transfer List");
    public static final By QUICK_SELL_BUTTON = LocatorsUtils.byText("Quick Sell");
    public static final By SWAP_DUPLICATE_BUTTON = LocatorsUtils.byText("Swap Duplicate Item from Club");

    public static final By REDEEM_BUTTON = By.cssSelector(".DetailPanel > div > button:nth-child(2)");
    public static final By UNTRADABLE_FLAG = LocatorsUtils.byText("This item cannot be traded");

    public static final By BID_BUTTON = By.cssSelector(".bidButton");
    public static final By BUY_NOW_BUTTON = By.cssSelector(".buyButton");

    public static final By BOUGHT_PRICE = By.cssSelector(".auctionInfo .currency-coins");
}

