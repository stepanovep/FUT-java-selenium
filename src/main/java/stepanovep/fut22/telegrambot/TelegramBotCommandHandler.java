package stepanovep.fut22.telegrambot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TelegramBotCommandHandler {

    @Autowired
    private TelegramFutBotService telegramFutBotService;

    public void handleCommand(String command) { //todo заменить на enum TelegramBotCommand
        switch (command) {
            case "/stop":
                telegramFutBotService.stop();
                break;

            case "/login":
                telegramFutBotService.login();
                break;

            case "/relistall":
                telegramFutBotService.relistAll();
                break;

            case "/screenshot":
                telegramFutBotService.screenshot();
                break;

            case "/massbid":
                telegramFutBotService.massBid();
                break;

            case "/checkbids":
                telegramFutBotService.checkBids();
                break;

            case "/schedulerelistall":
                telegramFutBotService.scheduleRelistAll();
                break;

            case "/schedulemassbid":
                telegramFutBotService.scheduleMassBid();
                break;

            case "/showdailystatistic":
                telegramFutBotService.showDailyStatistic();
                break;

            case "/clubstock":
                telegramFutBotService.clubStock();
                break;
        }
    }
}
