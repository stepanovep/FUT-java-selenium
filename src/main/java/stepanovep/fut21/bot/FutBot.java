package stepanovep.fut21.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stepanovep.fut21.api.BidChecker;
import stepanovep.fut21.api.MassBidder;
import stepanovep.fut21.bot.service.LoginService;
import stepanovep.fut21.core.driver.FutWebDriver;
import stepanovep.fut21.futbin.FutbinService;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Service
public class FutBot {

    @Autowired
    private FutWebDriver driver;

    @Autowired
    private ExecutorService futbotExecutor;

    @Autowired
    private LoginService loginService;

    @Autowired
    private MassBidder massBidder;

    @Autowired
    private BidChecker bidChecker;

    @Autowired
    private FutbinService futbinService;

    private Future<?> currentTask;

    /**
     * Войти в FUT web-app
     */
    public void login() {
        futbinService.updatePrices();
        currentTask = futbotExecutor.submit(() -> loginService.login());
    }

    public void stop() {
        if (currentTask != null) {
            currentTask.cancel(true);
        }
    }

    /**
     * Активна ли текущая сессия
     */
    public boolean isLoggedIn() {
        return true; //todo
    }

    public void massBid() {
        currentTask = futbotExecutor.submit(() -> massBidder.massBid());
    }

    public void checkBids() {
        currentTask = futbotExecutor.submit(() -> bidChecker.checkBids(15));
    }

    public void relistAll() {
        // todo relistAll
    }

    public void shutdown() {
        driver.quit();
    }

    public File screenshot() {
        return driver.screenshot();
    }
}
