package stepanovep.fut21.bot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import stepanovep.fut21.core.driver.FutWebDriver;
import stepanovep.fut21.core.entity.AuctionData;
import stepanovep.fut21.core.entity.BidResult;
import stepanovep.fut21.core.entity.BidState;
import stepanovep.fut21.core.entity.FutPlayerAuctionData;
import stepanovep.fut21.core.entity.FutPlayerElement;
import stepanovep.fut21.core.entity.PlayerAuctionDataService;
import stepanovep.fut21.core.page.transfers.TransferMarketPage;
import stepanovep.fut21.core.page.transfers.TransferSearchResult;
import stepanovep.fut21.core.page.transfers.filter.TransferMarketSearchFilter;
import stepanovep.fut21.mongo.ActiveAuction;
import stepanovep.fut21.mongo.AuctionService;
import stepanovep.fut21.mongo.Player;
import stepanovep.fut21.mongo.PlayerService;
import stepanovep.fut21.telegrambot.TelegramBotNotifier;
import stepanovep.fut21.utils.FutPriceUtils;

import java.util.List;

/**
 * Биддер
 */
@Component
public class MassBidder {

    private static final Logger log = LoggerFactory.getLogger(MassBidder.class);

    @Autowired
    private FutWebDriver driver;
    @Autowired
    private TransferMarketPage transferMarket;
    @Autowired
    private BidChecker bidChecker;
    @Autowired
    private PlayerAuctionDataService playerAuctionDataService;
    @Autowired
    private AuctionService auctionService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private TelegramBotNotifier telegramBotNotifier;

    private static final int MAX_COUNT_BIDS = 5;
    private static final int MAX_BID_EXPIRATION_TIME_LEFT_IN_SECONDS = 20 * 60;

    public void massBid() {
        driver.wakeup();
        try {
            log.info("Mass bidding");
            List<Player> players = playerService.getPlayersForMassBid(30, 1700, 20000);
            for (Player player: players) {
                if (driver.isInterrupted()) {
                    System.out.println("Thread interrupted - aborting mass bidding");
                    return;
                }
                bidChecker.checkBids();
                massBidPlayer(player);
                driver.sleep(2000);
            }

            driver.sleep(2000, 3000);

        } catch (Exception exc) {
            log.error("Mass bid failed: ", exc);
            telegramBotNotifier.notifyAboutException(driver.screenshot());
            return;
        }
    }

    private void massBidPlayer(Player player) {
        int bidsCount = 0;
        TransferMarketSearchFilter filter = mapToSearchFilter(player);
        Integer targetPrice = filter.getTargetPrice().orElseThrow(() -> new IllegalStateException("targetPrice is mandatory here"));
        TransferSearchResult searchResult = transferMarket.search(filter);
        if (targetPrice > getAdjustedTargetPrice(searchResult)) {
            targetPrice = getAdjustedTargetPrice(searchResult);
            int newActualPrice = Math.max((int) (targetPrice * 1.15), (int) (targetPrice * 1.1) + 500);
            playerService.updatePriceByFutbinId(player.getFutbinId(), FutPriceUtils.roundToValidFutPrice(newActualPrice));
            log.info("Futbin price is outdated, changing targetPrice based on firstPage minimum bin price: new targetPrice={}", targetPrice);
        }

        for (FutPlayerElement playerElement: searchResult.getPlayers()) {
            playerElement.focus();
            driver.sleep(1000, 2000);
            FutPlayerAuctionData extendedData = playerAuctionDataService.getFutPlayerAuctionData();
            AuctionData auction = extendedData.getAuction();
            if (auction.getExpires() > MAX_BID_EXPIRATION_TIME_LEFT_IN_SECONDS || bidsCount >= MAX_COUNT_BIDS) {
                log.info("Skipping this and next items due to the time left filter");
                break;
            }
            if (needToBid(auction, targetPrice)) {
                makeBid(playerElement, extendedData, targetPrice);
                bidsCount++;
            }
        }

        playerService.updateBidTime(player);
    }

    private int getAdjustedTargetPrice(TransferSearchResult searchResult) {
        return searchResult.getPlayers()
                .stream()
                .map(FutPlayerElement::getBuyNowPrice)
                .min(Integer::compareTo)
                .map(this::calculateTargetPrice)
                .orElse(0);
    }

    private TransferMarketSearchFilter mapToSearchFilter(Player player) {
            int targetPrice = calculateTargetPrice(player.getPcPrice());
            return TransferMarketSearchFilter.builder()
                    .withName(player.getName())
                    .withTargetPrice(targetPrice)
                    .build();
    }

    private int calculateTargetPrice(int price) {
        int tax = (int) (price * 0.05);
        int targetProfit = Math.max(tax, 500);
        return FutPriceUtils.roundToValidFutPrice(price - tax - targetProfit);
    }

    private boolean needToBid(AuctionData auctionData, Integer targetPrice) {
        if (auctionData.getBidState() == BidState.HIGHEST) {
            return false;
        }
        if (FutPriceUtils.getNextBid(auctionData.getStartingBid(),auctionData.getCurrentBid()) > targetPrice) {
            return false;
        }
        return true;
    }

    private void makeBid(FutPlayerElement player, FutPlayerAuctionData extendedData, Integer targetPrice) {
        BidResult bidResult = player.makeBid();
        AuctionData auction = extendedData.getAuction();
        Integer nextBid = FutPriceUtils.getNextBid(auction.getStartingBid(), auction.getCurrentBid());

        if (bidResult == BidResult.SUCCESS || bidResult == BidResult.OUTBID) {
            auctionService.insertActiveAuction(ActiveAuction.of(auction.getTradeId(), targetPrice));
            log.info("Player bid: name={}, rating={}, bidPrice={}, tradeId={}",
                    extendedData.getName(), extendedData.getRating(), nextBid, auction.getTradeId());

        } else if (bidResult == BidResult.LIMIT_REACHED) {
            telegramBotNotifier.sendMessage("Transfer targets limit reached, stop mass bidding and starting bid checker instead.");
            bidChecker.checkBids(10);

        } else {
            log.warn("Couldn't bid player: name={}, rating={}, bidPrice={}, bidResult={}",
                    extendedData.getName(), extendedData.getRating(), nextBid, bidResult);
        }
        driver.sleep(1000, 2000);
    }
}
