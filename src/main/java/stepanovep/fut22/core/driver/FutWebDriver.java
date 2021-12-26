package stepanovep.fut22.core.driver;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import stepanovep.fut22.core.Platform;
import stepanovep.fut22.core.page.FutActiveMenu;

import java.io.File;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Веб драйвер с расширенными возможностями
 * для удобного управления меню FUT Web-App
 */
public class FutWebDriver extends ChromeDriver {

    public FutActiveMenu activeMenu = FutActiveMenu.HOME;

    private volatile boolean interrupted = false;
    private final Random rnd = new Random();
    private final Platform platform;

    public FutWebDriver(ChromeOptions chromeOptions, Platform platform) {
        super(chromeOptions);
        this.platform = platform;
    }

    public Platform getPlatform() {
        return platform;
    }

    public WebElement findElementWithWait(By locator) {
        return new WebDriverWait(this, Duration.ofSeconds(5))
                .until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public List<WebElement> findElementsWithWait(By locator) {
        try {
            return new FluentWait<WebDriver>(this)
                    .withTimeout(Duration.ofSeconds(5))
                    .ignoreAll(List.of(
                            NoSuchElementException.class))
                    .until(driver -> {
                        driver.findElement(locator);
                        return driver.findElements(locator);
                    });
        } catch (Exception exc) {
            return Collections.emptyList();
        }
    }

    public boolean isElementPresent(By locator) {
        return !this.findElements(locator).isEmpty();
    }

    public void clickElement(By locator) {
        this.sleep(150, 250);
        new FluentWait<WebDriver>(this)
                .withTimeout(Duration.ofSeconds(5))
                .ignoreAll(List.of(
                        NoSuchElementException.class,
                        ElementClickInterceptedException.class))
                .until(driver ->  {
                    WebElement webElement = driver.findElement(locator);
                    webElement.click();
                    return true;
                });
    }

    public void clickElement(By locator, int sleepAfter) {
        clickElement(locator);
        sleep(sleepAfter);
    }

    public void clickElement(WebElement webElement) {
        this.sleep(150, 250);
        new FluentWait<WebDriver>(this)
                .withTimeout(Duration.ofSeconds(5))
                .ignoreAll(List.of(
                        NoSuchElementException.class,
                        ElementClickInterceptedException.class))
                .until(driver ->  {
                    webElement.click();
                    return true;
                });
    }

    public Optional<WebElement> getDialog() {
        List<WebElement> binaryDialogs = this.findElements(By.cssSelector(".view-modal-container > .ea-dialog-view"));
        List<WebElement> unaryDialogs = this.findElements(By.cssSelector(".Dialog.ui-dialog-type-alert"));

        return Stream.concat(binaryDialogs.stream(), unaryDialogs.stream())
                .collect(Collectors.toList())
                .stream()
                .findFirst();
    }

    /**
     * Нажимает кнопку "ОК" или "YES" в окошке диалога
     *
     * Диалог может быть вопросительным (с кнопками "yes" или "cancel") или информативным (с одной кнопкой "ok")
     */
    public void acceptDialogMessage() {
        this.sleep(50, 100);
        Optional<WebElement> dialogOpt = getDialog();
        if (dialogOpt.isEmpty()) {
            return;
        }
        WebElement dialog = dialogOpt.get();
        List<WebElement> dialogButtons = dialog.findElements(By.cssSelector("button"));
        for (WebElement button: dialogButtons) {
            String buttonText = button.getText().toLowerCase();
            if (buttonText.equals("ok") || buttonText.equals("yes")) {
                button.click();
                return;
            }
        }

        throw new IllegalStateException("Submit button hasn't been clicked");
    }

    /**
     * Нажимает кнопку "CANCEL" в окошке диалога
     *
     * Диалог ожидается быть вопросительным
     */
    public void declineDialogMessage() {
        this.sleep(50, 100);
        Optional<WebElement> dialogOpt = getDialog();
        if (dialogOpt.isEmpty()) {
            return;
        }
        WebElement dialog = dialogOpt.get();
        List<WebElement> dialogButtons = dialog.findElements(By.cssSelector("button"));
        for (WebElement button: dialogButtons) {
            String buttonText = button.getText().toLowerCase();
            if (buttonText.equals("cancel") || buttonText.equals("no")) {
                button.click();
                return;
            }
        }

        throw new IllegalStateException("Decline button hasn't been clicked");
    }

    public void sendKeys(WebElement element, String keys) {
        sleep(400);
        element.clear();
        element.click();
        sleep(200);
        for (char c: keys.toCharArray()) {
            element.sendKeys(String.valueOf(c));
            sleep(75);
        }
        sleep(400);
    }

    public boolean isInterrupted() {
        return interrupted;
    }

    public void wakeup() {
        this.interrupted = false;
    }

    public void interrupt() {
        this.interrupted = true;
    }

    public void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException exc) {
            System.out.println("Interrupted");
            interrupted = true;
        }
    }

    public void sleep(int millisecondsFrom, int millisecondsTo) {
        int milliseconds = millisecondsFrom + rnd.nextInt(millisecondsTo-millisecondsFrom);
        sleep(milliseconds);
    }

    public File screenshot() {
        return this.getScreenshotAs(OutputType.FILE);
    }

}
