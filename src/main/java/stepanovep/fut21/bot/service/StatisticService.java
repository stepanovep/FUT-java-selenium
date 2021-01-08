package stepanovep.fut21.bot.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stepanovep.fut21.mongo.WonAuction;
import stepanovep.fut21.telegrambot.TelegramNotifier;

import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class StatisticService {

    @Autowired
    private MongoCollection<WonAuction> wonAuctions;

    @Autowired
    private TelegramNotifier telegramNotifier;

    /**
     * Вывести на консоль статистику покупок: { игрок # кол-во покупок # суммарный профит }
     */
    public void displayOverallBuys() {
        Map<String, CountSum> map = new TreeMap<>();
        wonAuctions.find().forEach(auction -> {
            String name = auction.getPlayerName();
            if (!map.containsKey(name)) {
                map.put(name, new CountSum());
            }
            CountSum countSum = map.get(name);
            countSum.add(auction.getPotentialProfit());
        });

        AtomicInteger totalEstimatedProfit = new AtomicInteger();
        map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(entry -> {
                    System.out.println(entry);
                    totalEstimatedProfit.addAndGet(entry.getValue().getSum());
                });

        System.out.println("\n### Total estimated profit: " + totalEstimatedProfit.get() + " ###\n");
    }

    /**
     * Отправить уведомление о дневной статистики покупки игроков
     */
    public void showDailyStatistic() {
        AtomicInteger count = new AtomicInteger(0);
        AtomicInteger profitSum = new AtomicInteger(0);
        wonAuctions.find(Filters.gte("boughtDt", LocalDate.now().atStartOfDay()))
                .forEach(auction -> {
                    count.incrementAndGet();
                    profitSum.addAndGet(auction.getPotentialProfit());
                });

        telegramNotifier.sendMessage(String.format("Today so far bought players: count=%d, potential profit=%d",
                count.get(), profitSum.get()));
    }

    private static class CountSum implements Comparable<CountSum> {
        private int count;
        private int sum;

        public void add(int sum) {
            this.sum += sum;
            this.count++;
        }

        public int getCount() {
            return count;
        }

        public int getSum() {
            return sum;
        }

        @Override
        public String toString() {
            return "CountSum{" +
                    "count=" + count +
                    ", sum=" + sum +
                    '}';
        }

        @Override
        public int compareTo(@NotNull CountSum o) {
            return -(this.sum - o.sum);
        }
    }
}
