package stepanovep.fut23.telegrambot;

import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
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
        Message message = update.getMessage();
        if (properties.getChatId() == null) {
            properties.setChatId(message.getChatId());
        }
        if (message != null && message.hasText()) {
            String command = message.getText();
            if (command.startsWith("/")) {
                commandHandler.handleCommand(command);
            }
        }
    }
}
