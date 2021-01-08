package stepanovep.fut21.bot.service.sniping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stepanovep.fut21.core.driver.FutWebDriver;
import stepanovep.fut21.core.entity.FutPlayerElement;
import stepanovep.fut21.core.page.transfers.TransferMarketPage;
import stepanovep.fut21.core.page.transfers.TransferSearchResult;
import stepanovep.fut21.core.page.transfers.filter.TransferMarketSearchFilter;
import stepanovep.fut21.telegrambot.TelegramNotifier;

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

    public void snipe(TransferMarketSearchFilter searchFilter) {
        transferMarket.applyFilter(searchFilter);

        for (int i = 0 ; i < 20; i++) {
            updateSearchFilterForm(i);
            TransferSearchResult searchResult = transferMarket.search();
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

    private void updateSearchFilterForm(int i) {
        transferMarket.changeMinBuyNowPriceFilter(1);
    }
}
