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
import stepanovep.fut23.config.FutbinProperties;
import stepanovep.fut23.core.Platform;
import stepanovep.fut23.mongo.Player;
import stepanovep.fut23.mongo.PlayerService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Slf4j
@Service
@RequiredArgsConstructor
public class FutbinService {

    private final PlayerService playerService;
    private final ExecutorService futbinExecutor;
    private final FutbinProperties futbinProperties;

    public void updatePrices() {
        futbinExecutor.execute(() -> {
            if (updatedRecently()) {
                log.info("Players prices have been updated recently - no need to spam futbin");
                return;
            }

            for (String futbinSquadUrl : futbinProperties.getBiddingSquadUrls()) {
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
        return player.getPriceUpdatedDt().plus(futbinProperties.getMinTimeBetweenRequests()).compareTo(LocalDateTime.now()) > 0;
    }

    @SneakyThrows
    private Elements getPlayersDivs(String futbinSquadUrl) {
        Document document = Jsoup.connect(futbinSquadUrl)
                .userAgent("Chrome")
                .referrer("http://www.google.com")
                .timeout(10000)
                .get();
        sleep();
        return document.getElementsByClass("card-med");
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

            if (futbinProperties.getDeduplicationMap().containsKey(futbinId)) {
                name = futbinProperties.getDeduplicationMap().get(futbinId);
            }
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
