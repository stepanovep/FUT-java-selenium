package stepanovep.fut21.mongo;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class Main {

    public static void main(String[] args) {
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        MongoClient mongoClient = MongoClients.create(MongoClientSettings.builder()
                .codecRegistry(pojoCodecRegistry)
                .build());

        MongoDatabase database = mongoClient.getDatabase("fut");
        MongoCollection<AuctionTrade> collection = database.getCollection("auction", AuctionTrade.class);
        AuctionTrade auctionTrade = AuctionTrade.of("AAA", 100500);

        collection.insertOne(auctionTrade);

        AuctionTrade first = collection.find(eq("tradeId", "AAA")).first();
        System.out.println(first);
    }
}
