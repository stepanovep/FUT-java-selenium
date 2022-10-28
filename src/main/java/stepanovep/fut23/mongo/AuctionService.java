package stepanovep.fut23.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.exists;
import static com.mongodb.client.model.Filters.lte;
import static java.util.Objects.requireNonNull;

@Slf4j
@Service
public class AuctionService {

    @Autowired
    private MongoCollection<ActiveAuction> activeAuctions;

    @Autowired
    private MongoCollection<WonAuction> wonAuctions;

    public void insertActiveAuction(@Nonnull ActiveAuction activeAuction) {
        requireNonNull(activeAuction, "activeAuction");
        activeAuctions.insertOne(activeAuction);
    }

    public void insertWonAuction(@Nonnull WonAuction wonAuction) {
        requireNonNull(wonAuction, "wonAuction");
        wonAuctions.insertOne(wonAuction);
    }

    public Optional<ActiveAuction> getActiveAuction(@Nonnull String tradeId) {
        requireNonNull(tradeId, "tradeId");
        return Optional.ofNullable(activeAuctions.find(eq("tradeId", tradeId)).first());
    }

    /**
     * Очистить аукционы, которые были созданы 24 или более часов назад
     */
    public void cleanUpOldAuctions() {
        DeleteResult deleteResult = activeAuctions.deleteMany(and(
                exists("createdDt"),
                lte("createdDt", LocalDateTime.now().minusDays(1L))));
        log.info("Deleted old auctions: count={}", deleteResult.getDeletedCount());
    }

}
