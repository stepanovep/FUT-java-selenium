package stepanovep.fut23.utils;

import lombok.NoArgsConstructor;
import org.openqa.selenium.By;

@NoArgsConstructor
public final class LocatorsUtils {

    /**
     * Construct a locator by text value
     */
    public static By byText(String text) {
        return By.xpath(String.format("//*[text()='%s']", text));
    }
}
