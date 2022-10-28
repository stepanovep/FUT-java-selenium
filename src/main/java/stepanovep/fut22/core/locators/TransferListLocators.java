package stepanovep.fut22.core.locators;

import org.openqa.selenium.By;
import stepanovep.fut22.core.page.transfers.TransferListPage;
import stepanovep.fut22.utils.LocatorsUtils;

/**
 * Локаторы страницы {@link TransferListPage}
 */
public class TransferListLocators {

    public static By RELIST_ALL_BUTTON = LocatorsUtils.byText("Re-list All");
    public static By UNSOLD_ITEMS_SECTION = By.cssSelector(".ut-content section:nth-child(2)");
    public static By SECTION_ELEMENTS = By.cssSelector(".itemList > li");
}
