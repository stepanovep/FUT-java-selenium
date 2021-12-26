package stepanovep.fut22.bot.service.sniping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stepanovep.fut22.core.driver.FutWebDriver;
import stepanovep.fut22.core.entity.FutPlayerElement;
import stepanovep.fut22.core.page.transfers.TransferMarketPage;
import stepanovep.fut22.core.page.transfers.SearchResult;
import stepanovep.fut22.core.page.transfers.search.TransferMarketSearchOptions;
import stepanovep.fut22.telegrambot.TelegramNotifier;

import java.util.List;

@Service
public class SnipingService {

    private static final Logger log = LoggerFactory.getLogger(SnipingService.class);

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
                List<FutPlayerElement> players = searchResult.getPlayers();
                for (FutPlayerElement player: players) {
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
