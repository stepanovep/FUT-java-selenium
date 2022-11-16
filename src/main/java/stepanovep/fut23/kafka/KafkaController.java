package stepanovep.fut23.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KafkaController {

    private final KafkaProducer kafkaProducer;

    @PostMapping("/publish")
    public void sendMessageToTopic(@RequestParam("message") String message) {
        kafkaProducer.sendMessage(message);
    }
}
