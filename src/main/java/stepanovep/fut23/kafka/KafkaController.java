package stepanovep.fut23.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import stepanovep.fut23.bot.FutBot;

@Service
@RequiredArgsConstructor
public class KafkaController {

    private final FutBot futBot;

    @KafkaListener(
            topics = "telegram_bot_commands",
            groupId = "fut_selenium")
    public void telegramBotCommandListener(String command) {
        switch (command) {
            case "/login" -> futBot.login();
            case "/relistall" -> futBot.relistAll();
            case "/screenshot" -> futBot.screenshot();
            case "/massbid" -> futBot.massBid();
            case "/checkbids" -> futBot.checkBids();
            case "/schedulerelistall" -> futBot.scheduleRelistAll();
            case "/schedulemassbid" -> futBot.scheduleMassBid();
            case "/showdailystatistic" -> futBot.showDailyStatistic();
            case "/clubstock" -> futBot.clubStock();
        }
    }
}
