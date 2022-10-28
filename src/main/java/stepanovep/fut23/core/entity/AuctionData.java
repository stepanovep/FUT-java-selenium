package stepanovep.fut23.core.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nonnull;

/**
 * Данные о состоянии ставки
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuctionData {

    private final String tradeId;
    private final Integer startingBid;
    private final Integer currentBid;
    private final Integer buyNowPrice;
    private final BidState bidState;
    private final TradeState tradeState;
    private final Integer expires;

    @JsonCreator
    private AuctionData(@Nonnull @JsonProperty("tradeId") String tradeId,
                        @Nonnull @JsonProperty("startingBid") Integer startingBid,
                        @Nonnull @JsonProperty("currentBid") Integer currentBid,
                        @Nonnull @JsonProperty("buyNowPrice") Integer buyNowPrice,
                        @Nonnull @JsonProperty("bidState") BidState bidState,
                        @Nonnull @JsonProperty("tradeState") TradeState tradeState,
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

    public Integer getStartingBid() {
        return startingBid;
    }

    public Integer getCurrentBid() {
        return currentBid;
    }

    public Integer getBuyNowPrice() {
        return buyNowPrice;
    }

    public BidState getBidState() {
        return bidState;
    }

    public TradeState getTradeState() {
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
        private Integer startingBid;
        private Integer currentBid;
        private Integer buyNowPrice;
        private BidState bidState;
        private TradeState tradeState;
        private Integer expires;

        private Builder() {
        }

        public Builder withTradeId(String tradeId) {
            this.tradeId = tradeId;
            return this;
        }

        public Builder withStartingBid(Integer startingBid) {
            this.startingBid = startingBid;
            return this;
        }

        public Builder withCurrentBid(Integer currentBid) {
            this.currentBid = currentBid;
            return this;
        }

        public Builder withBuyNowPrice(Integer buyNowPrice) {
            this.buyNowPrice = buyNowPrice;
            return this;
        }

        public Builder withBidState(BidState bidState) {
            this.bidState = bidState;
            return this;
        }

        public Builder withTradeState(TradeState tradeState) {
            this.tradeState = tradeState;
            return this;
        }

        public Builder withExpires(Integer expires) {
            this.expires = expires;
            return this;
        }

        public AuctionData build() {
            return new AuctionData(tradeId, startingBid, currentBid, buyNowPrice, bidState, tradeState, expires);
        }
    }
}
