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
import stepanovep.fut21.core.entity.BidResult;
import stepanovep.fut21.core.entity.ExtendedDataService;
import stepanovep.fut21.core.entity.FutElement;
import stepanovep.fut21.core.entity.FutElementExtendedData;
import stepanovep.fut21.core.locators.MainPageLocators;
import stepanovep.fut21.core.locators.TransferTargetsLocators;
import stepanovep.fut21.core.page.FutActiveMenu;
import stepanovep.fut21.mongo.AuctionService;
import stepanovep.fut21.mongo.AuctionTrade;
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
    private ExtendedDataService extendedDataService;
    @Autowired
    private AuctionService auctionService;

    @Retryable(include = {StaleElementReferenceException.class}, backoff = @Backoff(delay = 3000))
    public void checkBids() {
        navigateToPage();
        List<FutElement> activeBids = getActiveBids();
        log.info("Checking bids: activeBids count = {}", activeBids.size());

        for (FutElement player: activeBids) {
            if (player.isOutbid()) {
                driver.sleep(1000, 2000);
                player.focus();
                FutElementExtendedData extendedData = extendedDataService.getFutElementExtendedData();
                if (extendedData.getAuction().getExpires() > MAX_EXPIRATION_TIME_TO_CHECK) {
                    log.info("Expiration time is too far - check bids later");
                    break;
                }
                String tradeId = extendedData.getAuction().getTradeId();

                log.info("Player is outbid: name={}, tradeId={}", extendedData.getName(), tradeId);
                Optional<AuctionTrade> auctionTrade = auctionService.get(tradeId);
                if (auctionTrade.isPresent()) {
                    Integer targetPrice = auctionTrade.get().getTargetPrice();
                    if (FutPriceUtils.getNextBid(extendedData.getAuction()) <= targetPrice) {
                        rebid(player);

                    } else {
                        log.info("Player bid is too high: name={}", extendedData.getName());
                        unwatchItem(player);
                    }
                }
            }
        }

        removeOneExpiredItem(10);
        driver.sleep(2000, 4000);
    }

    @Recover
    public void recover(StaleElementReferenceException exc) {
        log.error("Checking bids failed consecutive times");
    }

    private void navigateToPage() {
        if (driver.activeMenu != FutActiveMenu.TRANSFER_TARGETS) {
            driver.clickElement(MainPageLocators.GO_TO_TRANSFERS);
            driver.clickElement(MainPageLocators.GO_TO_TRANSFER_TARGETS);
        }
        driver.activeMenu = FutActiveMenu.TRANSFER_TARGETS;
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
        driver.sleep(400, 600);
        BidResult bidResult = player.makeBid();
        if (bidResult != BidResult.SUCCESS) {
            removeOneExpiredItem();
            checkBids();
        }
        driver.sleep(400, 600);
    }

    private void unwatchItem(FutElement player) {
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
            FutElement expiredItem = new FutElement(driver, expiredItems.get(0));
            expiredItem.focus();
            expiredItem.toggleWatch();
        }
        driver.sleep(1000, 2000);
    }

}
