package stepanovep.fut23.core.page.store;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;
import stepanovep.fut23.core.driver.FutWebDriver;
import stepanovep.fut23.core.entity.FutItem;
import stepanovep.fut23.core.entity.FutItemType;

import java.util.List;

import static stepanovep.fut23.core.entity.FutItemType.PLAYER;
import static stepanovep.fut23.core.entity.FutItemType.STAFF;
import static stepanovep.fut23.core.locators.MainPageLocators.GO_TO_STORE;
import static stepanovep.fut23.core.locators.StorePageLocators.BUY_BRONZE_PACK_BUTTON;
import static stepanovep.fut23.core.locators.StorePageLocators.CLASSIC_PACKS_SECTION;
import static stepanovep.fut23.core.locators.StorePageLocators.FUT_ITEMS;
import static stepanovep.fut23.core.locators.StorePageLocators.GO_TO_PACKS;

@Component
@RequiredArgsConstructor
@Slf4j
public class StorePage {

    private final FutWebDriver driver;

    public void bpm(int count) {
        for (int i = 0; i < count; i++) {
            openBronzePack();
            handleItems();
        }

        log.info("BPM finished");
    }

    private void openBronzePack() {
        log.info("Opening bronze pack");
        driver.clickElement(GO_TO_STORE, 2000);
        driver.clickElement(GO_TO_PACKS, 1000);
        driver.clickElement(CLASSIC_PACKS_SECTION);

        driver.clickElement(BUY_BRONZE_PACK_BUTTON, 500);
        driver.acceptDialogMessage();
        driver.sleep(7500);
    }

    private void handleItems() {
        List<WebElement> items;
        do {
            items = driver.findElements(FUT_ITEMS);
            if (items.isEmpty()) {
                break;
            }
            FutItem item = new FutItem(driver, items.get(0));
            item.focus();

            FutItemType itemType = item.getType();
            switch (itemType) {
                case PLAYER, STAFF -> {
                    List<Integer> prices = item.comparePrice();
                    if (prices.isEmpty()) {
                        item.sendToTransferMarket();
                    } else if (prices.get(0) > 250) {
                        Integer price = prices.get(0);
                        item.listToTransferMarket((int) (price * 0.725), (int) (price * 0.925));
                    } else if (itemType == PLAYER && item.isDuplicate()) {
                        item.sendToTransferMarket();
                    } else if (itemType == STAFF && item.isDuplicate()) {
                        item.quickSell();
                    } else {
                        item.sendToClub();
                    }
                }
                case GIFT -> item.redeem();
                case KIT, VANITY, BADGE, BALL, STADIUM -> item.quickSell();
                case CONSUMABLE -> item.sendToClub();
                default -> throw new RuntimeException("Unknown item type: " + itemType);
            }

            driver.sleep(750, 1000);
        } while (items.size() > 1);

        driver.sleep(2000);
    }
}
