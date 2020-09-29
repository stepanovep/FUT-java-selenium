package stepanovep.fut21.core.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;

import static java.util.Objects.requireNonNull;

/**
 * Расширенные данные элемента (карточки игрока)
 *
 * Речь идет о данных, которых нет в DOM.
 * Например: resourceId, transferId, bidState, auction.expires (in seconds)
 * Получить их можно путем исполнения js скрипта
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerTradeExtendedData {

    /**
     * Уникальный идентификатор карточки "resource_id" в базе FUT
     */
    private final String playerId;
    private final String name;
    private final Integer rating;
    private final AuctionExtendedData auction;

    @JsonCreator
    private PlayerTradeExtendedData(@Nonnull @JsonProperty("resourceId") String resourceId,
                                    @Nonnull @JsonProperty("name") String name,
                                    @Nonnull@JsonProperty("rating") Integer rating,
                                    @Nonnull @JsonProperty("auction") AuctionExtendedData auction) {
        this.playerId = requireNonNull(resourceId, "resourceId");
        this.name = requireNonNull(name, "name");
        this.rating = requireNonNull(rating, "rating");
        this.auction = requireNonNull(auction, "auction");
    }

    @Nonnull
    public String getPlayerId() {
        return playerId;
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Nonnull
    public Integer getRating() {
        return rating;
    }

    @Nonnull
    public AuctionExtendedData getAuction() {
        return auction;
    }

    @Override
    public String toString() {
        return "PlayerTradeExtendedData{" +
                "playerId='" + playerId + '\'' +
                ", name='" + name + '\'' +
                ", rating=" + rating +
                ", auction=" + auction +
                '}';
    }

    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        File file = new File("ext-data.json");
        PlayerTradeExtendedData playerTradeExtendedData = objectMapper.readValue(file, PlayerTradeExtendedData.class);
        System.out.println(playerTradeExtendedData);
    }
}
