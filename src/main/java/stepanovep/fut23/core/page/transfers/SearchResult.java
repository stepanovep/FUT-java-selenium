package stepanovep.fut23.core.page.transfers;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import stepanovep.fut23.core.driver.FutWebDriver;
import stepanovep.fut23.core.entity.FutPlayer;
import stepanovep.fut23.core.locators.TransferMarketLocators;
import stepanovep.fut23.core.page.FutActiveMenu;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SearchResult {

    private final List<FutPlayer> players;

    private SearchResult(List<FutPlayer> players) {
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

        List<FutPlayer> items = driver.findElements(TransferMarketLocators.SEARCH_RESULT_ITEMS)
                .stream()
                .map(webElem -> new FutPlayer(driver, webElem))
                .collect(Collectors.toList());

        driver.activeMenu = FutActiveMenu.TRANSFER_SEARCH_RESULT;
        return new SearchResult(items);
    }

    @Nonnull
    public List<FutPlayer> getPlayers() {
        return players == null ? Collections.emptyList() : players;
    }


}
