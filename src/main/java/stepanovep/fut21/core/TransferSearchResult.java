package stepanovep.fut21.core;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import stepanovep.fut21.core.locators.TransferMarketLocators;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.Collections;
import java.util.List;

public class TransferSearchResult {

    private final List<WebElement> players;

    private TransferSearchResult(List<WebElement> players) {
        this.players = players;
    }

    public static TransferSearchResult from(FutWebDriver driver) {
        new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(2))
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

        List<WebElement> elements = driver.findElements(TransferMarketLocators.SEARCH_RESULT_ITEMS);
        return new TransferSearchResult(elements);
    }

    @Nonnull
    public List<WebElement> getPlayers() {
        return players == null ? Collections.emptyList() : players;
    }


}
