package stepanovep.fut21.core.page.transfers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import stepanovep.fut21.core.driver.FutWebDriver;

/**
 * Page Object для страницы управления текущим трансферным списком
 */
@Component
public class TransferListPage {

    @Autowired
    private FutWebDriver driver;

    public void relistAll() {

    }
}
