package stepanovep.fut21.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stepanovep.fut21.bot.service.BidChecker;
import stepanovep.fut21.bot.service.MassBidder;
import stepanovep.fut21.bot.service.LoginService;
import stepanovep.fut21.core.driver.FutWebDriver;
import stepanovep.fut21.core.page.FutActiveMenu;
import stepanovep.fut21.core.page.transfers.TransferListPage;
import stepanovep.fut21.futbin.FutbinService;
import stepanovep.fut21.telegrambot.TelegramBotNotifier;

import java.io.File;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class FutBot {

    private static final Logger log = LoggerFactory.getLogger(FutBot.class);

    @Autowired
    private FutWebDriver driver;

    @Autowired
    private ScheduledExecutorService futbotExecutor;

    @Autowired
    private LoginService loginService;

    @Autowired
    private MassBidder massBidder;

    @Autowired
    private BidChecker bidChecker;

    @Autowired
    private TransferListPage transferListPage;

    @Autowired
    private FutbinService futbinService;

    @Autowired
    private TelegramBotNotifier telegramBotNotifier;

    private Future<?> currentTask;

    /**
     * Войти в FUT web-app
     */
    public void login() {
        futbinService.updatePrices();
        currentTask = futbotExecutor.submit(() -> loginService.login());
    }

    /**
     * Остановить текущую задачу
     */
    public void stop() {
        driver.interrupt();
        currentTask.cancel(true);
    }

    /**
     * Активна ли текущая сессия
     */
    public boolean isLoggedIn() {
        return true; //todo
    }

    /**
     * Запустить задачу для массовых ставок
     */
    public void massBid() {
        driver.activeMenu = FutActiveMenu.HOME;
        currentTask = futbotExecutor.submit(() ->  {
            massBidder.massBid();
            telegramBotNotifier.sendMessage("Mass bidding successfully finished");
        });
    }

    /**
     * Запустить задачу для проверки ставок
     */
    public void checkBids() {
        driver.activeMenu = FutActiveMenu.HOME;
        currentTask = futbotExecutor.submit(() ->  {
            bidChecker.checkBids(15);
            telegramBotNotifier.sendMessage("Bid checker successfully finished");
        });
    }

    /**
     * Перевыставить игроков на продажу
     */
    public void relistAll() {
        driver.activeMenu = FutActiveMenu.HOME;
        futbotExecutor.submit(() -> transferListPage.relistAll());
    }

    /**
     * Запустить периодическую задачу для перепродажи игроков
     */
    public void scheduleRelistAll() {
        System.out.println("scheduleRelistAll");
        for (int i = 0; i <= 6; i++) {
            currentTask = futbotExecutor.schedule(() -> {
                loginService.login();
                transferListPage.relistAll();
            }, i*60 + i*2, TimeUnit.MINUTES);
        }
    }

    public void shutdown() {
        driver.quit();
    }

    public File screenshot() {
        return driver.screenshot();
    }
}
