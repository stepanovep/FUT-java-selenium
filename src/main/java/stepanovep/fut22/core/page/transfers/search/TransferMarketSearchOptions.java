package stepanovep.fut22.core.page.transfers.search;

import lombok.Builder;
import lombok.ToString;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Контейнер с фильтрами для поиска карточек
 */
@Builder(setterPrefix = "with")
@ToString
public class TransferMarketSearchOptions {

    private final String name;
    private final Quality quality;
    private final Rarity rarity;
    private final Nationality nationality;
    private final League league;
    private final Position position;
    private final ChemStyle chemStyle;
    private final Integer bidMin;
    private final Integer bidMax;
    private final Integer buyNowMin;
    private final Integer buyNowMax;
    private final Integer targetPrice;

    @Nonnull
    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    @Nonnull
    public Optional<Quality> getQuality() {
        return Optional.ofNullable(quality);
    }

    @Nonnull
    public Optional<Rarity> getRarity() {
        return Optional.ofNullable(rarity);
    }

    @Nonnull
    public Optional<Nationality> getNationality() {
        return Optional.ofNullable(nationality);
    }

    @Nonnull
    public Optional<League> getLeague() {
        return Optional.ofNullable(league);
    }

    @Nonnull
    public Optional<Position> getPosition() {
        return Optional.ofNullable(position);
    }

    @Nonnull
    public Optional<ChemStyle> getChemStyle() {
        return Optional.ofNullable(chemStyle);
    }

    @Nonnull
    public Optional<Integer> getBidMin() {
        return Optional.ofNullable(bidMin);
    }

    @Nonnull
    public Optional<Integer> getBidMax() {
        return Optional.ofNullable(bidMax);
    }

    @Nonnull
    public Optional<Integer> getBuyNowMin() {
        return Optional.ofNullable(buyNowMin);
    }

    @Nonnull
    public Optional<Integer> getBuyNowMax() {
        return Optional.ofNullable(buyNowMax);
    }

    @Nonnull
    public Optional<Integer> getTargetPrice() {
        return Optional.ofNullable(targetPrice);
    }
}
