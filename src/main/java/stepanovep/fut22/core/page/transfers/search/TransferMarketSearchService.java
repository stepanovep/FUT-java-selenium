package stepanovep.fut22.core.page.transfers.search;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import stepanovep.fut22.core.driver.FutWebDriver;
import stepanovep.fut22.core.locators.TransferMarketLocators;
import stepanovep.fut22.utils.Enums;
import stepanovep.fut22.utils.LocatorsUtils;

import static stepanovep.fut22.core.locators.TransferMarketLocators.MAX_BID_PRICE_FORM_INPUT;
import static stepanovep.fut22.core.locators.TransferMarketLocators.MAX_BIN_PRICE_FORM_INPUT;
import static stepanovep.fut22.core.locators.TransferMarketLocators.MIN_BID_PRICE_FORM_INPUT;
import static stepanovep.fut22.core.locators.TransferMarketLocators.MIN_BIN_PRICE_FORM_INPUT;
import static stepanovep.fut22.core.locators.TransferMarketLocators.PLAYERS_DROPDOWN_GET_FIRST;
import static stepanovep.fut22.core.locators.TransferMarketLocators.SEARCH_CHEM_STYLE_LIST;
import static stepanovep.fut22.core.locators.TransferMarketLocators.SEARCH_LEAGUE_LIST;
import static stepanovep.fut22.core.locators.TransferMarketLocators.SEARCH_NATIONALITY_LIST;
import static stepanovep.fut22.core.locators.TransferMarketLocators.SEARCH_PLAYER_NAME_INPUT;
import static stepanovep.fut22.core.locators.TransferMarketLocators.SEARCH_POSITION_LIST;
import static stepanovep.fut22.core.locators.TransferMarketLocators.SEARCH_QUALITY_LIST;
import static stepanovep.fut22.core.locators.TransferMarketLocators.SEARCH_RARITY_LIST;

@Component
public class TransferMarketSearchService {

    @Autowired
    private FutWebDriver driver;

    public void applySearchOptions(TransferMarketSearchOptions searchOption) {
        searchOption.getName().ifPresent(this::setName);

        searchOption.getQuality().ifPresent(quality -> setDropDownOptions(SEARCH_QUALITY_LIST, quality));
        searchOption.getRarity().ifPresent(rarity -> setDropDownOptions(SEARCH_RARITY_LIST, rarity));
        searchOption.getNationality().ifPresent(nationality -> setDropDownOptions(SEARCH_NATIONALITY_LIST, nationality));
        searchOption.getLeague().ifPresent(league -> setDropDownOptions(SEARCH_LEAGUE_LIST, league));
        searchOption.getPosition().ifPresent(position -> setDropDownOptions(SEARCH_POSITION_LIST, position));
        searchOption.getChemStyle().ifPresent(chemStyle -> setDropDownOptions(SEARCH_CHEM_STYLE_LIST, chemStyle));

        searchOption.getBidMin().ifPresent(price -> setPrice(MIN_BID_PRICE_FORM_INPUT, price));
        searchOption.getBidMax().ifPresent(price -> setPrice(MAX_BID_PRICE_FORM_INPUT, price));
        searchOption.getTargetPrice().ifPresent(targetPrice -> setPrice(MAX_BID_PRICE_FORM_INPUT, targetPrice));
        searchOption.getBuyNowMin().ifPresent(price -> setPrice(MIN_BIN_PRICE_FORM_INPUT, price));
        searchOption.getBuyNowMax().ifPresent(price -> setPrice(MAX_BIN_PRICE_FORM_INPUT, price));
    }

    public void resetAll() {
        driver.clickElement(TransferMarketLocators.RESET_SEARCH_OPTIONS_BUTTON);
    }

    private void setName(String name) {
        WebElement nameInput = driver.findElement(SEARCH_PLAYER_NAME_INPUT);
        driver.sleep(300);
        nameInput.clear();
        nameInput.sendKeys(name);
        driver.sleep(300);

        nameInput.sendKeys(Keys.ENTER);
        driver.sleep(300);
        driver.findElementWithWait(PLAYERS_DROPDOWN_GET_FIRST).click();
        driver.sleep(500, 750);
    }

    private void setDropDownOptions(By dropDownMenuLocator, Enums.StringRepr value) {
        driver.clickElement(dropDownMenuLocator);
        driver.clickElement(LocatorsUtils.byText(value.getCode()));
        driver.sleep(500, 750);
    }

    private void setPrice(By priceInputLocator, Integer price) {
        WebElement priceInput = driver.findElement(priceInputLocator);
        driver.sendKeys(priceInput, String.valueOf(price));
        driver.sleep(500, 750);
    }
}
