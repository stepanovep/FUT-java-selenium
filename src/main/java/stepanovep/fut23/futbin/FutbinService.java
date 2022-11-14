package stepanovep.fut23.futbin;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.gcardone.junidecode.Junidecode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import stepanovep.fut23.core.Platform;
import stepanovep.fut23.mongo.Player;
import stepanovep.fut23.mongo.PlayerService;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Slf4j
@Service
@RequiredArgsConstructor
public class FutbinService {

    private final PlayerService playerService;
    private final ExecutorService futbinExecutor;

    private static final Duration MIN_TIME_BETWEEN_REQUESTS = Duration.ofMinutes(20);

    private static final List<String> FUTBIN_SQUADS_FOR_BIDDING_URLS = List.of(
            "https://www.futbin.com/23/squad/1500254",
            "https://www.futbin.com/23/squad/1500136",
            "https://www.futbin.com/23/squad/1499982",
            "https://www.futbin.com/23/squad/1624596",
            "https://www.futbin.com/23/squad/1932810"
    );

    public void updatePrices() {
        futbinExecutor.execute(() -> {
            if (updatedRecently()) {
                log.info("Players prices have been updated recently - no need to spam futbin");
                return;
            }

            for (String futbinSquadUrl : FUTBIN_SQUADS_FOR_BIDDING_URLS) {
                log.info("Updating players prices from futbin squad: url={}", futbinSquadUrl);

                Elements playersDivs = getPlayersDivs(futbinSquadUrl);
                for (Element playerDiv : playersDivs) {
                    String futbinId = playerDiv.attr("data-player-id");

                    if (playerService.getByFutbinId(futbinId).isPresent()) {
                        updatePlayerPrice(playerDiv, futbinId);
                    } else {
                        insertNewPlayer(playerDiv, futbinId);
                    }
                }

                sleep();
            }
            log.info("Futbin players prices updated");
        });
    }

    private boolean updatedRecently() {
        List<Player> players = playerService.getPlayersForMassBid(1, 200, 100_000, Platform.PC);
        if (players.isEmpty()) {
            return false;
        }
        Player player = players.get(0);
        return player.getPriceUpdatedDt().plus(MIN_TIME_BETWEEN_REQUESTS).compareTo(LocalDateTime.now()) > 0;
    }

    private Elements getPlayersDivs(String futbinSquadUrl) {
        try {
            Document document = Jsoup.connect(futbinSquadUrl)
                    .userAgent("Chrome")
                    .referrer("http://www.google.com")
                    .timeout(10000)
                    .get();
            sleep();
            return document.getElementsByClass("card-med");

        } catch (IOException exc) {
            log.error("Couldn't get futbin content:");
            throw new RuntimeException(exc);
        }
    }

    private void updatePlayerPrice(Element playerDiv, String futbinId) {
        try {
            int pcPrice = Integer.parseInt(playerDiv.attr("data-price-pc").replace(",", ""));
            int consolePrice = Integer.parseInt(playerDiv.attr("data-price-ps3").replace(",", ""));
            playerService.updatePriceByFutbinId(futbinId, pcPrice, Platform.PC);
            playerService.updatePriceByFutbinId(futbinId, consolePrice, Platform.CONSOLE);
        } catch (Exception e) {
            log.error("Error during updating player price: {}", playerDiv, e);
        }
    }

    private void insertNewPlayer(Element playerDiv, String futbinId) {
        try {
            Player player = new Player();
            String resourceId = playerDiv.attr("data-base-id");
            String name = Junidecode.unidecode(playerDiv.attr("data-player-commom"));
            int pcPrice = Integer.parseInt(playerDiv.attr("data-price-pc").replace(",", ""));
            int consolePrice = Integer.parseInt(playerDiv.attr("data-price-ps3").replace(",", ""));
            int rating = Integer.parseInt(playerDiv.attr("data-rating"));

            player.setFutbinId(futbinId);
            player.setResourceId(resourceId);
            player.setName(name);
            player.setPcPrice(pcPrice == 0 ? null : pcPrice);
            player.setConsolePrice(consolePrice == 0 ? null : consolePrice);
            player.setRating(rating);
            playerService.insert(player);

        } catch (Exception e) {
            log.error("Error during inserting new player: {}", playerDiv, e);
        }
    }

    @SneakyThrows
    private void sleep() {
        Thread.sleep(1000);
    }
}
