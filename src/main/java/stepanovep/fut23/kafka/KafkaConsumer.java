package stepanovep.fut23.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    @KafkaListener(topics = "telegram_bot_commands", groupId = "fut_selenium")
    public void readMessage(String message) {
        System.out.println("###" + message);
    }
}
