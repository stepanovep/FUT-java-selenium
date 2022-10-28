package stepanovep.fut23.mongo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.gcardone.junidecode.Junidecode;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class WonAuction {

    private String tradeId;
    private String playerName;
    private Integer playerRating;
    private Integer boughtPrice;
    private LocalDateTime boughtDt;
    private Integer potentialProfit;

    private WonAuction(@Nonnull String tradeId,
                       @Nonnull String playerName,
                       @Nonnull Integer playerRating,
                       @Nonnull Integer boughtPrice,
                       @Nonnull LocalDateTime boughtDt,
                       @Nonnull Integer potentialProfit) {
        this.tradeId = tradeId;
        this.playerName = playerName;
        this.playerRating = playerRating;
        this.boughtPrice = boughtPrice;
        this.boughtDt = boughtDt;
        this.potentialProfit = potentialProfit;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String tradeId;
        private String playerName;
        private Integer playerRating;
        private Integer boughtPrice;
        private LocalDateTime boughtDt;
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

        public Builder withBoughtDt(LocalDateTime boughtDt) {
            this.boughtDt = boughtDt;
            return this;
        }

        public Builder withPotentialProfit(Integer potentialProfit) {
            this.potentialProfit = potentialProfit;
            return this;
        }

        public WonAuction build() {
            boughtDt = boughtDt == null ? LocalDateTime.now() : boughtDt;
            return new WonAuction(tradeId, playerName, playerRating, boughtPrice, boughtDt, potentialProfit);
        }
    }
}
