package stepanovep.fut21.mongo;

import com.mongodb.client.MongoCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;
import static java.util.Objects.requireNonNull;

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

}
