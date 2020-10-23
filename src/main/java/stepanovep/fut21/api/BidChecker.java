package stepanovep.fut21.api;

import org.openqa.selenium.StaleElementReferenceException;
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
import stepanovep.fut21.core.page.transfers.TransferTargetsPage;
import stepanovep.fut21.mongo.AuctionService;
import stepanovep.fut21.mongo.ActiveAuction;
import stepanovep.fut21.mongo.Player;
import stepanovep.fut21.mongo.PlayerService;
import stepanovep.fut21.mongo.WonAuction;
import stepanovep.fut21.telegrambot.TelegramBotNotifier;
import stepanovep.fut21.utils.FutPriceUtils;

import java.util.List;
import java.util.Optional;

@Component
public class BidChecker {

    private static final Logger log = LoggerFactory.getLogger(BidChecker.class);

    private static final int MAX_EXPIRATION_TIME_SECONDS_TO_CHECK = 50;

    @Autowired
    private FutWebDriver driver;
    @Autowired
    private TransferTargetsPage transferTargetsPage;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private PlayerAuctionDataService playerAuctionDataService;
    @Autowired
    private AuctionService auctionService;
    @Autowired
    private TelegramBotNotifier telegramBotNotifier;

    public void checkBids(int repeat) {
        driver.wakeup();
        try {
            for (int i = 0; i < repeat; i++) {
                checkBids();
            }
        } catch (Exception exc) {
            log.error("Bid checker failed", exc);
            telegramBotNotifier.notifyAboutException(driver.screenshot());
            return;
        }

        log.info("Bid checker successfully finished");
        telegramBotNotifier.sendMessage("Bid checker successfully finished");
    }

    @Retryable(include = {StaleElementReferenceException.class}, backoff = @Backoff(delay = 3000))
    public void checkBids() {
        if (driver.isInterrupted()) {
            System.out.println("Thread is interrupted - aborting bid checker");
            return;
        }
        transferTargetsPage.navigateToPage();
        listWonItemsToTransferMarket();
        driver.sleep(2000);
        List<FutPlayerElement> activeBids = transferTargetsPage.getActiveBids();
        log.info("Checking bids: activeBids count = {}", activeBids.size());

        for (FutPlayerElement player: activeBids) {
            if (player.isOutbid()) {
                driver.sleep(1000, 2000);
                player.focus();
                FutPlayerAuctionData extendedData = playerAuctionDataService.getFutPlayerAuctionData();
                if (extendedData.getAuction().getExpires() > MAX_EXPIRATION_TIME_SECONDS_TO_CHECK) {
                    log.info("Expiration time is too far - check bids later");
                    break;
                }
                handleOutbidPlayer(player, extendedData);
            }
        }

        transferTargetsPage.removeOneExpiredItem(20);
        driver.sleep(2000, 3000);
    }

    @Recover
    public void recover(StaleElementReferenceException exc) {
        log.error("Checking bids failed consecutive times");
    }

    public void listWonItemsToTransferMarket() {
        List<FutPlayerElement> wonItems = transferTargetsPage.getWonItems();

        while (!wonItems.isEmpty()) {
            FutPlayerElement playerElement = wonItems.get(0);
            playerElement.focus();
            FutPlayerAuctionData extendedData = playerAuctionDataService.getFutPlayerAuctionData();
            String resourceId = extendedData.getResourceId();
            Optional<Player> playerOpt = playerService.getByResourceId(resourceId);

            int bidPrice = playerElement.getBoughtPrice();
            if (playerOpt.isPresent()) {
                Player player = playerOpt.get();
                int marketPrice = player.getPcPrice();
                String message = String.format("Player bid won! %s: bidPrice=%d, marketPrice=%d", extendedData.getName(), bidPrice, marketPrice);
                log.info(message);
                telegramBotNotifier.notifyAboutBoughtPlayer(driver.screenshot(), message); //TODO делать скриншот элемента, а не всей страницы
                playerElement.listToTransferMarket(marketPrice - 200, marketPrice + 200);
                auctionService.insertWonAuction(WonAuction.builder()
                        .withTradeId(extendedData.getAuction().getTradeId())
                        .withPlayerName(extendedData.getName())
                        .withPlayerRating(extendedData.getRating())
                        .withBoughtPrice(bidPrice)
                        .build());

            } else {
                String message = String.format("Player bid won, but resourceId is unknown: %s, bidPrice=%d", extendedData.getName(), bidPrice);
                log.info(message);
                telegramBotNotifier.notifyAboutBoughtPlayer(driver.screenshot(), message);
                playerElement.sendToTransferMarket();
            }

            driver.sleep(1000, 2000);
            wonItems = transferTargetsPage.getWonItems();
        }
    }

    private void handleOutbidPlayer(FutPlayerElement player, FutPlayerAuctionData extendedData) {
        AuctionData auctionData = extendedData.getAuction();
        String tradeId = auctionData.getTradeId();

        log.info("Player is outbid: name={}, tradeId={}", extendedData.getName(), tradeId);
        Optional<ActiveAuction> activeAuction = auctionService.getActiveAuction(tradeId);
        if (activeAuction.isPresent()) {
            Integer targetPrice = activeAuction.get().getTargetPrice();
            Integer nextBid = FutPriceUtils.getNextBid(auctionData.getStartingBid(), auctionData.getCurrentBid());
            if (nextBid <= targetPrice) {
                rebid(player);

            } else {
                log.info("Player bid is too high: name={}, nextBid={}, targetPrice={}", extendedData.getName(), nextBid, targetPrice);
                transferTargetsPage.unwatchOutbidPlayer(player);
            }
        }
    }

    private void rebid(FutPlayerElement player) {
        driver.sleep(400, 600);
        BidResult bidResult = player.makeBid();
        if (bidResult != BidResult.SUCCESS) {
            transferTargetsPage.removeOneExpiredItem();
            checkBids();
        }
        log.info("Player has been rebid");
        driver.sleep(400, 600);
    }
}
