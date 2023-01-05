package stepanovep.fut23.bot.service.clubstocking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stepanovep.fut23.bot.service.LoginService;
import stepanovep.fut23.core.driver.FutWebDriver;
import stepanovep.fut23.core.entity.BidResult;
import stepanovep.fut23.core.entity.FutPlayer;
import stepanovep.fut23.core.page.transfers.SearchResult;
import stepanovep.fut23.core.page.transfers.TransferMarketPage;
import stepanovep.fut23.core.page.transfers.TransferTargetsPage;
import stepanovep.fut23.core.page.transfers.search.League;
import stepanovep.fut23.core.page.transfers.search.Quality;
import stepanovep.fut23.core.page.transfers.search.Rarity;
import stepanovep.fut23.core.page.transfers.search.TransferMarketSearchOptions;
import stepanovep.fut23.telegrambot.TelegramNotifier;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class ClubStocker {

    @Autowired
    private TransferMarketPage transferMarketPage;

    @Autowired
    private TransferTargetsPage transferTargetsPage;

    @Autowired
    private FutWebDriver driver;

    @Autowired
    private TelegramNotifier telegramNotifier;

    @Autowired
    private LoginService loginService;

    private int failureCount = 0;

    public void clubStock() {
        TransferMarketSearchOptions.TransferMarketSearchOptionsBuilder searchOptions = TransferMarketSearchOptions.builder()
                .withBidMax(750)
                .withQuality(Quality.GOLD)
                .withRarity(Rarity.RARE);

        List<League> leagues = new ArrayList<>(List.of(
                League.PREMIER_LEAGUE,
//                League.LIGUE_1,
                League.BUNDESLIGA,
                League.LA_LIGA,
                League.SERIE_A
        ));
        Collections.shuffle(leagues);

        for (League league: leagues) {
            MassBidResult massBidResult = massBid(searchOptions.withLeague(league).build());
            if (massBidResult == MassBidResult.TRANSFER_TARGETS_LIMIT_REACHED) {
                int wonBids = checkWonBids();
                if (wonBids >= 40) {
                    telegramNotifier.sendMessage("Club stocking iteration complete");
                    GameApplicationService gameApplicationService = new GameApplicationService();
                    gameApplicationService.moveTransferTargetsToUnassignedPile();
                    driver.screenshot();
                    return;

                } else {
                    log.info("Targets limit reached, but not enough players bought - restarting club stock");
                    driver.sleep(30 * 1000);
                    break;
                }

            } else if (massBidResult == MassBidResult.BID_CHANGED_ERROR) {
                log.info("Bid changed error popping up, probably need to pause 1 minute");
                failureCount++;
                driver.sleep(45 * 1000);

            } else if (massBidResult == MassBidResult.TOO_MANY_ACTIONS_ERROR) {
                log.info("Too many actions performed. Need 5 minutes pause");
                failureCount++;
                driver.sleep(300 * 1000);
            }

            if (failureCount >= 2) {
                telegramNotifier.sendMessage("Club stock process failed. Reentering to FUT and starting again");
                failureCount = 0;
                loginService.login();
                clubStock();
                return;
            }

            driver.sleep(12 * 1000);
        }

        clubStock();
    }

    private MassBidResult massBid(TransferMarketSearchOptions searchOptions) {
        transferMarketPage.applySearchOptions(searchOptions);

        SearchResult searchResult = transferMarketPage.search();
//        waitUntil30SecondsLeft(searchResult);

        int bidCount = 0;
        for (FutPlayer playerElement : searchResult.getPlayers()) {
            playerElement.focus();

            if (playerElement.getExpirationTime().compareTo(Duration.ofMinutes(2)) <= 0 && bidCount < 8) {
                if (needToSkip(playerElement, searchOptions)) {
                    continue;
                }
                BidResult bidResult = playerElement.makeBid();
                if (bidResult == BidResult.SUCCESS | bidResult == BidResult.OUTBID) {
                    bidCount++;

                } else if (bidResult == BidResult.BID_CHANGED_ERROR) {
                    return MassBidResult.BID_CHANGED_ERROR;

                } else if (bidResult == BidResult.LIMIT_REACHED) {
                    driver.sleep(2000);
                    return MassBidResult.TRANSFER_TARGETS_LIMIT_REACHED;

                } else if (bidResult == BidResult.TOO_MANY_ACTIONS_ERROR) {
                    return MassBidResult.TOO_MANY_ACTIONS_ERROR;
                }

                driver.sleep(2000, 2500);

            } else {
                log.info("Stop bidding: bid count = {}", bidCount);
                break;
            }
        }

        driver.sleep(2000);
        return MassBidResult.CONTINUE;
    }

    private boolean needToSkip(FutPlayer playerElement, TransferMarketSearchOptions searchOptions) {
        if (playerElement.getNextBid().isEmpty()) {
            return true;
        }
        int maxPrice = searchOptions.getBidMax().orElseThrow() + 50;
        return playerElement.getNextBid().get() > maxPrice;
    }

    private void waitUntil30SecondsLeft(SearchResult searchResult) {
        FutPlayer firstPlayer = searchResult.getPlayers().get(0);

        if (firstPlayer.getExpirationTime().compareTo(Duration.ofMinutes(2)) > 0) {
            log.info("Need to wait too long - abort waiting");
            return;
        }

        while (firstPlayer.getExpirationTime().compareTo(Duration.ofSeconds(31)) >= 0) {
            driver.sleep(2500);
        }
    }

    private enum MassBidResult {
        CONTINUE,
        TRANSFER_TARGETS_LIMIT_REACHED,
        BID_CHANGED_ERROR,
        TOO_MANY_ACTIONS_ERROR
    }

    private int checkWonBids() {
        log.info("Checking won items");
        driver.sleep(15000);
        transferTargetsPage.navigateToPage();

        driver.sleep(45 * 1000);
        transferTargetsPage.sendAllToClub();
        transferTargetsPage.clearAllExpiredItems();

        List<FutPlayer> wonPlayers = transferTargetsPage.getWonItems();
        return wonPlayers.size();
    }
}
