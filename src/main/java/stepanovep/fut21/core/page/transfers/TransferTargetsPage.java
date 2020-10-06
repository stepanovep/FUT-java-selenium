package stepanovep.fut21.core.page.transfers;

import com.mongodb.client.MongoCollection;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import stepanovep.fut21.core.driver.FutWebDriver;
import stepanovep.fut21.core.entity.BidResult;
import stepanovep.fut21.core.entity.ExtendedDataService;
import stepanovep.fut21.core.entity.FutElement;
import stepanovep.fut21.core.entity.FutElementExtendedData;
import stepanovep.fut21.core.locators.MainPageLocators;
import stepanovep.fut21.core.locators.TransferTargetsLocators;
import stepanovep.fut21.mongo.AuctionTrade;
import stepanovep.fut21.utils.FutPriceUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;

/**
 * Page Object для страницы управления аукционами
 */
@Component
public class TransferTargetsPage {

    private static final Logger log = LoggerFactory.getLogger(TransferTargetsPage.class);

    @Autowired
    private FutWebDriver driver;
    @Autowired
    private ExtendedDataService extendedDataService;
    @Autowired
    private MongoCollection<AuctionTrade> auctions;

    public void checkBids() {
        log.info("checking bids...");
        navigateToPage();
        List<FutElement> activeBids = getActiveBids();
        for (FutElement player: activeBids) {
            if (player.isOutbid()) {
                driver.sleep(1000, 2000);
                player.focus();
                FutElementExtendedData extendedData = extendedDataService.getFutElementExtendedData();
                log.info("Player is outbid: {}", extendedData.getName());

                AuctionTrade auctionTrade = auctions.find(eq("tradeId", extendedData.getAuction().getTradeId())).first();
                if (auctionTrade != null) {
                    Integer targetPrice = auctionTrade.getTargetPrice();
                    if (FutPriceUtils.getNextBid(extendedData.getAuction()) <= targetPrice) {
                        rebid(player);
                    } else {
                        log.info("Player bid is too high. Stop tracking this player");
                        player.toggleWatch();
                    }
                } else {
                    log.info("Player has no target price - he was bid manually: {}", extendedData.getName());
                }
            }
        }
    }

    private void navigateToPage() {
        driver.clickElement(MainPageLocators.GO_TO_TRANSFERS);
        driver.clickElement(MainPageLocators.GO_TO_TRANSFER_TARGETS);
        driver.sleep(500);
    }

    private List<FutElement> getActiveBids() {
        WebElement activeBidsSection = driver.findElementWithWait(TransferTargetsLocators.ACTIVE_BIDS_SECTION);
        List<WebElement> targets = activeBidsSection.findElements(TransferTargetsLocators.SECTION_ELEMENTS);

        return targets.stream()
                .map(target -> new FutElement(driver, target))
                .collect(Collectors.toList());
    }

    private void rebid(FutElement player) {
        driver.sleep(300, 600);
        player.makeBid();
    }


    public void removeExpiredItems() {

    }

}
