package stepanovep.fut21.telegrambot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TelegramBot extends TelegramLongPollingBot {

    private final TelegramBotProperties properties;
    private final TelegramBotCommandHandler commandHandler;

    public TelegramBot(TelegramBotProperties properties, TelegramBotCommandHandler commandHandler) {
        this.properties = properties;
        this.commandHandler = commandHandler;
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
            String message = update.getMessage().getText();
            if (message.startsWith("/")) {
                commandHandler.handleCommand(message);
            }
        }
    }
}
