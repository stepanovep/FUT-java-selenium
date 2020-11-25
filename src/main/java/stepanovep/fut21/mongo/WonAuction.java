package stepanovep.fut21.mongo;

import net.gcardone.junidecode.Junidecode;

import javax.annotation.Nonnull;

public class WonAuction {

    private String tradeId;
    private String playerName;
    private Integer playerRating;
    private Integer boughtPrice;
    private Integer potentialProfit;

    /**
     * Пустой публичный контсруктор.
     * Необходим для кодека mongoDb
     */
    public WonAuction() {
    }

    private WonAuction(@Nonnull String tradeId,
                       @Nonnull String playerName,
                       @Nonnull Integer playerRating,
                       @Nonnull Integer boughtPrice,
                       @Nonnull Integer potentialProfit) {
        this.tradeId = tradeId;
        this.playerName = playerName;
        this.playerRating = playerRating;
        this.boughtPrice = boughtPrice;
        this.potentialProfit = potentialProfit;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Integer getPlayerRating() {
        return playerRating;
    }

    public void setPlayerRating(Integer playerRating) {
        this.playerRating = playerRating;
    }

    public Integer getBoughtPrice() {
        return boughtPrice;
    }

    public void setBoughtPrice(Integer boughtPrice) {
        this.boughtPrice = boughtPrice;
    }

    public Integer getPotentialProfit() {
        return potentialProfit;
    }

    public void setPotentialProfit(Integer potentialProfit) {
        this.potentialProfit = potentialProfit;
    }

    @Override
    public String toString() {
        return "WonAuction{" +
                "tradeId='" + tradeId + '\'' +
                ", playerName='" + playerName + '\'' +
                ", playerRating=" + playerRating +
                ", boughtPrice=" + boughtPrice +
                ", potentialProfit=" + potentialProfit +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String tradeId;
        private String playerName;
        private Integer playerRating;
        private Integer boughtPrice;
        private Integer potentialProfit;

        private Builder() {
        }

        public Builder withTradeId(String tradeId) {
            this.tradeId = tradeId;
            return this;
        }

        public Builder withPlayerName(String playerName) {
            this.playerName = Junidecode.unidecode(playerName);
            return this;
        }

        public Builder withPlayerRating(Integer playerRating) {
            this.playerRating = playerRating;
            return this;
        }

        public Builder withBoughtPrice(Integer boughtPrice) {
            this.boughtPrice = boughtPrice;
            return this;
        }

        public Builder withPotentialProfit(Integer potentialProfit) {
            this.potentialProfit = potentialProfit;
            return this;
        }

        public WonAuction build() {
            return new WonAuction(tradeId, playerName, playerRating, boughtPrice, potentialProfit);
        }
    }
}
