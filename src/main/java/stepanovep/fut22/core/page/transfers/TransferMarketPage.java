package stepanovep.fut22.core.page.transfers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import stepanovep.fut22.core.driver.FutWebDriver;
import stepanovep.fut22.core.locators.MainPageLocators;
import stepanovep.fut22.core.locators.TransferMarketLocators;
import stepanovep.fut22.core.page.FutActiveMenu;
import stepanovep.fut22.core.page.transfers.search.TransferMarketSearchOptions;
import stepanovep.fut22.core.page.transfers.search.TransferMarketSearchService;

/**
 * Page Object для страницы поиска и покупки карточек
 */
@Component
@Slf4j
public class TransferMarketPage {

    @Autowired
    private FutWebDriver driver;

    @Autowired
    private TransferMarketSearchService searchService;

    public SearchResult applySearchOptionsAndSearch(TransferMarketSearchOptions searchOptions) {
        applySearchOptions(searchOptions);
        driver.clickElement(TransferMarketLocators.SEARCH_BUTTON, 500);
        return SearchResult.from(driver);
    }

    public void applySearchOptions(TransferMarketSearchOptions searchOptions) {
        navigateToPage();
        log.info("Searching: searchOptions={}", searchOptions);
        searchService.resetAll();
        searchService.applySearchOptions(searchOptions);
    }

    public SearchResult search() {
        driver.clickElement(TransferMarketLocators.SEARCH_BUTTON);
        return SearchResult.from(driver);
    }

    public void backToSearchForm() {
        driver.clickElement(TransferMarketLocators.BACK_TO_SEARCH_FORM_BUTTON);
    }

    public void changeMinBidPrice(int sign) {
        if (sign > 0) {
            driver.clickElement(TransferMarketLocators.MIN_BID_PRICE_INCREASE_BUTTON);
        } else {
            driver.clickElement(TransferMarketLocators.MIN_BID_PRICE_DECREASE_BUTTON);
        }
    }

    public void changeMinBuyNowPrice(int sign) {
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
