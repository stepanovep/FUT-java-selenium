package stepanovep.fut21.api;

import com.mongodb.client.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import stepanovep.fut21.core.driver.FutWebDriver;
import stepanovep.fut21.core.page.transfers.TransferMarketPage;
import stepanovep.fut21.core.page.transfers.TransferTargetsPage;
import stepanovep.fut21.core.page.transfers.filter.TransferMarketFilter;

import java.util.List;

/**
 * Биддер
 */
@Component
public class Bidding {

    @Autowired
    private FutWebDriver driver;

    @Autowired
    private TransferMarketPage transferMarket;

    @Autowired
    private TransferTargetsPage transferTargets;

    @Autowired
    private MongoClient mongoClient;

    public void massBidPlayers(List<TransferMarketFilter> filters) {
        for (TransferMarketFilter filter: filters) {
            transferMarket.search(filter);
        }
    }
}
