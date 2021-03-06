package stepanovep.fut21.appconfig;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import stepanovep.fut21.core.driver.FutWebDriver;
import stepanovep.fut21.mongo.ActiveAuction;
import stepanovep.fut21.mongo.Player;
import stepanovep.fut21.mongo.WonAuction;
import stepanovep.fut21.telegrambot.TelegramBot;
import stepanovep.fut21.telegrambot.TelegramBotCommandHandler;
import stepanovep.fut21.telegrambot.TelegramBotProperties;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Configuration
@EnableScheduling
public class AppConfiguration {

    @Autowired
    private AppProperties appProperties;

    @Bean
    public ChromeOptions chromeOptions() {
        System.setProperty("webdriver.chrome.driver", appProperties.getChromeDriverExecutablePath());
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments(
                "user-data-dir=" + appProperties.getChromeUserDataDir(),
                "--no-sandbox",
                "--start-maximized");

        if (appProperties.isHeadless()) {
            chromeOptions.addArguments("--headless");
        }

        return chromeOptions;
    }

    @Bean(name = "driver")
    public FutWebDriver futWebDriver(ChromeOptions chromeOptions) {
        return new FutWebDriver(chromeOptions, appProperties.getPlatform());
    }

    @Bean
    public MongoClient mongoClient() {
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        return MongoClients.create(MongoClientSettings.builder()
                .codecRegistry(pojoCodecRegistry)
                .build());
    }

    @Bean
    public MongoCollection<Player> playersCollection() {
        MongoDatabase database = mongoClient().getDatabase("fut");
        MongoCollection<Player> players = database.getCollection("players", Player.class);
        players.createIndex(Indexes.ascending("resourceId"), new IndexOptions().unique(true));
        players.createIndex(Indexes.ascending("futbinId"), new IndexOptions().unique(true));
        return players;
    }

    @Bean
    public MongoCollection<ActiveAuction> activeAuctionsCollection() {
        MongoDatabase database = mongoClient().getDatabase("fut");
        MongoCollection<ActiveAuction> activeAuctions = database.getCollection("activeAuctions", ActiveAuction.class);
        activeAuctions.createIndex(Indexes.ascending("tradeId"), new IndexOptions().unique(true));
        return activeAuctions;
    }

    @Bean
    public MongoCollection<WonAuction> wonAuctionsCollection() {
        MongoDatabase database = mongoClient().getDatabase("fut");
        MongoCollection<WonAuction> auctionsHistory = database.getCollection("wonAuctions", WonAuction.class);
        auctionsHistory.createIndex(Indexes.ascending("tradeId"), new IndexOptions().unique(true));
        auctionsHistory.createIndex(Indexes.ascending("boughtDt"));
        return auctionsHistory;
    }

    @Bean
    public TelegramBot telegramBot(TelegramBotProperties properties,
                                   TelegramBotCommandHandler commandHandler) throws TelegramApiRequestException {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        TelegramBot telegramBot = new TelegramBot(properties, commandHandler);
        telegramBotsApi.registerBot(telegramBot);

        return telegramBot;
    }

    @Bean
    public ScheduledExecutorService futbotExecutor() {
        return Executors.newSingleThreadScheduledExecutor();
    }

    @Bean
    public ExecutorService futbinExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    @Bean
    public ExecutorService telegramNotifierExecutor() {
        return Executors.newSingleThreadExecutor();
    }
}
