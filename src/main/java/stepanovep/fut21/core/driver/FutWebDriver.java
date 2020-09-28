package stepanovep.fut21.core.driver;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Random;

/**
 * Веб драйвер с расширенными возможностями
 * для удобного управления меню FUT Web-App
 */
public class FutWebDriver extends ChromeDriver {

    private final Random rnd = new Random();

    public FutWebDriver(ChromeOptions chromeOptions) {
        super(chromeOptions);
    }

    /**
     * Получает элемент по локатору с явным ожиданием
     */
    public WebElement findElementWithWait(By locator) {
        return new WebDriverWait(this, Duration.ofSeconds(5))
                .until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Находит и кликает элемент по заданному локатору
     *
     * @param locator локатор элемента для клика
     */
    public void clickElement(By locator) {
        new FluentWait<WebDriver>(this)
                .withTimeout(Duration.ofSeconds(10))
                .ignoreAll(List.of(
                        NoSuchElementException.class,
                        ElementClickInterceptedException.class))
                .until(driver ->  {
                    WebElement webElement = driver.findElement(locator);
                    webElement.click();
                    return webElement;
                });
    }

    /**
     * Заполняет форму ввода элемента заданным текстом
     *
     * @param element веб элемент с формой для ввода
     * @param keys текст для ввода
     */
    public void sendKeys(WebElement element, String keys) {
        sleep(400);
        element.click();
        element.clear();
        sleep(200);
        for (char c: keys.toCharArray()) {
            element.sendKeys(String.valueOf(c));
            sleep(75);
        }
        sleep(400);
    }

    /**
     * Ставит поток на паузу для имитации естественной пользователькой задержки
     */
    public void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException exc) {
            // ignore
        }
    }

    /**
     * Ставит поток на паузу для имитации естественной пользователькой задержки
     */
    public void sleep(int millisecondsFrom, int millisecondsTo) {
        int milliseconds = millisecondsFrom + rnd.nextInt(millisecondsTo-millisecondsFrom);
        sleep(milliseconds);
    }


}
