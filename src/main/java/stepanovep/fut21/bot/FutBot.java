package stepanovep.fut21.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stepanovep.fut21.api.MassBidder;
import stepanovep.fut21.bot.service.LoginService;
import stepanovep.fut21.core.driver.FutWebDriver;

import java.io.File;

@Service
public class FutBot {

    @Autowired
    private FutWebDriver driver;

    @Autowired
    private LoginService loginService;

    @Autowired
    private MassBidder massBidder;

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
        massBidder.massBid();
    }

    public void shutdown() {
        driver.quit();
    }

    public File screenshot() {
        return driver.screenshot();
    }
}
