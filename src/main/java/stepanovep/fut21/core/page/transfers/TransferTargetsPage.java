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

    public List<FutPlayerElement> getActiveBids() {
        WebElement activeBidsSection = driver.findElementWithWait(TransferTargetsLocators.ACTIVE_BIDS_SECTION);
        List<WebElement> elements = activeBidsSection.findElements(TransferTargetsLocators.SECTION_ELEMENTS);

        return elements.stream()
                .map(activeBid -> new FutPlayerElement(driver, activeBid))
                .collect(Collectors.toList());
    }

    public List<FutPlayerElement> getWonItems() {
        WebElement wonItemsSection = driver.findElementWithWait(TransferTargetsLocators.WON_BIDS_SECTION);
        List<WebElement> elements = wonItemsSection.findElements(TransferTargetsLocators.SECTION_ELEMENTS);

        return elements.stream()
                .map(wonItem -> new FutPlayerElement(driver, wonItem))
                .collect(Collectors.toList());
    }

    public void unwatchOutbidPlayer(FutPlayerElement player) {
        WebElement activeBidsSection = driver.findElementWithWait(TransferTargetsLocators.EXPIRED_BIDS_SECTION);
        List<WebElement> expiredItems = activeBidsSection.findElements(TransferTargetsLocators.SECTION_ELEMENTS);
        if (expiredItems.size() > 0) {
            player.toggleWatch();
        }
    }

    public void unwatchExpiredPlayer() {
        // TODO
    }


    public void removeOneExpiredItem() {
        removeOneExpiredItem(0);
    }

    /**
     * TODO expired items tracker
     */
    public void removeOneExpiredItem(int lowerBound) {
        driver.sleep(1000);
        WebElement activeBidsSection = driver.findElementWithWait(TransferTargetsLocators.EXPIRED_BIDS_SECTION);
        List<WebElement> expiredItems = activeBidsSection.findElements(TransferTargetsLocators.SECTION_ELEMENTS);
        if (expiredItems.size() > lowerBound) {
            FutPlayerElement expiredItem = new FutPlayerElement(driver, expiredItems.get(0));
            expiredItem.focus();
            expiredItem.toggleWatch();
        }
        driver.sleep(1000, 2000);
    }

}
