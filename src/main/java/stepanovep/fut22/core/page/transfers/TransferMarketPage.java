package stepanovep.fut22.core.page.transfers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import stepanovep.fut22.core.driver.FutWebDriver;
import stepanovep.fut22.core.locators.MainPageLocators;
import stepanovep.fut22.core.locators.TransferMarketLocators;
import stepanovep.fut22.core.page.FutActiveMenu;
import stepanovep.fut22.core.page.transfers.filter.TransferMarketSearchFilter;
import stepanovep.fut22.core.page.transfers.filter.TransferMarketFilterService;

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

    public TransferSearchResult applyFilterAndSearch(TransferMarketSearchFilter filter) {
        applyFilter(filter);
        driver.clickElement(TransferMarketLocators.SEARCH_BUTTON);
        return TransferSearchResult.from(driver);
    }

    public void applyFilter(TransferMarketSearchFilter filter) {
        navigateToPage();
        log.info("Searching: filter={}", filter);
        filterService.resetAllFilters();
        filterService.applyFilter(filter);
    }

    public TransferSearchResult search() {
        driver.clickElement(TransferMarketLocators.SEARCH_BUTTON);
        return TransferSearchResult.from(driver);
    }

    public void backToSearchForm() {
        driver.clickElement(TransferMarketLocators.BACK_TO_SEARCH_FORM_BUTTON);
    }

    public void changeMinBidPriceFilter(int sign) {
        if (sign > 0) {
            driver.clickElement(TransferMarketLocators.MIN_BID_PRICE_INCREASE_BUTTON);
        } else {
            driver.clickElement(TransferMarketLocators.MIN_BID_PRICE_DECREASE_BUTTON);
        }
    }

    public void changeMinBuyNowPriceFilter(int sign) {
        if (sign > 0) {
            driver.clickElement(TransferMarketLocators.MIN_BIN_PRICE_INCREASE_BUTTON);
        } else {
            driver.clickElement(TransferMarketLocators.MIN_BIN_PRICE_DECREASE_BUTTON);
        }
    }

    private void navigateToPage() {
        if (driver.activeMenu != FutActiveMenu.TRANSFER_MARKET) {
            driver.clickElement(MainPageLocators.GO_TO_TRANSFERS);
            driver.clickElement(MainPageLocators.GO_TO_TRANSFER_MARKET);
        }
        driver.activeMenu = FutActiveMenu.TRANSFER_MARKET;
        driver.sleep(300);
    }
}
