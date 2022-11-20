package stepanovep.fut23;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.retry.annotation.EnableRetry;
import stepanovep.fut23.bot.service.StatisticService;
import stepanovep.fut23.config.AppConfiguration;
import stepanovep.fut23.futbin.FutbinService;
import stepanovep.fut23.kafka.KafkaProducer;

@SpringBootApplication
@Import(AppConfiguration.class)
@EnableRetry
@Slf4j
public class FutSeleniumApplication implements CommandLineRunner {

    @Autowired
    private StatisticService statisticService;
    @Autowired
    private FutbinService futbinService;
    @Autowired
    private KafkaProducer kafkaProducer;

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
