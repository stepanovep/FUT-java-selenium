package stepanovep.fut21.mongo;

import com.mongodb.client.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lte;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

@Service
public class PlayerService {

    private static final Logger log = LoggerFactory.getLogger(PlayerService.class);

    @Autowired
    private MongoCollection<Player> playersCollection;

    public Optional<Player> getByFutbinId(String futbinId) {
        return Optional.ofNullable(playersCollection.find(eq("futbinId", futbinId)).first());
    }

    public List<Player> getRandomPlayers(int count, int minPrice, int maxPrice) {
        List<Player> players = new ArrayList<>();
        playersCollection.find(and(gte("pcPrice", minPrice), lte("pcPrice", maxPrice)))
                .forEach(players::add);

        Collections.shuffle(players);
        return players.stream().limit(count).collect(Collectors.toList());
    }

    public void insert(Player player) {
        playersCollection.insertOne(player);
        playersCollection.updateOne(eq("resourceId", player.getResourceId()), set("priceUpdatedDt", LocalDateTime.now()));
    }

    public void updatePriceByFutbinId(String futbinId, Integer price) {
        Player player = playersCollection.find(eq("futbinId", futbinId)).first();
        if (player == null) {
            log.warn("Player not found: futbinId={}", futbinId);
            return;
        }

        playersCollection.updateOne(eq("futbinId", futbinId), combine(set("pcPrice", price), set("priceUpdatedDt", LocalDateTime.now())));
    }
}
