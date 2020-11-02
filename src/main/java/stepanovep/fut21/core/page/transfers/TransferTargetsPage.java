package stepanovep.fut21.core.page.transfers;

import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import stepanovep.fut21.core.driver.FutWebDriver;
import stepanovep.fut21.core.entity.FutPlayerElement;
import stepanovep.fut21.core.locators.MainPageLocators;
import stepanovep.fut21.core.locators.TransferTargetsLocators;
import stepanovep.fut21.core.page.FutActiveMenu;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Page Object для страницы управления аукционами
 */
@Component
public class TransferTargetsPage {

    @Autowired
    private FutWebDriver driver;

    public void navigateToPage() {
        if (driver.activeMenu != FutActiveMenu.TRANSFER_TARGETS) {
            driver.clickElement(MainPageLocators.GO_TO_TRANSFERS);
            driver.clickElement(MainPageLocators.GO_TO_TRANSFER_TARGETS);
        }
        driver.activeMenu = FutActiveMenu.TRANSFER_TARGETS;
        driver.sleep(500);
    }

    /**
     * Получить список активных сделок
     */
    public List<FutPlayerElement> getActiveBids() {
        WebElement activeBidsSection = driver.findElementWithWait(TransferTargetsLocators.ACTIVE_BIDS_SECTION);
        List<WebElement> activeBids = activeBidsSection.findElements(TransferTargetsLocators.SECTION_ELEMENTS);
        return mapToPlayers(activeBids);
    }

    /**
     * Получить список выигранных сделок
     */
    public List<FutPlayerElement> getWonItems() {
        WebElement wonItemsSection = driver.findElementWithWait(TransferTargetsLocators.WON_BIDS_SECTION);
        List<WebElement> wonItems = wonItemsSection.findElements(TransferTargetsLocators.SECTION_ELEMENTS);
        return mapToPlayers(wonItems);
    }

    /**
     * Получить список наблюдения
     */
    public List<FutPlayerElement> getWatchedItems() {
        WebElement watchedSection = driver.findElementWithWait(TransferTargetsLocators.WATCHED_BIDS_SECTION);
        List<WebElement> watchedItems = watchedSection.findElements(TransferTargetsLocators.SECTION_ELEMENTS);
        return mapToPlayers(watchedItems);
    }

    /**
     * Получить список просроченных сделок (проигранные или просто наблюдаемые)
     */
    public List<FutPlayerElement> getExpiredItems() {
        WebElement activeBidsSection = driver.findElementWithWait(TransferTargetsLocators.EXPIRED_BIDS_SECTION);
        List<WebElement> expiredItems = activeBidsSection.findElements(TransferTargetsLocators.SECTION_ELEMENTS);
        return mapToPlayers(expiredItems);
    }

    private List<FutPlayerElement> mapToPlayers(List<WebElement> webElements) {
        return webElements.stream()
                .map(webElement -> new FutPlayerElement(driver, webElement))
                .collect(Collectors.toList());
    }
}
