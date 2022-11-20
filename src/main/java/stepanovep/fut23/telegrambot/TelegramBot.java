package stepanovep.fut23.telegrambot;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import stepanovep.fut23.kafka.KafkaProducer;

@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private final TelegramBotProperties properties;
    private final KafkaProducer kafkaProducer;

    @Value("${spring.kafka.topic.telegram-bot-commands.name}")
    private String topic;

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
                kafkaProducer.sendMessage(topic, command);
            }
        }
    }
}
