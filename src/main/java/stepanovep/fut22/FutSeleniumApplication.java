package stepanovep.fut22;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.retry.annotation.EnableRetry;
import stepanovep.fut22.config.AppConfiguration;
import stepanovep.fut22.bot.FutBot;
import stepanovep.fut22.bot.service.GemsSeller;
import stepanovep.fut22.bot.service.LoginService;
import stepanovep.fut22.bot.service.StatisticService;
import stepanovep.fut22.bot.service.clubstocking.ClubStocker;
import stepanovep.fut22.bot.service.sniping.SnipingService;
import stepanovep.fut22.futbin.FutbinService;

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
    @Autowired
    private ClubStocker clubStocker;
    @Autowired
    private FutBot futBot;
    @Autowired
    private FutbinService futbinService;
    @Autowired
    private GemsSeller gemsSeller;

    private static final Logger log = LoggerFactory.getLogger(FutSeleniumApplication.class);

    public static void main(String[] args) {
        log.info("STARTING THE APPLICATION");
        SpringApplication.run(FutSeleniumApplication.class);
        log.info("APPLICATION FINISHED");
    }

    @Override
    public void run(String... args) {
        log.info("EXECUTING : command line runner");
        statisticService.displayWeeklyStatistic();
        statisticService.displayOverallBuys();

//        loginService.login();
//        gemsSeller.sellGems();
    }

}
