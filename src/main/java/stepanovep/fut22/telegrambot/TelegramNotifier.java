package stepanovep.fut22.telegrambot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.concurrent.ExecutorService;

@Component
public class TelegramNotifier {

    private final static Logger log = LoggerFactory.getLogger(TelegramNotifier.class);

    @Autowired
    private TelegramBot telegramBot;

    @Autowired
    private TelegramBotProperties properties;

    @Autowired
    private ExecutorService telegramNotifierExecutor;

    public void notifyAboutBoughtPlayer(File screenshot, String message) {
        sendScreenshot(screenshot, message);
    }

    public void notifyAboutSuccessFinish(File screenshot) {
        sendScreenshot(screenshot, "Application finished.");
    }

    public void notifyAboutException(File screenshot) {
        sendScreenshot(screenshot, "Application failed...");
    }

    public void sendScreenshot(File screenshot) {
        sendScreenshot(screenshot, null);
    }

    /**
     * Async send screenshot via telegram
     *
     * @param screenshot screenshot file
     * @param caption caption message
     */
    public void sendScreenshot(File screenshot, String caption) {
        telegramNotifierExecutor.submit(() -> {
            SendPhoto sendPhoto = new SendPhoto()
                    .setPhoto(screenshot)
                    .setChatId(properties.getChatId())
                    .setCaption(caption);
            try {
                telegramBot.execute(sendPhoto);
            } catch (TelegramApiException exc) {
                log.error("Telegram send screenshot failed", exc);
            }
        });
    }

    /**
     * Async send message via telegram
     *
     * @param message message
     */
    public void sendMessage(String message) {
        telegramNotifierExecutor.submit(() -> {
            log.info(message);
            SendMessage sendMessage = new SendMessage(properties.getChatId(), message);
            try {
                telegramBot.execute(sendMessage);

            } catch (TelegramApiException exc) {
                log.error("Telegram send message failed", exc);
            }
        });

    }
}
