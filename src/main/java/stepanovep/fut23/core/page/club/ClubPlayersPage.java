package stepanovep.fut23.core.page.club;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import stepanovep.fut23.core.driver.FutWebDriver;
import stepanovep.fut23.core.locators.ClubPageLocators;
import stepanovep.fut23.core.locators.MainPageLocators;
import stepanovep.fut23.core.page.FutActiveMenu;
import stepanovep.fut23.core.page.transfers.SearchResult;

@Component
@RequiredArgsConstructor
public class ClubPlayersPage {

    private final FutWebDriver driver;
    private final ClubPlayersSearchService clubPlayersSearchService;

    public SearchResult searchPlayers(ClubPlayersSearchOptions searchOptions) {
        navigateToClubPlayers();
        driver.clickElement(ClubPageLocators.GO_TO_SEARCH_OPTIONS);
        clubPlayersSearchService.applySearchOptions(searchOptions);
        driver.clickElement(ClubPageLocators.CLICK_SEARCH_BUTTON, 1000);
        return SearchResult.from(driver);
    }

    private void navigateToClubPlayers() {
        if (driver.activeMenu != FutActiveMenu.CLUB_PLAYERS) {
            driver.clickElement(MainPageLocators.GO_TO_CLUB);
            driver.clickElement(MainPageLocators.GO_TO_CLUB_PLAYERS);
            driver.activeMenu = FutActiveMenu.CLUB_PLAYERS;
        }
        driver.sleep(500);
    }
}
