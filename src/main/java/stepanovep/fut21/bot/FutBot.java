package stepanovep.fut21.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stepanovep.fut21.bot.service.bidding.BidChecker;
import stepanovep.fut21.bot.service.bidding.MassBidder;
import stepanovep.fut21.bot.service.LoginService;
import stepanovep.fut21.bot.service.StatisticService;
import stepanovep.fut21.bot.service.clubstocking.ClubStocker;
import stepanovep.fut21.core.driver.FutWebDriver;
import stepanovep.fut21.core.page.FutActiveMenu;
import stepanovep.fut21.core.page.transfers.TransferListPage;
import stepanovep.fut21.futbin.FutbinService;
import stepanovep.fut21.telegrambot.TelegramNotifier;

import java.io.File;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * High level FutBot methods
 */
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
    private ClubStocker clubStocker;

    @Autowired
    private TransferListPage transferListPage;

    @Autowired
    private FutbinService futbinService;

    @Autowired
    private StatisticService statisticService;

    @Autowired
    private TelegramNotifier telegramNotifier;

    private Future<?> currentTask;

    /**
     * Login to Web-app
     */
    public void login() {
        futbinService.updatePrices();
        currentTask = futbotExecutor.submit(() -> loginService.login());
    }

    /**
     * Stop current task
     * todo: sometimes task won't stop
     */
    public void stop() {
        driver.interrupt();
        currentTask.cancel(true);
    }

    /**
     * Run mass bidding
     */
    public void massBid() {
        driver.activeMenu = FutActiveMenu.HOME;
        futbinService.updatePrices();

        currentTask = futbotExecutor.submit(() ->  {
            loginService.login();
            // TODO fix relistAll() thread stuck if nothing to relist
            transferListPage.relistAll();
        });

        currentTask = futbotExecutor.submit(() ->  {
            massBidder.massBid();
            bidChecker.checkBids(10);
            massBidder.massBid();
            bidChecker.checkBids(10);

            telegramNotifier.sendMessage("Mass bidding successfully finished");
        });
    }

    /**
     * Run bid checker only
     */
    public void checkBids() {
        driver.activeMenu = FutActiveMenu.HOME;
        currentTask = futbotExecutor.submit(() ->  {
            bidChecker.checkBids(15);
            telegramNotifier.sendMessage("Bid checker successfully finished");
        });
    }

    /**
     * Relist all players
     */
    public void relistAll() {
        driver.activeMenu = FutActiveMenu.HOME;
        futbotExecutor.submit(() -> transferListPage.relistAll());
    }

    /**
     * Schedule relist all every hour for 6 hours
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

    /**
     * Show daily statistics: amount of players bought and potential profit
     */
    public void showDailyStatistic() {
        statisticService.sendDailyStatistic();
    }

    /**
     * Club stock low rated (gold rare players) by bidding
     */
    public void clubStock() {
        futbotExecutor.submit(() -> clubStocker.clubStock());
    }

    public void shutdown() {
        driver.quit();
    }

    public File screenshot() {
        return driver.screenshot();
    }
}
