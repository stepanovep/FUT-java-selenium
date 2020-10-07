package stepanovep.fut21.mongo;

import com.mongodb.client.MongoCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.Objects;

@Service
public class AuctionService {

    @Autowired
    private MongoCollection<AuctionTrade> auctions;

    public void insert(@Nonnull String tradeId, @Nonnull Integer targetPrice) {
        Objects.requireNonNull(tradeId, "tradeId");
        Objects.requireNonNull(targetPrice, "targetPrice");
        auctions.insertOne(AuctionTrade.of(tradeId, targetPrice));
    }
}
