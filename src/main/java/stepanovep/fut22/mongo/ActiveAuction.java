package stepanovep.fut22.mongo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nonnull;

import java.time.LocalDateTime;

import static java.util.Objects.requireNonNull;

/**
 * Данные активной ставки
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
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

    private ActiveAuction(@Nonnull String tradeId, @Nonnull Integer targetPrice) {
        this.tradeId = requireNonNull(tradeId, "tradeId");
        this.targetPrice = requireNonNull(targetPrice, "targetPrice");
        this.createdDt = LocalDateTime.now();
    }

    public static ActiveAuction of(@Nonnull String tradeId, @Nonnull Integer targetPrice) {
        return new ActiveAuction(tradeId, targetPrice);
    }
}
