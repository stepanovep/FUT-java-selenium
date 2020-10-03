package stepanovep.fut21.core.page.transfers;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import stepanovep.fut21.core.driver.FutWebDriver;
import stepanovep.fut21.core.entity.FutElement;
import stepanovep.fut21.core.locators.TransferMarketLocators;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Результат поиска через Трансферный Рынок (ТР)
 */
public class TransferSearchResult {

    private final List<FutElement> players;

    private TransferSearchResult(List<FutElement> players) {
        this.players = players;
    }

    public static TransferSearchResult from(FutWebDriver driver) {
        new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(5))
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

        List<FutElement> items = driver.findElements(TransferMarketLocators.SEARCH_RESULT_ITEMS)
                .stream()
                .map(webElem -> new FutElement(driver, webElem))
                .collect(Collectors.toList());

        return new TransferSearchResult(items);
    }

    @Nonnull
    public List<FutElement> getPlayers() {
        return players == null ? Collections.emptyList() : players;
    }


}
