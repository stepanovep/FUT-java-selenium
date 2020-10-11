package stepanovep.fut21.core.page.transfers;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import stepanovep.fut21.core.driver.FutWebDriver;
import stepanovep.fut21.core.entity.AuctionData;
import stepanovep.fut21.core.entity.BidResult;
import stepanovep.fut21.core.entity.FutPlayerAuctionData;
import stepanovep.fut21.core.entity.FutPlayerElement;
import stepanovep.fut21.core.entity.PlayerAuctionDataService;
import stepanovep.fut21.core.locators.MainPageLocators;
import stepanovep.fut21.core.locators.TransferTargetsLocators;
import stepanovep.fut21.core.page.FutActiveMenu;
import stepanovep.fut21.mongo.AuctionService;
import stepanovep.fut21.mongo.AuctionTrade;
import stepanovep.fut21.mongo.Player;
import stepanovep.fut21.mongo.PlayerService;
import stepanovep.fut21.telegrambot.TelegramBot;
import stepanovep.fut21.utils.FutPriceUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Page Object для страницы управления аукционами
 */
@Component
public class TransferTargetsPage {

    private static final Logger log = LoggerFactory.getLogger(TransferTargetsPage.class);

    private static final int MAX_EXPIRATION_TIME_TO_CHECK = 60;

    @Autowired
    private FutWebDriver driver;
    @Autowired
    private PlayerAuctionDataService playerAuctionDataService;
    @Autowired
    private AuctionService auctionService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private TelegramBot telegramBot;

    private void navigateToPage() {
        if (driver.activeMenu != FutActiveMenu.TRANSFER_TARGETS) {
            driver.clickElement(MainPageLocators.GO_TO_TRANSFERS);
            driver.clickElement(MainPageLocators.GO_TO_TRANSFER_TARGETS);
        }
        driver.activeMenu = FutActiveMenu.TRANSFER_TARGETS;
        driver.sleep(500);
    }

    @Retryable(include = {StaleElementReferenceException.class}, backoff = @Backoff(delay = 3000))
    public void checkBids() {
        navigateToPage();
        listWonItemsToTransferMarket();
        driver.sleep(2000);
        List<FutPlayerElement> activeBids = getActiveBids();
        log.info("Checking bids: activeBids count = {}", activeBids.size());

        for (FutPlayerElement player: activeBids) {
            if (player.isOutbid()) {
                driver.sleep(1000, 2000);
                player.focus();
                FutPlayerAuctionData extendedData = playerAuctionDataService.getFutPlayerAuctionData();
                AuctionData auction = extendedData.getAuction();
                if (auction.getExpires() > MAX_EXPIRATION_TIME_TO_CHECK) {
                    log.info("Expiration time is too far - check bids later");
                    break;
                }
                String tradeId = auction.getTradeId();

                log.info("Player is outbid: name={}, tradeId={}", extendedData.getName(), tradeId);
                Optional<AuctionTrade> auctionTrade = auctionService.get(tradeId);
                if (auctionTrade.isPresent()) {
                    Integer targetPrice = auctionTrade.get().getTargetPrice();
                    Integer nextBid = FutPriceUtils.getNextBid(auction.getStartingBid(), auction.getCurrentBid());
                    if (nextBid <= targetPrice) {
                        rebid(player);

                    } else {
                        log.info("Player bid is too high: name={}, nextBid={}, targetPrice={}", extendedData.getName(), nextBid, targetPrice);
                        unwatchItem(player);
                    }
                }
            }
        }

        removeOneExpiredItem(20);
        listWonItemsToTransferMarket();
        driver.sleep(2000, 3000);
    }

    @Recover
    public void recover(StaleElementReferenceException exc) {
        log.error("Checking bids failed consecutive times");
    }

    public void listWonItemsToTransferMarket() {
        WebElement wonItemsSection = driver.findElementWithWait(TransferTargetsLocators.WON_BIDS_SECTION);
        List<WebElement> wonItems = wonItemsSection.findElements(TransferTargetsLocators.SECTION_ELEMENTS);

        while (!wonItems.isEmpty()) {
            FutPlayerElement playerElement = new FutPlayerElement(driver, wonItems.get(0));
            playerElement.focus();
            FutPlayerAuctionData extendedData = playerAuctionDataService.getFutPlayerAuctionData();
            String resourceId = extendedData.getResourceId();
            Optional<Player> playerOpt = playerService.getByResourceId(resourceId);
            if (playerOpt.isPresent()) {
                int boughtPrice = playerElement.getBoughtPrice();
                int marketPrice = playerOpt.get().getPcPrice();
                log.info("Player bid won! name={}, boughtPrice={}, marketPrice={}",
                        extendedData.getName(), boughtPrice, marketPrice);
                telegramBot.notifyAboutBoughtPlayer(driver.screenshot()); //TODO делать скриншот элемента, а не всей страницы
                playerElement.listToTransferMarket(marketPrice - 200, marketPrice + 200);

            } else {
                playerElement.sendToTransferMarket();
            }

            wonItems = wonItemsSection.findElements(TransferTargetsLocators.SECTION_ELEMENTS);
            driver.sleep(1000, 2000);
        }
    }

    private List<FutPlayerElement> getActiveBids() {
        WebElement activeBidsSection = driver.findElementWithWait(TransferTargetsLocators.ACTIVE_BIDS_SECTION);
        List<WebElement> targets = activeBidsSection.findElements(TransferTargetsLocators.SECTION_ELEMENTS);

        return targets.stream()
                .map(target -> new FutPlayerElement(driver, target))
                .collect(Collectors.toList());
    }

    private void rebid(FutPlayerElement player) {
        driver.sleep(400, 600);
        BidResult bidResult = player.makeBid();
        if (bidResult != BidResult.SUCCESS) {
            removeOneExpiredItem();
            checkBids();
        }
        log.info("Player has been rebid");
        driver.sleep(400, 600);
    }

    private void unwatchItem(FutPlayerElement player) {
        WebElement activeBidsSection = driver.findElementWithWait(TransferTargetsLocators.EXPIRED_BIDS_SECTION);
        List<WebElement> expiredItems = activeBidsSection.findElements(TransferTargetsLocators.SECTION_ELEMENTS);
        if (expiredItems.size() > 0) {
            player.toggleWatch();
        }
    }

    private void removeOneExpiredItem() {
        removeOneExpiredItem(0);
    }

    private void removeOneExpiredItem(int lowerBound) {
        driver.sleep(1000);
        WebElement activeBidsSection = driver.findElementWithWait(TransferTargetsLocators.EXPIRED_BIDS_SECTION);
        List<WebElement> expiredItems = activeBidsSection.findElements(TransferTargetsLocators.SECTION_ELEMENTS);
        if (expiredItems.size() > lowerBound) {
            FutPlayerElement expiredItem = new FutPlayerElement(driver, expiredItems.get(0));
            expiredItem.focus();
            expiredItem.toggleWatch();
        }
        driver.sleep(1000, 2000);
    }

}
