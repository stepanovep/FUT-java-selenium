package stepanovep.fut21.core.locators;

import org.openqa.selenium.By;
import stepanovep.fut21.utils.LocatorsUtils;

public class FutElementLocators {

    public static By COMPARE_PRICE_BUTTON = LocatorsUtils.byText("Compare Price");
    public static By COMPARE_PRICE_ELEMENTS = By.cssSelector(".ui-layout-right ul.paginated > li");
    public static By COMPARE_PRICE_ELEMENT_BUY_NOW_VALUE = By.cssSelector("div.auction > div:nth-child(3) > span.value");
    public static By COMPARE_PRICE_NEXT_BUTTON = By.cssSelector(".ui-layout-right button.next");
}

