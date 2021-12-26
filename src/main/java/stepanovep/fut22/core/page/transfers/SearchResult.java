package stepanovep.fut22.core.page.transfers;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import stepanovep.fut22.core.driver.FutWebDriver;
import stepanovep.fut22.core.entity.FutPlayerElement;
import stepanovep.fut22.core.locators.TransferMarketLocators;
import stepanovep.fut22.core.page.FutActiveMenu;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SearchResult {

    private final List<FutPlayerElement> players;

    private SearchResult(List<FutPlayerElement> players) {
        this.players = players;
    }

    public static SearchResult from(FutWebDriver driver) {
        new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofMillis(100))
                .ignoring(NoSuchElementException.class)
                .until(webDriver -> {
                    try {
                        webDriver.findElement(TransferMarketLocators.NO_RESULTS_ICON);
                        return true;
                    } catch (Exception exc) {
                        webDriver.findElement(TransferMarketLocators.SEARCH_RESULT_ITEMS);
                        return true;
                    }
                });

        List<FutPlayerElement> items = driver.findElements(TransferMarketLocators.SEARCH_RESULT_ITEMS)
                .stream()
                .map(webElem -> new FutPlayerElement(driver, webElem))
                .collect(Collectors.toList());

        driver.activeMenu = FutActiveMenu.TRANSFER_SEARCH_RESULT;
        return new SearchResult(items);
    }

    @Nonnull
    public List<FutPlayerElement> getPlayers() {
        return players == null ? Collections.emptyList() : players;
    }


}
