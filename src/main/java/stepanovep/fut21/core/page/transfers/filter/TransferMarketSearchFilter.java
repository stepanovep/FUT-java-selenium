package stepanovep.fut21.core.page.transfers.filter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Контейнер с фильтрами для поиска карточек
 */
public class TransferMarketSearchFilter {

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

    private TransferMarketSearchFilter(@Nullable String name,
                                       @Nullable Quality quality,
                                       @Nullable Rarity rarity,
                                       @Nullable Nationality nationality,
                                       @Nullable League league,
                                       @Nullable Position position,
                                       @Nullable ChemStyle chemStyle,
                                       @Nullable Integer bidMin, @Nullable Integer bidMax,
                                       @Nullable Integer buyNowMin, @Nullable Integer buyNowMax,
                                       @Nullable Integer targetPrice) {
        this.name = name;
        this.quality = quality;
        this.rarity = rarity;
        this.nationality = nationality;
        this.league = league;
        this.position = position;
        this.chemStyle = chemStyle;
        this.bidMin = bidMin;
        this.bidMax = bidMax;
        this.buyNowMin = buyNowMin;
        this.buyNowMax = buyNowMax;
        this.targetPrice = targetPrice;
    }

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

    @Override
    public String toString() {
        return "TransferMarketSearchFilter{" +
                "name='" + name + '\'' +
                ", quality=" + quality +
                ", rarity=" + rarity +
                ", nationality=" + nationality +
                ", league=" + league +
                ", position=" + position +
                ", chemStyle=" + chemStyle +
                ", bidMin=" + bidMin +
                ", bidMax=" + bidMax +
                ", buyNowMin=" + buyNowMin +
                ", buyNowMax=" + buyNowMax +
                ", targetPrice=" + targetPrice +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String name;
        private Quality quality;
        private Rarity rarity;
        private Nationality nationality;
        private League league;
        private Position position;
        private ChemStyle chemStyle;
        private Integer bidMin;
        private Integer bidMax;
        private Integer buyNowMin;
        private Integer buyNowMax;
        private Integer targetPrice;

        private Builder() {
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withQuality(Quality quality) {
            this.quality = quality;
            return this;
        }

        public Builder withRarity(Rarity rarity) {
            this.rarity = rarity;
            return this;
        }

        public Builder withNationality(Nationality nationality) {
            this.nationality = nationality;
            return this;
        }

        public Builder withLeague(League league) {
            this.league = league;
            return this;
        }

        public Builder withPosition(Position position) {
            this.position = position;
            return this;
        }

        public Builder withChemStyle(ChemStyle chemStyle) {
            this.chemStyle = chemStyle;
            return this;
        }

        public Builder withBidMin(Integer bidMin) {
            this.bidMin = bidMin;
            return this;
        }

        public Builder withBidMax(Integer bidMax) {
            this.bidMax = bidMax;
            return this;
        }

        public Builder withBuyNowMin(Integer buyNowMin) {
            this.buyNowMin = buyNowMin;
            return this;
        }

        public Builder withBuyNowMax(Integer buyNowMax) {
            this.buyNowMax = buyNowMax;
            return this;
        }

        public Builder withTargetPrice(Integer targetPrice) {
            this.targetPrice = targetPrice;
            return this;
        }

        public TransferMarketSearchFilter build() {
            return new TransferMarketSearchFilter(
                    name, quality, rarity, nationality, league, position, chemStyle,
                    bidMin, bidMax, buyNowMin, buyNowMax, targetPrice);
        }
    }
}
