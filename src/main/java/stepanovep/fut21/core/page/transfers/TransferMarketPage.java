package stepanovep.fut21.core.page.transfers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import stepanovep.fut21.core.driver.FutWebDriver;
import stepanovep.fut21.core.locators.MainPageLocators;
import stepanovep.fut21.core.locators.TransferMarketLocators;
import stepanovep.fut21.core.page.transfers.filter.TransferMarketFilter;
import stepanovep.fut21.core.page.transfers.filter.TransferMarketFilterService;

/**
 * Page Object для страницы поиска и покупки карточек
 */
@Component
public class TransferMarketPage {

    @Autowired
    private FutWebDriver driver;

    @Autowired
    private TransferMarketFilterService filterService;

    @Retryable(value = {RuntimeException.class}, maxAttempts = 10)
    public TransferSearchResult search(TransferMarketFilter filter) {
        retry();
        navigateToPage();
        driver.clickElement(TransferMarketLocators.RESET_FILTER_BUTTON);
        filterService.applyFilter(filter);
        driver.clickElement(TransferMarketLocators.SEARCH_BUTTON);
        return TransferSearchResult.from(driver);
    }

    public void retry() {
        System.out.println("------ Excpetion -----");
        throw new RuntimeException("exception # ");
    }

    private void navigateToPage() {
        driver.clickElement(MainPageLocators.GO_TO_TRANSFERS);
        driver.clickElement(MainPageLocators.SEARCH_IN_TRANSFER_MARKET_FORM);
        driver.sleep(300);
    }
}
