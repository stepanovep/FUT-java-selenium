package stepanovep.fut21.core.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nonnull;

/**
 * Расширенные данные о состоянии ставки
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuctionExtendedData {

    private final String tradeId;
    private final String startingBid;
    private final String currentBid;
    private final String buyNowPrice;
    private final String bidState;
    private final String tradeState;
    private final Integer expires;

    @JsonCreator
    private AuctionExtendedData(@Nonnull @JsonProperty("tradeId") String tradeId,
                                @Nonnull @JsonProperty("startingBid") String startingBid,
                                @Nonnull @JsonProperty("currentBid") String currentBid,
                                @Nonnull @JsonProperty("buyNowPrice") String buyNowPrice,
                                @Nonnull @JsonProperty("bidState") String bidState,
                                @Nonnull @JsonProperty("tradeState") String tradeState,
                                @Nonnull @JsonProperty("expires") Integer expires) {
        this.tradeId = tradeId;
        this.startingBid = startingBid;
        this.currentBid = currentBid;
        this.buyNowPrice = buyNowPrice;
        this.bidState = bidState;
        this.tradeState = tradeState;
        this.expires = expires;
    }

    public String getTradeId() {
        return tradeId;
    }

    public String getStartingBid() {
        return startingBid;
    }

    public String getCurrentBid() {
        return currentBid;
    }

    public String getBuyNowPrice() {
        return buyNowPrice;
    }

    public String getBidState() {
        return bidState;
    }

    public String getTradeState() {
        return tradeState;
    }

    public Integer getExpires() {
        return expires;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "AuctionExtendedData{" +
                "tradeId='" + tradeId + '\'' +
                ", startingBid='" + startingBid + '\'' +
                ", currentBid='" + currentBid + '\'' +
                ", buyNowPrice='" + buyNowPrice + '\'' +
                ", bidState='" + bidState + '\'' +
                ", tradeState='" + tradeState + '\'' +
                ", expires=" + expires +
                '}';
    }

    /**
     * Билдер
     */
    public static final class Builder {
        private String tradeId;
        private String startingBid;
        private String currentBid;
        private String buyNowPrice;
        private String bidState;
        private String tradeState;
        private Integer expires;

        private Builder() {
        }

        public Builder withTradeId(String tradeId) {
            this.tradeId = tradeId;
            return this;
        }

        public Builder withStartingBid(String startingBid) {
            this.startingBid = startingBid;
            return this;
        }

        public Builder withCurrentBid(String currentBid) {
            this.currentBid = currentBid;
            return this;
        }

        public Builder withBuyNowPrice(String buyNowPrice) {
            this.buyNowPrice = buyNowPrice;
            return this;
        }

        public Builder withBidState(String bidState) {
            this.bidState = bidState;
            return this;
        }

        public Builder withTradeState(String tradeState) {
            this.tradeState = tradeState;
            return this;
        }

        public Builder withExpires(Integer expires) {
            this.expires = expires;
            return this;
        }

        public AuctionExtendedData build() {
            return new AuctionExtendedData(tradeId, startingBid, currentBid, buyNowPrice, bidState, tradeState, expires);
        }
    }
}
