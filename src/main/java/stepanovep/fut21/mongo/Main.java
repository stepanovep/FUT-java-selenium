package stepanovep.fut21.mongo;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class Main {

    public static void main(String[] args) {
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        MongoClient mongoClient = MongoClients.create(MongoClientSettings.builder()
                .codecRegistry(pojoCodecRegistry)
                .build());

        MongoDatabase database = mongoClient.getDatabase("test");
        MongoCollection<Person> collection = database.getCollection("people", Person.class);
        Person ada = Person.builder()
                .withName("Ada Byron")
                .withAge(20)
                .withAddress(Address.builder()
                        .withStreet("St James Square")
                        .withCity("London")
                        .withZip("W1")
                        .build())
                .build();

        collection.insertOne(ada);

        Person somebody = collection.find().first();
        System.out.println(somebody);
    }
}
