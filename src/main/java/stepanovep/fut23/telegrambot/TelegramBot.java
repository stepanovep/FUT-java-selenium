package stepanovep.fut23.telegrambot;

import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private final TelegramBotProperties properties;
    private final TelegramBotCommandHandler commandHandler;

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
