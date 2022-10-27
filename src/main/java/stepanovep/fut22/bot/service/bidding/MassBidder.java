package stepanovep.fut22.bot.service.bidding;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import stepanovep.fut22.core.Platform;
import stepanovep.fut22.core.driver.FutWebDriver;
import stepanovep.fut22.core.entity.AuctionData;
import stepanovep.fut22.core.entity.BidResult;
import stepanovep.fut22.core.entity.BidState;
import stepanovep.fut22.core.entity.FutPlayerAuctionData;
import stepanovep.fut22.core.entity.FutPlayerElement;
import stepanovep.fut22.core.entity.PlayerAuctionDataService;
import stepanovep.fut22.core.page.transfers.TransferMarketPage;
import stepanovep.fut22.core.page.transfers.SearchResult;
import stepanovep.fut22.core.page.transfers.search.TransferMarketSearchOptions;
import stepanovep.fut22.mongo.ActiveAuction;
import stepanovep.fut22.mongo.AuctionService;
import stepanovep.fut22.mongo.Player;
import stepanovep.fut22.mongo.PlayerService;
import stepanovep.fut22.telegrambot.TelegramNotifier;
import stepanovep.fut22.utils.FutPriceUtils;

import java.util.List;

/**
 * Биддер
 */
@Component
@Slf4j
public class MassBidder {

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
    private TelegramNotifier telegramNotifier;

    private static final int MAX_COUNT_BIDS = 5;
    private static final int MAX_BID_EXPIRATION_TIME_LEFT_IN_SECONDS = 20 * 60;

    public void massBid() {
        try {
            log.info("Mass bidding");
            List<Player> players = playerService.getPlayersForMassBid(20, 1100, 4000, driver.getPlatform());
            for (Player player: players) {
                bidChecker.checkBids();
                massBidPlayer(player);
                driver.sleep(2000);
            }

            driver.sleep(2000, 3000);

        } catch (Exception exc) {
            log.error("Mass bid failed: ", exc);
            telegramNotifier.notifyAboutException(driver.screenshot());
        }
    }

    private void massBidPlayer(Player player) {
        int bidsCount = 0;
        TransferMarketSearchOptions searchOptions = mapToSearchOptions(player);
        Integer targetPrice = searchOptions.getTargetPrice().orElseThrow(() -> new IllegalStateException("targetPrice is mandatory here"));
        SearchResult searchResult = transferMarket.applySearchOptionsAndSearch(searchOptions);
        if (targetPrice > getAdjustedTargetPrice(searchResult)) {
            targetPrice = getAdjustedTargetPrice(searchResult);
            int newActualPrice = Math.max((int) (targetPrice * 1.15), (int) (targetPrice * 1.1) + 500);
            playerService.updatePriceByFutbinId(player.getFutbinId(), FutPriceUtils.roundToValidFutPrice(newActualPrice), Platform.PC);
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

    private int getAdjustedTargetPrice(SearchResult searchResult) {
        return searchResult.getPlayers()
                .stream()
                .map(FutPlayerElement::getBuyNowPrice)
                .min(Integer::compareTo)
                .map(this::calculateTargetPrice)
                .orElse(0);
    }

    private TransferMarketSearchOptions mapToSearchOptions(Player player) {
        int targetPrice = calculateTargetPrice(player.getPrice(driver.getPlatform()));
        return TransferMarketSearchOptions.builder()
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

    private BidResult makeBid(FutPlayerElement player, FutPlayerAuctionData extendedData, Integer targetPrice) {
        BidResult bidResult = player.makeBid();
        AuctionData auction = extendedData.getAuction();
        Integer nextBid = FutPriceUtils.getNextBid(auction.getStartingBid(), auction.getCurrentBid());

        if (bidResult == BidResult.SUCCESS || bidResult == BidResult.OUTBID) {
            auctionService.insertActiveAuction(ActiveAuction.of(auction.getTradeId(), targetPrice));
            log.info("Player bid: name={}, rating={}, bidPrice={}, tradeId={}",
                    extendedData.getName(), extendedData.getRating(), nextBid, auction.getTradeId());

        } else if (bidResult == BidResult.LIMIT_REACHED) {
            telegramNotifier.sendMessage("Transfer targets limit reached, stop mass bidding and starting bid checker instead.");
            bidChecker.checkBids(10);

        } else {
            // TODO если не удалось сделать ставку, то надо либо:
            // 1) просто выйти
            // 2) идти проверять ставки
            // 3) убрать игрока их expired списка, потом попытаться сделать ставку еще раз
            log.warn("Couldn't bid player: name={}, rating={}, bidPrice={}, bidResult={}",
                    extendedData.getName(), extendedData.getRating(), nextBid, bidResult);
        }
        driver.sleep(1000, 2000);
        return bidResult;
    }
}
