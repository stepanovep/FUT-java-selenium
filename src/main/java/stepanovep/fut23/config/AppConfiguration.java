package stepanovep.fut23.config;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import lombok.SneakyThrows;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import stepanovep.fut23.core.driver.FutWebDriver;
import stepanovep.fut23.mongo.ActiveAuction;
import stepanovep.fut23.mongo.Player;
import stepanovep.fut23.mongo.WonAuction;
import stepanovep.fut23.telegrambot.TelegramBot;
import stepanovep.fut23.telegrambot.TelegramBotCommandHandler;
import stepanovep.fut23.telegrambot.TelegramBotProperties;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Configuration
@EnableAsync
@EnableScheduling
@EnableRetry
@EnableConfigurationProperties({
        WebDriverProperties.class,
        FutbinProperties.class,
        BidderProperties.class
})
public class AppConfiguration {

    @Autowired
    private WebDriverProperties properties;

    @Bean
    public ChromeOptions chromeOptions() {
        System.setProperty("webdriver.chrome.driver", properties.getChromeDriverExecutablePath());
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments(
                "user-data-dir=" + properties.getChromeUserDataDir(),
                "--no-sandbox",
                "--disable-blink-features=AutomationControlled",
                "--remote-allow-origins=*",
                "--start-maximized",
                "--start-fullscreen");
        chromeOptions.setExperimentalOption("excludeSwitches", List.of("enable-automation]"));

        return chromeOptions;
    }

    @Bean(name = "driver")
    public FutWebDriver futWebDriver(ChromeOptions chromeOptions) {
        return new FutWebDriver(chromeOptions, properties.getPlatform());
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

    @SneakyThrows
    @Bean
    public TelegramBot telegramBot(TelegramBotProperties properties,
                                   TelegramBotCommandHandler commandHandler) {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
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
