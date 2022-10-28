package stepanovep.fut23.utils;

import org.openqa.selenium.By;

public final class LocatorsUtils {

    private LocatorsUtils() {
    }

    /**
     * Construct a locator by text value
     */
    public static By byText(String text) {
        return By.xpath(String.format("//*[text()='%s']", text));
    }
}
