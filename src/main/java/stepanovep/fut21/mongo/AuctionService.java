package stepanovep.fut21.mongo;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;
import static java.util.Objects.requireNonNull;

@Service
public class AuctionService {

    private static final Logger log = LoggerFactory.getLogger(AuctionService.class);

    @Autowired
    private MongoCollection<ActiveAuction> activeAuctions;

    @Autowired
    private MongoCollection<WonAuction> wonAuctions;

    public void insertActiveAuction(@Nonnull ActiveAuction activeAuction) {
        requireNonNull(activeAuction, "activeAuction");
        try {
            activeAuctions.insertOne(activeAuction);
        } catch (MongoWriteException exc) {
            log.error("Cannot insert active auction: ", exc);
        }
    }

    public void insertWonAuction(@Nonnull WonAuction wonAuction) {
        requireNonNull(wonAuction, "wonAuction");
        try {
            wonAuctions.insertOne(wonAuction);
        } catch (MongoWriteException exc) {
            log.error("Cannot insert won auction: ", exc);
        }
    }

    public Optional<ActiveAuction> getActiveAuction(@Nonnull String tradeId) {
        requireNonNull(tradeId, "tradeId");
        return Optional.ofNullable(activeAuctions.find(eq("tradeId", tradeId)).first());
    }

}
