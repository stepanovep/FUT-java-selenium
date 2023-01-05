package stepanovep.fut23.core.locators;


import lombok.NoArgsConstructor;
import org.openqa.selenium.By;

@NoArgsConstructor
public final class TransferMarketLocators {

    public static By SEARCH_BUTTON = By.cssSelector(".button-container > .btn-standard.call-to-action");
    public static By RESET_SEARCH_OPTIONS_BUTTON = By.cssSelector(".button-container > .btn-standard");
    public static By BACK_TO_SEARCH_FORM_BUTTON = By.cssSelector(".ut-navigation-button-control");

    public static By SEARCH_PLAYER_NAME_INPUT = By.cssSelector("input.ut-text-input-control");
    public static By PLAYERS_DROPDOWN_GET_FIRST = By.cssSelector("button > .btn-text");

    public static By SEARCH_QUALITY_LIST = By.xpath("//*[text()='Quality']");
    public static By SEARCH_POSITION_LIST = By.xpath("//*[text()='Position']");
    public static By SEARCH_CHEM_STYLE_LIST = By.xpath("//*[text()='Chemistry Style']");
    public static By SEARCH_RARITY_LIST = By.xpath("//*[text()='Rarity']");
    public static By SEARCH_NATIONALITY_LIST = By.xpath("//*[text()='Nationality']");
    public static By SEARCH_LEAGUE_LIST = By.xpath("//*[text()='League']");

    public static By MIN_BID_PRICE_FORM_INPUT = By.cssSelector("div.search-prices > div:nth-child(2) > div > input");
    public static By MAX_BID_PRICE_FORM_INPUT = By.cssSelector("div.search-prices > div:nth-child(3) > div > input");
    public static By MIN_BIN_PRICE_FORM_INPUT = By.cssSelector("div.search-prices > div:nth-child(5) > div > input");
    public static By MAX_BIN_PRICE_FORM_INPUT = By.cssSelector("div.search-prices > div:nth-child(6) > div > input");

    public static By MIN_BID_PRICE_DECREASE_BUTTON = By.cssSelector("div.search-prices > div:nth-child(2) button.decrement-value");
    public static By MIN_BID_PRICE_INCREASE_BUTTON = By.cssSelector("div.search-prices > div:nth-child(2) button.increment-value");
    public static By MIN_BIN_PRICE_DECREASE_BUTTON = By.cssSelector("div.search-prices > div:nth-child(5) button.decrement-value");
    public static By MIN_BIN_PRICE_INCREASE_BUTTON = By.cssSelector("div.search-prices > div:nth-child(5) button.increment-value");

    public static By NO_RESULTS_ICON = By.cssSelector(".no-results-icon");
    public static By SEARCH_RESULT_ITEMS = By.cssSelector(".paginated-item-list > ul > li");
}
