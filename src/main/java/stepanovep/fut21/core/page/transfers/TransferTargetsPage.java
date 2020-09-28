package stepanovep.fut21.core.page.transfers;

import com.mongodb.client.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import stepanovep.fut21.core.driver.FutWebDriver;

/**
 * Page Object для страницы управления аукционами
 */
@Component
public class TransferTargetsPage {

    @Autowired
    private FutWebDriver driver;

    @Autowired
    private MongoClient mongoClient;

    public static void checkBids() {

    }

    public void removeExpiredItems() {

    }

}
