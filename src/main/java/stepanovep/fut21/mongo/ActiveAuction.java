package stepanovep.fut21.mongo;

import javax.annotation.Nonnull;

import java.time.LocalDateTime;

import static java.util.Objects.requireNonNull;

/**
 * Данные активной ставки
 */
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
     * Дата и время создания ставки
     */
    private LocalDateTime createdDt;

    /**
     * Пустой публичный контсруктор.
     * Необходим для кодека mongoDb
     */
    public ActiveAuction() {
    }

    private ActiveAuction(@Nonnull String tradeId, @Nonnull Integer targetPrice) {
        this.tradeId = requireNonNull(tradeId, "tradeId");
        this.targetPrice = requireNonNull(targetPrice, "targetPrice");
        this.createdDt = LocalDateTime.now();
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

    public LocalDateTime getCreatedDt() {
        return createdDt;
    }

    public void setCreatedDt(LocalDateTime createdDt) {
        this.createdDt = createdDt;
    }

    @Override
    public String toString() {
        return "ActiveAuction{" +
                "tradeId='" + tradeId + '\'' +
                ", targetPrice=" + targetPrice +
                ", createdDt=" + createdDt +
                '}';
    }
}
