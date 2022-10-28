package stepanovep.fut23.bot.service.bidding;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import stepanovep.fut23.core.driver.FutWebDriver;
import stepanovep.fut23.core.entity.AuctionData;
import stepanovep.fut23.core.entity.BidResult;
import stepanovep.fut23.core.entity.FutPlayerAuctionData;
import stepanovep.fut23.core.entity.FutPlayerElement;
import stepanovep.fut23.core.entity.PlayerAuctionDataService;
import stepanovep.fut23.core.page.FutActiveMenu;
import stepanovep.fut23.core.page.transfers.SearchResult;
import stepanovep.fut23.core.page.transfers.TransferMarketPage;
import stepanovep.fut23.core.page.transfers.TransferTargetsPage;
import stepanovep.fut23.core.page.transfers.search.TransferMarketSearchOptions;
import stepanovep.fut23.mongo.ActiveAuction;
import stepanovep.fut23.mongo.AuctionService;
import stepanovep.fut23.mongo.Player;
import stepanovep.fut23.mongo.PlayerService;
import stepanovep.fut23.mongo.WonAuction;
import stepanovep.fut23.telegrambot.TelegramNotifier;
import stepanovep.fut23.utils.FutPriceUtils;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class BidChecker {

    private static final int MAX_EXPIRATION_TIME_SECONDS_TO_CHECK = 50;

    @Autowired
    private FutWebDriver driver;
    @Autowired
    private TransferTargetsPage transferTargetsPage;
    @Autowired
    private TransferMarketPage transferMarketPage;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private PlayerAuctionDataService playerAuctionDataService;
    @Autowired
    private AuctionService auctionService;
    @Autowired
    private TelegramNotifier telegramNotifier;

    public void checkBids(int repeat) {
        for (int i = 0; i < repeat; i++) {
            try {
                checkBids();

            } catch (StaleElementReferenceException exc) {
                driver.activeMenu = FutActiveMenu.HOME;
                log.error("Bid checker failed", exc);

            } catch (Exception exc) {
                log.error("Bid checker failed", exc);
                telegramNotifier.notifyAboutException(driver.screenshot());
                return;
            }
        }
    }

    @Retryable(include = {StaleElementReferenceException.class}, backoff = @Backoff(delay = 3000))
    public void checkBids() {
        transferTargetsPage.navigateToPage();
        driver.sleep(1000);
        List<FutPlayerElement> activeBids = transferTargetsPage.getActiveBids();
        if (activeBids.isEmpty()) {
            return;
        }
        log.info("Checking bids: activeBids count = {}", activeBids.size());

        for (FutPlayerElement player : activeBids) {
            if (player.isOutbid()) {
                driver.sleep(700, 1200);
                player.focus();
                FutPlayerAuctionData extendedData = playerAuctionDataService.getFutPlayerAuctionData();
                if (extendedData.getAuction().getExpires() > MAX_EXPIRATION_TIME_SECONDS_TO_CHECK) {
                    log.info("Expiration time is too far - check bids later");
                    break;
                }
                handleOutbidPlayer(player, extendedData);
            }
        }

//        if (checkAnyOutbidIsExpiring()) {
//            checkBids();
//        }

        listWonItemsToTransferMarket();
        keepBalanceOfExpiredItems();

//        if (checkAnyOutbidIsExpiring()) {
//            checkBids();
//        }

        driver.sleep(2000, 3000);
    }

    private boolean checkAnyOutbidIsExpiring() {
        List<FutPlayerElement> activeBids;
        driver.sleep(1000);
        activeBids = transferTargetsPage.getActiveBids();
        for (FutPlayerElement player: activeBids) {
            if (player.isOutbid()) {
                driver.sleep(600, 900);
                player.focus();
                FutPlayerAuctionData extendedData = playerAuctionDataService.getFutPlayerAuctionData();
                if (extendedData.getAuction().getExpires() <= 30) {
                    log.info("Some outbid auctions are expiring - need to run bid checker again");
                    return true;
                }

                return false;
            }
        }

        return false;
    }

    @Recover
    public void recover(StaleElementReferenceException exc) {
        log.error("Checking bids failed consecutive times");
    }

    @Retryable(include = {StaleElementReferenceException.class, TimeoutException.class}, backoff = @Backoff(delay = 3000))
    public void listWonItemsToTransferMarket() {
        List<FutPlayerElement> activeBids = transferTargetsPage.getActiveBids();
        Duration earliestExpirationTime = Duration.ofHours(1L);
        if (!activeBids.isEmpty()) {
            earliestExpirationTime = activeBids.get(0).getExpirationTime();
        }
        if (earliestExpirationTime.compareTo(Duration.ofSeconds(16L)) < 0) {
            return;
        }

        List<FutPlayerElement> wonItems = transferTargetsPage.getWonItems()
                .stream()
                .filter(item -> {
                    item.focus();
                    return item.isTradable();
                })
                .collect(Collectors.toList());

        while (!wonItems.isEmpty()) {
            FutPlayerElement playerElement = wonItems.get(0);
            listPlayerToTransferMarket(playerElement);

            driver.sleep(1000, 2000);
            wonItems = transferTargetsPage.getWonItems()
                    .stream()
                    .filter(item -> {
                        item.focus();
                        return item.isTradable();
                    })
                    .collect(Collectors.toList());;
        }
    }

    private void listPlayerToTransferMarket(FutPlayerElement playerElement) {
        playerElement.focus();
        if (playerElement.isUntradable()) {
            log.error("Player is untradable. Skipping item");
            return;
        }
        FutPlayerAuctionData extendedData = playerAuctionDataService.getFutPlayerAuctionData();
        String resourceId = extendedData.getResourceId();

        int boughtPrice = playerElement.getBoughtPrice();
        String tradeId = extendedData.getAuction().getTradeId();

        Optional<Player> playerOpt = playerService.getByResourceId(resourceId);
        Optional<ActiveAuction> auctionOpt = auctionService.getActiveAuction(tradeId);

        Integer marketPrice = null;

        if (playerOpt.isPresent()) {
            Player player = playerOpt.get();
            marketPrice = player.getPrice(driver.getPlatform());

        } else if (auctionOpt.isPresent()) {
            ActiveAuction auction = auctionOpt.get();
            int targetPrice = auction.getTargetPrice();
            marketPrice = FutPriceUtils.roundToValidFutPrice((int) Math.max((targetPrice * 10.0) / 9, (targetPrice + 500) / 0.95));

        } else {
            playerElement.sendToTransferMarket();
        }

        if (marketPrice != null) {
            int extraMargin = 0; // 200
            int listBinPrice = Math.max(marketPrice + extraMargin, (int) (boughtPrice * 1.1));
            String message = String.format("Bid won! %s: bidPrice=%d, marketPrice=%d", extendedData.getName(), boughtPrice, marketPrice);
            log.info(message);
            telegramNotifier.notifyAboutBoughtPlayer(driver.screenshot(), message);
            playerElement.listToTransferMarket(listBinPrice - 200, listBinPrice);
            auctionService.insertWonAuction(WonAuction.builder()
                    .withTradeId(tradeId)
                    .withPlayerName(extendedData.getName())
                    .withPlayerRating(extendedData.getRating())
                    .withBoughtPrice(boughtPrice)
                    .withPotentialProfit((int) (listBinPrice * 0.95 - boughtPrice))
                    .build());
        }
    }

    private void handleOutbidPlayer(FutPlayerElement player, FutPlayerAuctionData extendedData) {
        // TODO: снизить количество ставок. Большинство ставок все равно проигрывается в итоге - тратится количество ставок
        //    - Делать ставку только если осталось <15 секунд.
        //    - Повышать ставку на 2 шага
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
                player.toggleWatch();
            }
        }
    }

    private void rebid(FutPlayerElement player) {
        BidResult bidResult = player.makeBid();
        if (bidResult != BidResult.SUCCESS) {
            bidResult = player.makeBid();
            if (bidResult != BidResult.SUCCESS) {
                removeOneExpiredItem();
                checkBids();
            }
        }
        // TODO log to the console and file as well
        log.info("Player has been rebid");
        driver.sleep(400, 600);
    }

    private void keepBalanceOfExpiredItems() {
        List<FutPlayerElement> expiredItems = transferTargetsPage.getExpiredItems();
        List<FutPlayerElement> watchedItems = transferTargetsPage.getWatchedItems();

        if (expiredItems.size() >= 20) {
            transferTargetsPage.clearAllExpiredItems();

        } else if (expiredItems.size() >= 10) {
            FutPlayerElement expiredItem = expiredItems.get(0);
            expiredItem.focus();
            expiredItem.toggleWatch();
            driver.sleep(1000);

        } else if (expiredItems.size() + watchedItems.size() < 5) {
            addWatchItems(6);
        }

        driver.sleep(750, 1500);
    }

    private void removeOneExpiredItem() {
        List<FutPlayerElement> expiredItems = transferTargetsPage.getExpiredItems();
        if (!expiredItems.isEmpty()) {
            expiredItems.get(0).toggleWatch();
        }
        driver.sleep(1000);
    }

    private void addWatchItems(int count) {
        TransferMarketSearchOptions emptyOptions = TransferMarketSearchOptions.builder().build();
        SearchResult searchResult = transferMarketPage.applySearchOptionsAndSearch(emptyOptions);
        List<FutPlayerElement> players = searchResult.getPlayers()
                .stream()
                .limit(count)
                .collect(Collectors.toList());

        players.forEach(player -> {
            player.focus();
            player.toggleWatch();
            driver.sleep(1000, 2000);
        });
    }
}