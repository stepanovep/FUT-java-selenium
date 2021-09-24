package stepanovep.fut22.telegrambot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import stepanovep.fut22.bot.FutBot;

@Component
public class TelegramFutBotService {

    @Autowired
    private FutBot futBot;

    @Autowired
    private TelegramNotifier telegramNotifier;

    public void login() {
         futBot.login();
    }

    public void stop() {
        futBot.stop();
    }

    public void screenshot() {
        telegramNotifier.sendScreenshot(futBot.screenshot());
    }

    public void massBid() {
        futBot.massBid();
    }

    public void checkBids() {
        futBot.checkBids();
    }

    public void relistAll() {
        futBot.relistAll();
    }

    public void scheduleRelistAll() {
        futBot.scheduleRelistAll();
    }

    public void scheduleMassBid() {
        futBot.scheduleMassBid();
    }

    public void showDailyStatistic() {
        futBot.showDailyStatistic();
    }

    public void clubStock() {
        futBot.clubStock();
    }
}
