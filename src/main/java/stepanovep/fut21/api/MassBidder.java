package stepanovep.fut21.api;

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
import stepanovep.fut21.core.page.transfers.filter.TransferMarketFilter;
import stepanovep.fut21.mongo.AuctionService;
import stepanovep.fut21.mongo.Player;
import stepanovep.fut21.mongo.PlayerService;
import stepanovep.fut21.telegrambot.TelegramBotNotifier;
import stepanovep.fut21.utils.FutPriceUtils;

import java.util.List;
import java.util.stream.Collectors;

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

    private static final int MAX_COUNT_BIDS = 7;
    private static final int MAX_TIME = 20 * 60;

    public void massBid() {
        driver.wakeup();
        try {
            log.info("Mass bidding");
            List<TransferMarketFilter> filters = getPlayersFilters();
            for (TransferMarketFilter filter : filters) {
                if (driver.isInterrupted()) {
                    System.out.println("Thread interrupted - aborting mass bidding");
                    return;
                }
                bidChecker.checkBids();
                massBidPlayer(filter);
                driver.sleep(2000);
            }

            for (int i = 0; i < 15; i++) {
                if (driver.isInterrupted()) {
                    System.out.println("Thread interrupted - aborting mass bidding");
                    return;
                }
                bidChecker.checkBids();
                driver.sleep(10000, 15000);
            }

            driver.sleep(2000, 3000);

        } catch (Exception exc) {
            log.error("Mass bid failed: ", exc);
            telegramBotNotifier.notifyAboutException(driver.screenshot());
            return;
        }

        log.info("Mass bidding successfully finished");
        telegramBotNotifier.sendMessage("Mass bidding successfully finished");
    }

    private void massBidPlayer(TransferMarketFilter filter) {
        int bidsCount = 0;
        Integer targetPrice = filter.getTargetPrice().orElseThrow(() -> new IllegalStateException("targetPrice is mandatory here"));
        TransferSearchResult searchResult = transferMarket.search(filter);
        for (FutPlayerElement player: searchResult.getPlayers()) {
            player.focus();
            driver.sleep(1000, 2000);
            FutPlayerAuctionData extendedData = playerAuctionDataService.getFutPlayerAuctionData();
            AuctionData auction = extendedData.getAuction();
            if (auction.getExpires() > MAX_TIME || bidsCount >= MAX_COUNT_BIDS) {
                log.info("Skipping this and next items due to the time left filter");
                break;
            }
            if (needToBid(auction, targetPrice)) {
                makeBid(player, extendedData, targetPrice);
                bidsCount++;
            }
        }
    }

    private List<TransferMarketFilter> getPlayersFilters() {
        List<Player> players = playerService.getRandomPlayers(20, 2500, 13000);
        return players.stream()
                .map(player -> {
                    int price = player.getPcPrice();
                    int tax = (int) (price * 0.05);
                    int targetProfit = Math.max(tax, 500);
                    int targetPrice = FutPriceUtils.roundToValidFutPrice(price - tax - targetProfit);
                    return TransferMarketFilter.builder()
                            .withName(player.getName())
                            .withTargetPrice(targetPrice)
                            .build();
                })
                .collect(Collectors.toList());
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
            auctionService.insert(auction.getTradeId(), targetPrice);
            log.info("Player bid: name={}, rating={}, bidPrice={}, tradeId={}",
                    extendedData.getName(), extendedData.getRating(), nextBid, auction.getTradeId());

        } else {
            log.warn("Couldn't bid player: name={}, rating={}, bidPrice={}, bidResult={}",
                    extendedData.getName(), extendedData.getRating(), nextBid, bidResult);
        }
        driver.sleep(1000, 2000);
    }
}
