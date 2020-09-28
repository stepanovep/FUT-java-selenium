package stepanovep.fut21.utils;

import org.openqa.selenium.By;

/**
 * Утилитный класс для работы с локатороми
 */
public final class LocatorsUtils {

    private LocatorsUtils() {
    }

    /**
     * Получить локатор для поиска веб элемента по его текстовому значению
     */
    public static By byText(String text) {
        return By.xpath(String.format("//*[text()='%s']", text));
    }
}
