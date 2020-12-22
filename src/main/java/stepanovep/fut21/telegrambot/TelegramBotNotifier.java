package stepanovep.fut21.telegrambot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;

@Component
public class TelegramBotNotifier {

    private final static Logger log = LoggerFactory.getLogger(TelegramBotNotifier.class);

    @Autowired
    private TelegramBot telegramBot;

    @Autowired
    private TelegramBotProperties properties;

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

    public void sendScreenshot(File screenshot, String caption) {
        SendPhoto sendPhoto = new SendPhoto()
                .setPhoto(screenshot)
                .setChatId(properties.getChatId())
                .setCaption(caption);
        try {
            telegramBot.execute(sendPhoto);
        } catch (TelegramApiException exc) {
            exc.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        log.info(message);
        SendMessage sendMessage = new SendMessage(properties.getChatId(), message);
        try {
            telegramBot.execute(sendMessage);

        } catch (TelegramApiException exc) {
            exc.printStackTrace();
        }
    }
}
