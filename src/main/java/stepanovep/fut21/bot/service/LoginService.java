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
import stepanovep.fut21.core.driver.FutWebDriver;
import stepanovep.fut21.core.page.FutActiveMenu;

import java.time.Duration;

import static stepanovep.fut21.core.locators.LoginLocators.LOGIN_BUTTON_LOCATOR;
import static stepanovep.fut21.core.locators.MainPageLocators.COINS_ELEM_LOCATOR;


/**
 * Сервис для входа в FUT web-app
 */
@Service
public class LoginService {

    private static final Logger log = LoggerFactory.getLogger(LoginService.class);

    private static final String FUT_WEB_URL = "https://www.easports.com/fifa/ultimate-team/web-app/";

    @Autowired
    private FutWebDriver driver;

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
            driver.sleep(10000);
        }

        driver.activeMenu = FutActiveMenu.HOME;
    }
}
