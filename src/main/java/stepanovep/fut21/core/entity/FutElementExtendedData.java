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
 * Например: resourceId, transferId, bidState, auction.expires (in seconds)
 * Получить их можно путем исполнения js скрипта
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FutElementExtendedData {

    /**
     * Уникальный идентификатор карточки "resource_id" в базе FUT
     */
    private final String playerId;
    private final String name;
    private final Integer rating;
    private final AuctionData auction;

    @JsonCreator
    private FutElementExtendedData(@Nonnull @JsonProperty("resourceId") String resourceId,
                                   @Nonnull @JsonProperty("name") String name,
                                   @Nullable @JsonProperty("rating") Integer rating,
                                   @Nonnull @JsonProperty("auction") AuctionData auction) {
        this.playerId = requireNonNull(resourceId, "resourceId");
        this.name = requireNonNull(name, "name");
        this.rating = rating;
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
                "playerId='" + playerId + '\'' +
                ", name='" + name + '\'' +
                ", rating=" + rating +
                ", auction=" + auction +
                '}';
    }

}
