package stepanovep.fut21;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.retry.annotation.EnableRetry;
import stepanovep.fut21.appconfig.AppConfiguration;
import stepanovep.fut21.bot.service.LoginService;
import stepanovep.fut21.bot.service.StatisticService;
import stepanovep.fut21.bot.service.sniping.SnipingService;
import stepanovep.fut21.core.page.transfers.filter.League;
import stepanovep.fut21.core.page.transfers.filter.Nationality;
import stepanovep.fut21.core.page.transfers.filter.Position;
import stepanovep.fut21.core.page.transfers.filter.Quality;
import stepanovep.fut21.core.page.transfers.filter.Rarity;
import stepanovep.fut21.core.page.transfers.filter.TransferMarketSearchFilter;

@SpringBootApplication
@Import(AppConfiguration.class)
@EnableRetry
public class FutSeleniumApplication implements CommandLineRunner {

    @Autowired
    private StatisticService statisticService;

    @Autowired
    private SnipingService snipingService;

    @Autowired
    private LoginService loginService;

    private static final Logger log = LoggerFactory.getLogger(FutSeleniumApplication.class);

    public static void main(String[] args) {
        log.info("STARTING THE APPLICATION");
        SpringApplication.run(FutSeleniumApplication.class);
        log.info("APPLICATION FINISHED");
    }

    @Override
    public void run(String... args) {
        log.info("EXECUTING : command line runner");
        statisticService.displayOverallBuys();

        loginService.login();
        snipingService.snipe(TransferMarketSearchFilter.builder()
//                .withQuality(Quality.GOLD)
//                .withPosition(Position.DEFENDER)
//                .withNationality(Nationality.ITALY)
                .withLeague(League.LIGA_NOS)
                .withRarity(Rarity.UCL_NON_RARE)
                .withBuyNowMax(3300)
                .build());
    }
}
