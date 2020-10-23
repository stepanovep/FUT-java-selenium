package stepanovep.fut21.mongo;

import javax.annotation.Nonnull;

import static java.util.Objects.requireNonNull;

public class ActiveAuction {
    /**
     * Уникальный идентификатор объекта, находящийся на ТР
     */
    private String tradeId;
    /**
     * Желаемая цена покупки объекта
     */
    private Integer targetPrice;

    /**
     * Пустой публичный контсруктор.
     * Необходим для кодека mongoDb
     */
    public ActiveAuction() {
    }

    private ActiveAuction(@Nonnull String tradeId, @Nonnull Integer targetPrice) {
        this.tradeId = requireNonNull(tradeId, "tradeId");
        this.targetPrice = requireNonNull(targetPrice, "targetPrice");
    }

    public static ActiveAuction of(@Nonnull String tradeId, @Nonnull Integer targetPrice) {
        return new ActiveAuction(tradeId, targetPrice);
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public Integer getTargetPrice() {
        return targetPrice;
    }

    public void setTargetPrice(Integer targetPrice) {
        this.targetPrice = targetPrice;
    }

    @Override
    public String toString() {
        return "Trade{" +
                "tradeId='" + tradeId + '\'' +
                ", targetPrice=" + targetPrice +
                '}';
    }
}
