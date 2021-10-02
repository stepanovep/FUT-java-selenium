package stepanovep.fut22.futbin;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.gcardone.junidecode.Junidecode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stepanovep.fut22.core.Platform;
import stepanovep.fut22.mongo.Player;
import stepanovep.fut22.mongo.PlayerService;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Slf4j
@Service
public class FutbinService {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private ExecutorService futbinExecutor;

    private static final Duration MIN_TIME_BETWEEN_REQUESTS = Duration.ofMinutes(20);

    private static final List<String> FUTBIN_TRADE_SQUADS_URLS = List.of(
            "https://www.futbin.com/22/squad/454889",
            "https://www.futbin.com/22/squad/454511",
            "https://www.futbin.com/22/squad/452851",
            "https://www.futbin.com/22/squad/945509"
    );

    public void updatePrices() {
        futbinExecutor.execute(() -> {
            if (updatedRecently()) {
                log.info("Players prices have been updated recently - no need to spam futbin");
                return;
            }

            for (String futbinSquadUrl : FUTBIN_TRADE_SQUADS_URLS) {
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
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36")
                    .referrer("http://www.google.com")
                    .get();
            return document.getElementsByClass("card-med");

        } catch (IOException exc) {
            log.error("Couldn't get futbin content:");
            throw new RuntimeException(exc);
        }
    }

    private void updatePlayerPrice(Element playerDiv, String futbinId) {
        Element pricesBlock = playerDiv.selectFirst(".prices");
        int pcPrice = Integer.parseInt(pricesBlock.selectFirst(".pcdisplay-pc-price").text().replace(",", ""));
        int psPrice = Integer.parseInt(pricesBlock.selectFirst(".pcdisplay-ps-price").text().replace(",", ""));
        int xboxPrice = Integer.parseInt(pricesBlock.selectFirst(".pcdisplay-xbox-price").text().replace(",", ""));
        playerService.updatePriceByFutbinId(futbinId, pcPrice, Platform.PC);
        playerService.updatePriceByFutbinId(futbinId, psPrice, Platform.PS);
        playerService.updatePriceByFutbinId(futbinId, xboxPrice, Platform.XBOX);
    }

    private void insertNewPlayer(Element playerDiv, String futbinId) {
        Player player = new Player();
        String resourceId = playerDiv.attr("data-base-id");
        String name = Junidecode.unidecode(playerDiv.attr("data-player-commom"));
        Element pricesBlock = playerDiv.selectFirst(".prices");
        int pcPrice = Integer.parseInt(pricesBlock.selectFirst(".pcdisplay-pc-price").text().replace(",", ""));
        int psPrice = Integer.parseInt(pricesBlock.selectFirst(".pcdisplay-ps-price").text().replace(",", ""));
        int xboxPrice = Integer.parseInt(pricesBlock.selectFirst(".pcdisplay-xbox-price").text().replace(",", ""));
        int rating = Integer.parseInt(playerDiv.attr("data-rating"));
        int nationId = Integer.parseInt(playerDiv.attr("data-player-nation"));
        int leagueId = Integer.parseInt(playerDiv.attr("data-player-league"));

        player.setFutbinId(futbinId);
        player.setResourceId(resourceId);
        player.setName(name);
        player.setPcPrice(pcPrice == 0 ? null : pcPrice);
        player.setPsPrice(psPrice == 0 ? null: psPrice);
        player.setXboxPrice(xboxPrice == 0 ? null : xboxPrice);
        player.setRating(rating);
        playerService.insert(player);
    }

    @SneakyThrows
    private void sleep() {
        Thread.sleep(1000);
    }
}
