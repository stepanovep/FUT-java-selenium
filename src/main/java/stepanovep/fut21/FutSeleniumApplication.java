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
import stepanovep.fut21.bot.service.StatisticService;

@SpringBootApplication
@Import(AppConfiguration.class)
@EnableRetry
public class FutSeleniumApplication implements CommandLineRunner {

    @Autowired
    private StatisticService statisticService;

    private static final Logger log = LoggerFactory.getLogger(FutSeleniumApplication.class);

    public static void main(String[] args) {
        log.info("STARTING THE APPLICATION");
        SpringApplication.run(FutSeleniumApplication.class);
        log.info("APPLICATION FINISHED");
    }

    @Override
    public void run(String... args) {
        statisticService.displayOverallBuys();
        log.info("EXECUTING : command line runner");
    }
}
