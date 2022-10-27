package stepanovep.fut22.bot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import stepanovep.fut22.core.driver.FutWebDriver;
import stepanovep.fut22.core.locators.MainPageLocators;
import stepanovep.fut22.core.page.FutActiveMenu;

import java.time.Duration;

import static stepanovep.fut22.core.locators.LoginLocators.LOGIN_BUTTON_LOCATOR;
import static stepanovep.fut22.core.locators.MainPageLocators.COINS_ELEM_LOCATOR;


@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

    private static final String FUT_WEB_URL = "https://www.easports.com/fifa/ultimate-team/web-app/";

    private final FutWebDriver driver;

    public void login() {
        log.info("Login to FUT");
        driver.get(FUT_WEB_URL);

        try {
            new WebDriverWait(driver, Duration.ofSeconds(15))
                    .until(ExpectedConditions.elementToBeClickable(By.cssSelector(LOGIN_BUTTON_LOCATOR)))
                    .click();

        } catch (TimeoutException exc) {
            log.info("Login button disabled. Waiting for FUT menu to load itself.");

        } finally {
            WebElement coinsElement = new WebDriverWait(driver, Duration.ofSeconds(30))
                    .until(d -> d.findElement(COINS_ELEM_LOCATOR));
            log.info("Logged in successfully: coins={}", coinsElement.getText());
            driver.sleep(7500);

            closeLiveMessagePopup();
        }

        driver.activeMenu = FutActiveMenu.HOME;
    }

    private void closeLiveMessagePopup() {
        if (driver.isElementPresent(MainPageLocators.LIVE_MESSAGE_POPUP)) {
            WebElement liveMessageCloseButton = driver.findElement(MainPageLocators.LIVE_MESSAGE_CLOSE_BUTTON);
            liveMessageCloseButton.click();
        }
    }
}
