package stepanovep.fut21.core.page.transfers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import stepanovep.fut21.core.driver.FutWebDriver;
import stepanovep.fut21.core.locators.MainPageLocators;
import stepanovep.fut21.core.locators.TransferMarketLocators;
import stepanovep.fut21.core.page.FutActiveMenu;
import stepanovep.fut21.core.page.transfers.filter.TransferMarketFilter;
import stepanovep.fut21.core.page.transfers.filter.TransferMarketFilterService;

/**
 * Page Object для страницы поиска и покупки карточек
 */
@Component
public class TransferMarketPage {

    private final static Logger log = LoggerFactory.getLogger(TransferMarketPage.class);

    @Autowired
    private FutWebDriver driver;

    @Autowired
    private TransferMarketFilterService filterService;

    public TransferSearchResult search(TransferMarketFilter filter) {
        navigateToPage();
        log.info("Searching: filter={}", filter);
        filterService.resetAllFilters();
        filterService.applyFilter(filter);
        driver.clickElement(TransferMarketLocators.SEARCH_BUTTON);
        return TransferSearchResult.from(driver);
    }

    private void navigateToPage() {
        if (driver.activeMenu != FutActiveMenu.TRANSFER_MARKET) {
            driver.clickElement(MainPageLocators.GO_TO_TRANSFERS);
            driver.clickElement(MainPageLocators.SEARCH_IN_TRANSFER_MARKET_FORM);
        }
        driver.activeMenu = FutActiveMenu.TRANSFER_MARKET;
        driver.sleep(300);
    }
}
