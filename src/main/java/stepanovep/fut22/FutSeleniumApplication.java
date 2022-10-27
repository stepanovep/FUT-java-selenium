package stepanovep.fut22;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.retry.annotation.EnableRetry;
import stepanovep.fut22.bot.service.StatisticService;
import stepanovep.fut22.config.AppConfiguration;

@SpringBootApplication
@Import(AppConfiguration.class)
@EnableRetry
@Slf4j
public class FutSeleniumApplication implements CommandLineRunner {

    @Autowired
    private StatisticService statisticService;

    public static void main(String[] args) {
        log.info("STARTING THE APPLICATION");
        SpringApplication.run(FutSeleniumApplication.class);
        log.info("APPLICATION FINISHED");
    }

    @Override
    public void run(String... args) {
        log.info("EXECUTING : command line runner");
        statisticService.displayOverallBuys();
        statisticService.displayWeeklyStatistic();
    }

}
