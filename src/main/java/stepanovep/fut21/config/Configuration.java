package stepanovep.fut21.config;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import stepanovep.fut21.core.driver.FutWebDriver;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

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

    @Bean(name = "driver", destroyMethod = "quit")
    public FutWebDriver futWebDriver(ChromeOptions chromeOptions) {
        return new FutWebDriver(chromeOptions);
    }

    @Bean
    public MongoClient mongoClient() {
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        return MongoClients.create(MongoClientSettings.builder()
                .codecRegistry(pojoCodecRegistry)
                .build());
    }
}
