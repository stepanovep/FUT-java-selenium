package stepanovep.fut21.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import stepanovep.fut21.core.driver.FutWebDriver;
import stepanovep.fut21.core.entity.AuctionData;
import stepanovep.fut21.core.entity.BidResult;
import stepanovep.fut21.core.entity.BidState;
import stepanovep.fut21.core.entity.ExtendedDataService;
import stepanovep.fut21.core.entity.FutElement;
import stepanovep.fut21.core.entity.FutElementExtendedData;
import stepanovep.fut21.core.page.transfers.TransferMarketPage;
import stepanovep.fut21.core.page.transfers.TransferSearchResult;
import stepanovep.fut21.core.page.transfers.TransferTargetsPage;
import stepanovep.fut21.core.page.transfers.filter.TransferMarketFilter;
import stepanovep.fut21.mongo.AuctionService;
import stepanovep.fut21.mongo.Player;
import stepanovep.fut21.mongo.PlayerService;
import stepanovep.fut21.utils.FutPriceUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Биддер
 */
@Component
public class Bidding {

    private static final Logger log = LoggerFactory.getLogger(Bidding.class);

    @Autowired
    private FutWebDriver driver;
    @Autowired
    private TransferMarketPage transferMarket;
    @Autowired
    private TransferTargetsPage transferTargets;
    @Autowired
    private ExtendedDataService extendedDataService;
    @Autowired
    private AuctionService auctionService;
    @Autowired
    private PlayerService playerService;

    private static final int MAX_COUNT_BIDS = 5;
    private static final int MAX_TIME = 20 * 60;

    public void massBid() {
        log.info("Mass bidding");
        List<TransferMarketFilter> filters = getPlayersFilters();
        for (TransferMarketFilter filter: filters) {
            transferTargets.checkBids();
            massBidPlayer(filter);
            driver.sleep(2000);
        }

        for (int i = 0; i < 15; i++) {
            transferTargets.checkBids();
            driver.sleep(10000, 15000);
        }

        driver.sleep(2000, 3000);
    }

    private void massBidPlayer(TransferMarketFilter filter) {
        int bidsCount = 0;
        Integer targetPrice = filter.getTargetPrice().orElseThrow(() -> new IllegalStateException("targetPrice is mandatory here"));
        TransferSearchResult searchResult = transferMarket.search(filter);
        for (FutElement player: searchResult.getPlayers()) {
            player.focus();
            driver.sleep(1000, 2000);
            FutElementExtendedData extendedData = extendedDataService.getFutElementExtendedData();
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
        List<Player> players = playerService.getRandomPlayers(25, 2500, 13000);
        return players.stream()
                .map(player -> {
                    int price = player.getPcPrice();
                    int tax = (int) (price * 0.05);
                    int targetProfit = Math.max(tax, 500);
                    return TransferMarketFilter.builder()
                            .withName(player.getName())
                            .withTargetPrice(price - tax - targetProfit)
                            .build();
                })
                .collect(Collectors.toList());
    }

    private boolean needToBid(AuctionData auctionData, Integer targetPrice) {
        if (auctionData.getBidState() == BidState.HIGHEST) {
            return false;
        }
        if (FutPriceUtils.getNextBid(auctionData) > targetPrice) {
            return false;
        }
        return true;
    }

    private void makeBid(FutElement player, FutElementExtendedData extendedData, Integer targetPrice) {
        BidResult bidResult = player.makeBid();
        if (bidResult == BidResult.SUCCESS || bidResult == BidResult.OUTBID) {
            auctionService.insert(extendedData.getAuction().getTradeId(), targetPrice);
            log.info("Player bid: name={}, rating={}, bidPrice={}, tradeId={}",
                    extendedData.getName(), extendedData.getRating(), FutPriceUtils.getNextBid(extendedData.getAuction()),
                    extendedData.getAuction().getTradeId());

        } else {
            log.warn("Couldn't bid player: name={}, rating={}, bidPrice={}, bidResult={}",
                    extendedData.getName(), extendedData.getRating(), FutPriceUtils.getNextBid(extendedData.getAuction()), bidResult);
        }
        driver.sleep(1000, 2000);
    }
}
