package stepanovep.fut22.core.page.transfers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import stepanovep.fut22.core.driver.FutWebDriver;
import stepanovep.fut22.core.locators.MainPageLocators;
import stepanovep.fut22.core.locators.TransferListLocators;
import stepanovep.fut22.core.page.FutActiveMenu;

/**
 * Page Object для страницы управления текущим трансферным списком
 */
@Component
@Slf4j
public class TransferListPage {

    @Autowired
    private FutWebDriver driver;

    public void relistAll() {
        log.info("Relisting items...");
        navigateToPage();
        if (driver.isElementPresent(TransferListLocators.RELIST_ALL_BUTTON)) {
            driver.clickElement(TransferListLocators.RELIST_ALL_BUTTON);
            driver.acceptDialogMessage();
            log.info("Items relisted");
        }

        driver.sleep(2000);
    }

    public void navigateToPage() {
        if (driver.activeMenu != FutActiveMenu.TRANSFER_LIST) {
            driver.clickElement(MainPageLocators.GO_TO_TRANSFERS);
            driver.clickElement(MainPageLocators.GO_TO_TRANSFER_LIST);
        }
        driver.activeMenu = FutActiveMenu.TRANSFER_LIST;
        driver.sleep(300);
    }
}
