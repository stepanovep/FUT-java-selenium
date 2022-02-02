package stepanovep.fut22.bot.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stepanovep.fut22.mongo.WonAuction;
import stepanovep.fut22.telegrambot.TelegramNotifier;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
     * Display all time statistics
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
     * Display weekly (so far since Monday) statistic of bought players: count and potential profit
     */
    public void displayWeeklyStatistic() {
        AtomicInteger count = new AtomicInteger(0);
        AtomicInteger profitSum = new AtomicInteger(0);
        LocalDateTime mondayMidnight = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
        wonAuctions.find(Filters.gte("boughtDt", mondayMidnight))
                .forEach(auction -> {
                    count.incrementAndGet();
                    profitSum.addAndGet(auction.getPotentialProfit());
                });

        System.out.printf("\n####  Weekly statistic: count=%d, potential profit=%d  ####\n\n", count.get(), profitSum.get());
    }

    /**
     * Send notification about daily statistics of bought players: count and potential profit
     */
    public void sendDailyStatistic() {
        AtomicInteger count = new AtomicInteger(0);
        AtomicInteger profitSum = new AtomicInteger(0);
        wonAuctions.find(Filters.gte("boughtDt", LocalDate.now().atStartOfDay()))
                .forEach(auction -> {
                    count.incrementAndGet();
                    profitSum.addAndGet(auction.getPotentialProfit());
                });

        telegramNotifier.sendMessage(String.format("Daily massbidding stats: count=%d, potential profit=%d",
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
