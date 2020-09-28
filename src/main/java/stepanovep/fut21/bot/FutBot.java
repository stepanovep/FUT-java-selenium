package stepanovep.fut21.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stepanovep.fut21.bot.service.LoginService;
import stepanovep.fut21.core.driver.FutWebDriver;
import stepanovep.fut21.core.page.transfers.TransferMarketPage;
import stepanovep.fut21.core.page.transfers.filter.ChemStyle;
import stepanovep.fut21.core.page.transfers.filter.Quality;
import stepanovep.fut21.core.page.transfers.filter.TransferMarketFilter;

@Service
public class FutBot {

    @Autowired
    private FutWebDriver driver;

    @Autowired
    private LoginService loginService;

    @Autowired
    private TransferMarketPage transferMarketPage;

    /**
     * Войти в FUT web-app
     */
    public void login() {
        loginService.login();
    }

    /**
     * Активна ли текущая сессия
     */
    public boolean isLoggedIn() {
        return true; //todo
    }

    public void test() {
        var filter = TransferMarketFilter.builder()
                .withName("Firmino")
                .withQuality(Quality.SPECIAL)
                .withChemStyle(ChemStyle.HUNTER)
                .withBuyNowMax(1_000_000)
                .build();

        var searchResult = transferMarketPage.search(filter);
    }

    public void shutdown() {
        driver.quit();
    }
}
