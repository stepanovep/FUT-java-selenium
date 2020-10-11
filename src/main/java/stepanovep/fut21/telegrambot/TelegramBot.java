package stepanovep.fut21.telegrambot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;

public class TelegramBot extends TelegramLongPollingBot {

    private final TelegramBotProperties properties;

    public TelegramBot(TelegramBotProperties properties) {
        this.properties = properties;
    }

    @Override
    public String getBotUsername() {
        return properties.getUserName();
    }

    @Override
    public String getBotToken() {
        return properties.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.getMessage() != null && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            try {
                execute(new SendMessage(chatId, "Hello: " + update.getMessage().getText()));

            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public void notifyAboutBoughtPlayer(File screenshot) {
        sendScreenshot(screenshot, "Player bought", true);
    }

    public void notifyAboutSuccessFinish(File screenshot) {
        sendScreenshot(screenshot, "Application finished.", false);
    }

    public void notifyAboutException(File screenshot) {
            sendScreenshot(screenshot, "Application failed...", false);
    }

    private void sendMessage(String message, boolean silent) {
        SendMessage sendMessage = new SendMessage(properties.getChatId(), message);
        if (silent)
            sendMessage.disableNotification();
        else
            sendMessage.enableNotification();

        try {
            execute(sendMessage);

        } catch (TelegramApiException exc) {
            exc.printStackTrace();
        }
    }

    private void sendScreenshot(File screenshot, String caption, boolean silent) {
        SendPhoto sendPhoto = new SendPhoto()
                .setPhoto(screenshot)
                .setChatId(properties.getChatId())
                .setCaption(caption);
        if (silent)
            sendPhoto.disableNotification();
        else
            sendPhoto.enableNotification();

        try {
            execute(sendPhoto);

        } catch (TelegramApiException exc) {
            exc.printStackTrace();
        }
    }
}
