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
    private MongoCollection<AuctionTrade> auctions;

    public void insert(@Nonnull String tradeId, @Nonnull Integer targetPrice) {
        requireNonNull(tradeId, "tradeId");
        requireNonNull(targetPrice, "targetPrice");
        auctions.insertOne(AuctionTrade.of(tradeId, targetPrice));
    }

    public Optional<AuctionTrade> get(@Nonnull String tradeId) {
        requireNonNull(tradeId, "tradeId");
        return Optional.ofNullable(auctions.find(eq("tradeId", tradeId)).first());
    }
}
