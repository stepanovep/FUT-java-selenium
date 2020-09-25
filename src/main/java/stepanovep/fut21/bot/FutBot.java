package stepanovep.fut21.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stepanovep.fut21.core.FutWebDriver;
import stepanovep.fut21.core.page.transfers.TransferMarketPage;
import stepanovep.fut21.core.page.transfers.filter.ChemStyle;
import stepanovep.fut21.core.page.transfers.filter.Quality;
import stepanovep.fut21.core.page.transfers.filter.TransferMarketFilter;

@Service
public class FutBot {

    @Autowired
    private FutWebDriver driver;

    @Autowired
    private TransferMarketPage transferMarketPage;

    public void test() {
        transferMarketPage.navigateToPage();

        var filter = TransferMarketFilter.builder()
                .withName("Firmino")
                .withQuality(Quality.SPECIAL)
                .withChemStyle(ChemStyle.HUNTER)
                .withBuyNowMax(1_000_000)
                .build();

        var searchResult = transferMarketPage.search(filter);
    }

    public void shutdown() {
        driver.quit();
    }
}
