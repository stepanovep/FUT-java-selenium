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
import stepanovep.fut21.bot.FutBot;
import stepanovep.fut21.futbin.FutbinService;
import stepanovep.fut21.telegrambot.TelegramBot;

@SpringBootApplication
@Import(AppConfiguration.class)
@EnableRetry
public class FutSeleniumApplication implements CommandLineRunner {

    @Autowired
    private FutBot futBot;

    @Autowired
    private FutbinService futbinService;

    @Autowired
    private TelegramBot telegramBot;

    private static final Logger log = LoggerFactory.getLogger(FutSeleniumApplication.class);

    public static void main(String[] args) {
        log.info("STARTING THE APPLICATION");
        SpringApplication.run(FutSeleniumApplication.class);
        log.info("APPLICATION FINISHED");
    }

    @Override
    public void run(String... args) {
        log.info("EXECUTING : command line runner");
        try {
            futbinService.updatePrices();
            futBot.login();
            futBot.start();
            telegramBot.notifyAboutSuccessFinish(futBot.screenshot());

        } catch (Exception exc) {
            telegramBot.notifyAboutException(futBot.screenshot());
            log.error("Error: ", exc);
        }
    }
}
