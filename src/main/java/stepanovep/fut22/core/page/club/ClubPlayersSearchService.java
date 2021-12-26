package stepanovep.fut22.core.page.club;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stepanovep.fut22.core.driver.FutWebDriver;
import stepanovep.fut22.utils.Enums;
import stepanovep.fut22.utils.LocatorsUtils;

import static stepanovep.fut22.core.locators.TransferMarketLocators.PLAYERS_DROPDOWN_GET_FIRST;
import static stepanovep.fut22.core.locators.TransferMarketLocators.SEARCH_CHEM_STYLE_LIST;
import static stepanovep.fut22.core.locators.TransferMarketLocators.SEARCH_LEAGUE_LIST;
import static stepanovep.fut22.core.locators.TransferMarketLocators.SEARCH_NATIONALITY_LIST;
import static stepanovep.fut22.core.locators.TransferMarketLocators.SEARCH_PLAYER_NAME_INPUT;
import static stepanovep.fut22.core.locators.TransferMarketLocators.SEARCH_POSITION_LIST;
import static stepanovep.fut22.core.locators.TransferMarketLocators.SEARCH_QUALITY_LIST;
import static stepanovep.fut22.core.locators.TransferMarketLocators.SEARCH_RARITY_LIST;

@Service
public class ClubPlayersSearchService {

    @Autowired
    private FutWebDriver driver;

    public void applySearchOptions(ClubPlayersSearchOptions searchOption) {
        searchOption.getName().ifPresent(this::setName);

        searchOption.getQuality().ifPresent(quality -> setDropDownOptions(SEARCH_QUALITY_LIST, quality));
        searchOption.getRarity().ifPresent(rarity -> setDropDownOptions(SEARCH_RARITY_LIST, rarity));
        searchOption.getNationality().ifPresent(nationality -> setDropDownOptions(SEARCH_NATIONALITY_LIST, nationality));
        searchOption.getLeague().ifPresent(league -> setDropDownOptions(SEARCH_LEAGUE_LIST, league));
        searchOption.getPosition().ifPresent(position -> setDropDownOptions(SEARCH_POSITION_LIST, position));
        searchOption.getChemStyle().ifPresent(chemStyle -> setDropDownOptions(SEARCH_CHEM_STYLE_LIST, chemStyle));
        driver.sleep(500);
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
}
