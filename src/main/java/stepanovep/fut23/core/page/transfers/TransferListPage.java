package stepanovep.fut23.core.page.transfers;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import stepanovep.fut23.core.driver.FutWebDriver;
import stepanovep.fut23.core.locators.MainPageLocators;
import stepanovep.fut23.core.locators.TransferListLocators;
import stepanovep.fut23.core.page.FutActiveMenu;

import java.util.List;

/**
 * Page Object для страницы управления текущим трансферным списком
 */
@Component
@Slf4j
public class TransferListPage {

    @Autowired
    private FutWebDriver driver;

    public void relistAll() {
        navigateToPage();

        List<WebElement> unsoldItems = getUnsoldItems();
        if (unsoldItems.size() > 0) {
            driver.clickElement(TransferListLocators.RELIST_ALL_BUTTON);
            driver.acceptDialogMessage();
        }

        log.info("Unsold items relisted: count={}", unsoldItems.size());
        driver.sleep(2000);
    }

    public void navigateToPage() {
        if (driver.activeMenu != FutActiveMenu.TRANSFER_LIST) {
            driver.clickElement(MainPageLocators.GO_TO_TRANSFERS);
            driver.clickElement(MainPageLocators.GO_TO_TRANSFER_LIST);
        }
        driver.activeMenu = FutActiveMenu.TRANSFER_LIST;
        driver.sleep(1000);
    }

    public List<WebElement> getUnsoldItems() {
        WebElement unsoldItemsSection = driver.findElementWithWait(TransferListLocators.UNSOLD_ITEMS_SECTION);
        return unsoldItemsSection.findElements(TransferListLocators.SECTION_ELEMENTS);
    }
}
