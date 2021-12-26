package stepanovep.fut22.core.page.transfers;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import stepanovep.fut22.core.driver.FutWebDriver;
import stepanovep.fut22.core.entity.FutPlayerElement;
import stepanovep.fut22.core.locators.MainPageLocators;
import stepanovep.fut22.core.locators.TransferTargetsLocators;
import stepanovep.fut22.core.page.FutActiveMenu;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Page Object для страницы управления аукционами
 */
@Component
@Slf4j
public class TransferTargetsPage {

    @Autowired
    private FutWebDriver driver;

    public void navigateToPage() {
//        if (driver.activeMenu != FutActiveMenu.TRANSFER_TARGETS) {
            driver.clickElement(MainPageLocators.GO_TO_TRANSFERS);
            driver.clickElement(MainPageLocators.GO_TO_TRANSFER_TARGETS);
//        }
        driver.activeMenu = FutActiveMenu.TRANSFER_TARGETS;
        driver.sleep(500);
    }

    public List<FutPlayerElement> getActiveBids() {
        WebElement activeBidsSection = driver.findElementWithWait(TransferTargetsLocators.ACTIVE_BIDS_SECTION);
        List<WebElement> activeBids = activeBidsSection.findElements(TransferTargetsLocators.SECTION_ELEMENTS);
        log.info("Active bids elements: count={}", activeBids.size());
        return mapToPlayers(activeBids);
    }

    public List<FutPlayerElement> getWonItems() {
        WebElement wonItemsSection = driver.findElementWithWait(TransferTargetsLocators.WON_BIDS_SECTION);
        List<WebElement> wonItems = wonItemsSection.findElements(TransferTargetsLocators.SECTION_ELEMENTS);
        return mapToPlayers(wonItems);
    }

    public List<FutPlayerElement> getWatchedItems() {
        WebElement watchedSection = driver.findElementWithWait(TransferTargetsLocators.WATCHED_BIDS_SECTION);
        List<WebElement> watchedItems = watchedSection.findElements(TransferTargetsLocators.SECTION_ELEMENTS);
        return mapToPlayers(watchedItems);
    }

    public List<FutPlayerElement> getExpiredItems() {
        WebElement expiredItemsSection = driver.findElementWithWait(TransferTargetsLocators.EXPIRED_BIDS_SECTION);
        List<WebElement> expiredItems = expiredItemsSection.findElements(TransferTargetsLocators.SECTION_ELEMENTS);
        return mapToPlayers(expiredItems);
    }

    public void sendAllToClub() {
        log.info("Sending won items to club");
        new Thread(() -> {
            WebElement button = driver.findElement(TransferTargetsLocators.SEND_WON_ITEMS_TO_CLUB);
            if (button.isDisplayed()) {
                button.click();
            }
        }).start();

        driver.sleep(2000, 3000);
    }

    public void clearAllExpiredItems() {
        log.info("Clean expired items");
        new Thread(() -> {
            WebElement button = driver.findElement(TransferTargetsLocators.CLEAR_EXPIRED_ITEMS_BUTTON);
            if (button.isDisplayed()) {
                button.click();
            }
        }).start();

        driver.sleep(2000, 3000);
    }

    private List<FutPlayerElement> mapToPlayers(List<WebElement> webElements) {
        return webElements.stream()
                .map(webElement -> new FutPlayerElement(driver, webElement))
                .collect(Collectors.toList());
    }
}
