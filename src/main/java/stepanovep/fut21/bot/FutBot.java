package stepanovep.fut21.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stepanovep.fut21.api.Bidding;
import stepanovep.fut21.bot.service.LoginService;
import stepanovep.fut21.core.driver.FutWebDriver;

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
        bidding.massBid();
    }

    public void shutdown() {
        driver.quit();
    }
}
