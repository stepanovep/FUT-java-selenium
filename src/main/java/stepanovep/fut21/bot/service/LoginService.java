package stepanovep.fut21.bot.service;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stepanovep.fut21.config.LoginSettings;
import stepanovep.fut21.core.driver.FutWebDriver;

import java.time.Duration;

import static stepanovep.fut21.core.locators.LoginLocators.COINS_ELEM_LOCATOR;
import static stepanovep.fut21.core.locators.MainPageLocators.LOGIN_BUTTON_LOCATOR;

/**
 * Сервис для входа в FUT web-app
 */
@Service
public class LoginService {

    private static final Logger log = LoggerFactory.getLogger(LoginService.class);

    private static final String FUT_WEB_URL = "https://www.easports.com/fifa/ultimate-team/web-app/";

    @Autowired
    private FutWebDriver driver;

    @Autowired
    private LoginSettings credentialsProperties;

    public void login() {
        log.info("Login to FUT");
        driver.get(FUT_WEB_URL);

        try {
            new WebDriverWait(driver, Duration.ofSeconds(15))
                    .until(ExpectedConditions.elementToBeClickable(By.cssSelector(LOGIN_BUTTON_LOCATOR)))
                    .click();

        } catch (TimeoutException exc) {
            log.info("Login button disabled. Waiting for FUT menu to load itself.");

            // TODO: use credentials if page redirects to ea.login.com

        } finally {
            WebElement coinsElement = new WebDriverWait(driver, Duration.ofSeconds(30))
                    .until(d -> d.findElement(By.cssSelector(COINS_ELEM_LOCATOR)));
            log.info("Logged in successfully: coins={}", coinsElement.getText());
            driver.sleep(2000);
        }
    }
}
