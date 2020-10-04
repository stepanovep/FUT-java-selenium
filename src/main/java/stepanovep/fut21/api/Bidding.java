package stepanovep.fut21.api;

import com.mongodb.client.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import stepanovep.fut21.core.driver.FutWebDriver;
import stepanovep.fut21.core.entity.AuctionData;
import stepanovep.fut21.core.entity.BidState;
import stepanovep.fut21.core.entity.ExtendedDataService;
import stepanovep.fut21.core.entity.FutElement;
import stepanovep.fut21.core.entity.FutElementExtendedData;
import stepanovep.fut21.core.locators.TransferMarketLocators;
import stepanovep.fut21.core.page.transfers.TransferMarketPage;
import stepanovep.fut21.core.page.transfers.TransferSearchResult;
import stepanovep.fut21.core.page.transfers.TransferTargetsPage;
import stepanovep.fut21.core.page.transfers.filter.TransferMarketFilter;
import stepanovep.fut21.mongo.AuctionTrade;
import stepanovep.fut21.utils.FutPriceUtils;

import java.util.List;

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
    private MongoCollection<AuctionTrade> auctions;

    private final static Integer MAX_TIME = 15 * 60;

    public void massBid(List<TransferMarketFilter> filters) {
        log.info("Mass bidding");
        for (TransferMarketFilter filter: filters) {
            Integer targetPrice = filter.getTargetPrice().orElseThrow(() -> new IllegalStateException("targetPrice is mandatory here"));
            TransferSearchResult searchResult = transferMarket.search(filter);

            for (FutElement player: searchResult.getPlayers()) {
                player.focus();
                driver.sleep(1500, 3000);
                FutElementExtendedData extendedData = extendedDataService.getFutElementExtendedData();
                AuctionData auction = extendedData.getAuction();
                if (auction.getExpires() > MAX_TIME) {
                    log.info("Skipping this and next items due to the time left filter");
                    break;
                }
                if (needToBid(auction, targetPrice)) {
                    makeBid(extendedData, targetPrice);
                }
            }
        }
        driver.sleep(2000, 3000);
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

    private void makeBid(FutElementExtendedData extendedData, Integer targetPrice) {
        driver.clickElement(TransferMarketLocators.BID_BUTTON);
        driver.sleep(500, 1000);
        FutElementExtendedData updatedData = extendedDataService.getFutElementExtendedData();
        if (updatedData.getAuction().getBidState() == BidState.HIGHEST) {
            log.info("Player bidded: name={}, rating={}, bidPrice={}",
                    extendedData.getName(), extendedData.getRating(), FutPriceUtils.getNextBid(extendedData.getAuction()));
            auctions.insertOne(AuctionTrade.of(extendedData.getAuction().getTradeId(), targetPrice));

        } else {
            log.warn("Couldn't bid player: name={}, rating={}, bidPrice={}",
                    extendedData.getName(), extendedData.getRating(), FutPriceUtils.getNextBid(extendedData.getAuction()));
        }
    }
}
