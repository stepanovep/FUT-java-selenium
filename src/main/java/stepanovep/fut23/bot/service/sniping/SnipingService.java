package stepanovep.fut23.bot.service.sniping;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stepanovep.fut23.core.driver.FutWebDriver;
import stepanovep.fut23.core.entity.FutPlayer;
import stepanovep.fut23.core.page.transfers.SearchResult;
import stepanovep.fut23.core.page.transfers.TransferMarketPage;
import stepanovep.fut23.core.page.transfers.search.TransferMarketSearchOptions;
import stepanovep.fut23.telegrambot.TelegramNotifier;

import java.util.List;

@Service
@Slf4j
public class SnipingService {

    @Autowired
    private FutWebDriver driver;

    @Autowired
    private TransferMarketPage transferMarket;

    @Autowired
    private TelegramNotifier telegramNotifier;

    public void snipe(TransferMarketSearchOptions searchOptions) {
        transferMarket.applySearchOptions(searchOptions);

        for (int i = 0 ; i < 20; i++) {
            updateSearchOptions(i);
            SearchResult searchResult = transferMarket.search();
            if (!searchResult.getPlayers().isEmpty()) {
                List<FutPlayer> players = searchResult.getPlayers();
                for (FutPlayer player: players) {
                    log.info("Player found - trying to snipe: {}", player);
                    player.buyNow();
                    break;
                }

            } else {
                log.info("No players found");
            }

            transferMarket.backToSearchForm();
            driver.sleep(600, 900);
        }
    }

    private void updateSearchOptions(int i) {
        transferMarket.changeMinBuyNowPrice(1);
    }
}
