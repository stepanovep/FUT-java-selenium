package stepanovep.fut21.core.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * Расширенные данные элемента (карточки)
 *
 * Речь идет о данных, которых нет в DOM.
 * Например: resourceId, tradeId, bidState, auction.expires (in seconds)
 * Получить их можно путем исполнения js скрипта
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FutPlayerAuctionData {

    /**
     * Уникальный идентификатор карточки "resource_id" в базе FUT
     */
    private final String resourceId;
    private final String name;
    private final Integer rating;
    private final AuctionData auction;

    @JsonCreator
    private FutPlayerAuctionData(@Nonnull @JsonProperty("resourceId") String resourceId,
                                 @Nonnull @JsonProperty("name") String name,
                                 @Nullable @JsonProperty("rating") Integer rating,
                                 @Nonnull @JsonProperty("auction") AuctionData auction) {
        this.resourceId = requireNonNull(resourceId, "resourceId");
        this.name = requireNonNull(name, "name");
        this.rating = rating;
        this.auction = requireNonNull(auction, "auction");
    }

    @Nonnull
    public String getResourceId() {
        return resourceId;
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Nullable
    public Integer getRating() {
        return rating;
    }

    @Nonnull
    public AuctionData getAuction() {
        return auction;
    }

    @Override
    public String toString() {
        return "PlayerTradeExtendedData{" +
                "playerId='" + resourceId + '\'' +
                ", name='" + name + '\'' +
                ", rating=" + rating +
                ", auction=" + auction +
                '}';
    }

}
