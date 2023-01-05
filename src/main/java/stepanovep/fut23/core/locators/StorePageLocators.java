package stepanovep.fut23.core.locators;

import lombok.NoArgsConstructor;
import org.openqa.selenium.By;
import stepanovep.fut23.utils.LocatorsUtils;

@NoArgsConstructor
public final class StorePageLocators {

    public static final By GO_TO_PACKS = By.cssSelector("div.packs-tile");
    public static final By CLASSIC_PACKS_SECTION = LocatorsUtils.byText("CLASSIC PACKS");

    public static final By BUY_BRONZE_PACK_BUTTON = By.cssSelector("div.ut-store-hub-view--content > div.ut-store-pack-details-view:nth-child(2) button.coins");
    public static final By FUT_ITEMS = By.cssSelector("li.listFUTItem");

    public static final By STORE_ALL_IN_CLUB_BUTTON = By.cssSelector(".sectioned-item-list:nth-child(1) > header > button");
    public static final By QUICK_SELL_DUPLICATES = By.cssSelector(".sectioned-item-list:nth-child(2) > div > button");


}
