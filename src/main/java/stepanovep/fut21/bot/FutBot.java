package stepanovep.fut21.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stepanovep.fut21.api.Bidding;
import stepanovep.fut21.bot.service.LoginService;
import stepanovep.fut21.core.driver.FutWebDriver;
import stepanovep.fut21.core.page.transfers.filter.TransferMarketFilter;

import java.util.List;

@Service
public class FutBot {

    @Autowired
    private FutWebDriver driver;

    @Autowired
    private LoginService loginService;

    @Autowired
    private Bidding bidding;

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

    public void start() {
        var filter1 = TransferMarketFilter.builder()
                .withName("Canales")
                .withTargetPrice(2500)
                .build();

        var filter2 = TransferMarketFilter.builder()
                .withName("Djene")
                .withTargetPrice(3200)
                .build();

        var filter3 = TransferMarketFilter.builder()
                .withName("Witsel")
                .withTargetPrice(7000)
                .build();

        bidding.massBid(List.of(
                filter1,
                filter2,
                filter3)
        );
    }

    public void shutdown() {
        driver.quit();
    }
}
