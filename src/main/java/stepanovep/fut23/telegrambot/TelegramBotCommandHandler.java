package stepanovep.fut23.telegrambot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TelegramBotCommandHandler {

    @Autowired
    private TelegramFutBotService telegramFutBotService;

    public void handleCommand(String command) {
        switch (command) {
            case "/login" -> telegramFutBotService.login();
            case "/relistall" -> telegramFutBotService.relistAll();
            case "/screenshot" -> telegramFutBotService.screenshot();
            case "/massbid" -> telegramFutBotService.massBid();
            case "/checkbids" -> telegramFutBotService.checkBids();
            case "/schedulerelistall" -> telegramFutBotService.scheduleRelistAll();
            case "/schedulemassbid" -> telegramFutBotService.scheduleMassBid();
            case "/showdailystatistic" -> telegramFutBotService.showDailyStatistic();
            case "/clubstock" -> telegramFutBotService.clubStock();
            case "/bpm" -> telegramFutBotService.bpm();
        }
    }
}
