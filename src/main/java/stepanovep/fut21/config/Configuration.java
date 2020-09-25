package stepanovep.fut21.config;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import stepanovep.fut21.core.FutWebDriver;

public class Configuration {

    @Bean
    public ChromeOptions chromeOptions() {
        System.setProperty("webdriver.chrome.driver", "C:\\Selenium\\chromedriver.exe");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments(
                "user-data-dir=C:\\Selenium\\UserData",
                "--no-sandbox",
                "--start-maximized");

        return chromeOptions;
    }

    @Bean(name = "driver")
    public FutWebDriver futWebDriver(ChromeOptions chromeOptions) {
        return new FutWebDriver(chromeOptions);
    }
}
