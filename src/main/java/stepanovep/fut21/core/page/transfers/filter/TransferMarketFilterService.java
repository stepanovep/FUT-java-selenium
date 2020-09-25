package stepanovep.fut21.core.page.transfers.filter;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import stepanovep.fut21.core.FutWebDriver;
import stepanovep.fut21.utils.Enums;
import stepanovep.fut21.utils.LocatorsUtils;

import static stepanovep.fut21.core.locators.TransferMarketLocators.MAX_BID_PRICE_FORM_INPUT;
import static stepanovep.fut21.core.locators.TransferMarketLocators.MAX_BIN_PRICE_FORM_INPUT;
import static stepanovep.fut21.core.locators.TransferMarketLocators.MIN_BID_PRICE_FORM_INPUT;
import static stepanovep.fut21.core.locators.TransferMarketLocators.MIN_BIN_PRICE_FORM_INPUT;
import static stepanovep.fut21.core.locators.TransferMarketLocators.PLAYERS_DROPDOWN_GET_FIRST;
import static stepanovep.fut21.core.locators.TransferMarketLocators.SEARCH_CHEM_STYLE_LIST;
import static stepanovep.fut21.core.locators.TransferMarketLocators.SEARCH_PLAYER_NAME_INPUT;
import static stepanovep.fut21.core.locators.TransferMarketLocators.SEARCH_POSITION_LIST;
import static stepanovep.fut21.core.locators.TransferMarketLocators.SEARCH_QUALITY_LIST;

@Component
public class TransferMarketFilterService {

    @Autowired
    private FutWebDriver driver;

    public void applyFilter(TransferMarketFilter filter) {
        filter.getName().ifPresent(this::setNameFilter);

        filter.getQuality().ifPresent(quality -> setDropDownFilter(SEARCH_QUALITY_LIST, quality));
        filter.getPosition().ifPresent(position -> setDropDownFilter(SEARCH_POSITION_LIST, position));
        filter.getChemStyle().ifPresent(chemStyle -> setDropDownFilter(SEARCH_CHEM_STYLE_LIST, chemStyle));

        filter.getBidMin().ifPresent(price -> setPriceFilter(MIN_BID_PRICE_FORM_INPUT, price));
        filter.getBidMax().ifPresent(price -> setPriceFilter(MAX_BID_PRICE_FORM_INPUT, price));
        filter.getBuyNowMin().ifPresent(price -> setPriceFilter(MIN_BIN_PRICE_FORM_INPUT, price));
        filter.getBuyNowMax().ifPresent(price -> setPriceFilter(MAX_BIN_PRICE_FORM_INPUT, price));
    }

    private void setNameFilter(String name) {
        WebElement nameInput = driver.findElement(SEARCH_PLAYER_NAME_INPUT);
        driver.sendKeys(nameInput, name);

        nameInput.sendKeys(Keys.ENTER);
        driver.findElementWithWait(PLAYERS_DROPDOWN_GET_FIRST).click();
    }

    private void setDropDownFilter(By dropDownMenuLocator, Enums.StringRepr filter) {
        driver.clickElement(dropDownMenuLocator);
        driver.clickElement(LocatorsUtils.byText(filter.getCode()));
        driver.sleep(250);
    }

    private void setPriceFilter(By priceInputLocator, Integer price) {
        WebElement priceInput = driver.findElement(priceInputLocator);
        driver.sendKeys(priceInput, String.valueOf(price));
        driver.sleep(250);
    }
}
