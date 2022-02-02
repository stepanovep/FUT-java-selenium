package stepanovep.fut22.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stepanovep.fut22.bot.service.GemsSeller;
import stepanovep.fut22.bot.service.LoginService;
import stepanovep.fut22.bot.service.StatisticService;
import stepanovep.fut22.bot.service.bidding.BidChecker;
import stepanovep.fut22.bot.service.bidding.MassBidder;
import stepanovep.fut22.bot.service.clubstocking.ClubStocker;
import stepanovep.fut22.core.driver.FutWebDriver;
import stepanovep.fut22.core.page.FutActiveMenu;
import stepanovep.fut22.core.page.transfers.TransferListPage;
import stepanovep.fut22.futbin.FutbinService;
import stepanovep.fut22.telegrambot.TelegramNotifier;

import java.io.File;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * High level FutBot methods
 */
@Service
@Slf4j
public class FutBot {

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
    private GemsSeller gemsSeller;

    @Autowired
    private TransferListPage transferListPage;

    @Autowired
    private FutbinService futbinService;

    @Autowired
    private StatisticService statisticService;

    @Autowired
    private TelegramNotifier telegramNotifier;

    /**
     * Login to Web-app
     */
    public void login() {
        futbinService.updatePrices();
        futbotExecutor.submit(() -> loginService.login());
    }

    /**
     * Stop current task
     */
    public void stop() {
        // TODO Not Implemented
    }

    /**
     * Run mass bidding
     */
    public void massBid() {
        driver.activeMenu = FutActiveMenu.HOME;
        futbinService.updatePrices();

        futbotExecutor.submit(() ->  {
            loginService.login();
            transferListPage.relistAll();
        });

        futbotExecutor.submit(() ->  {
            massBidder.massBid();
            bidChecker.checkBids(10);
            massBidder.massBid();
            bidChecker.checkBids(15);

            telegramNotifier.sendMessage("Mass bidding successfully finished");
        });
    }

    /**
     * Run bid checker
     */
    public void checkBids() {
        driver.activeMenu = FutActiveMenu.HOME;
        futbotExecutor.submit(() ->  {
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
        log.info("scheduleRelistAll");
        for (int i = 0; i <= 6; i++) {
            futbotExecutor.schedule(() -> {
                loginService.login();
                transferListPage.relistAll();
            }, i*60 + i*2, TimeUnit.MINUTES);
        }
    }

    public void scheduleMassBid() {
        log.info("scheduleMassBid");
        for (int i = 0; i <= 6; i++) {
            futbotExecutor.schedule(this::massBid, i*90, TimeUnit.MINUTES);
        }
        for (int i = 0; i <= 9; i++) {
            futbotExecutor.schedule(() -> {
                loginService.login();
                transferListPage.relistAll();
            }, (i+1) * 62, TimeUnit.MINUTES);
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

    /**
     * Sell gems - low rated players with high current market price
     */
    public void sellGems() {
        futbotExecutor.submit(() -> gemsSeller.sellGems());
    }

    public void shutdown() {
        driver.quit();
    }

    public File screenshot() {
        return driver.screenshot();
    }
}
