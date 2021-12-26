package stepanovep.fut22.core.page.club;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import stepanovep.fut22.core.page.transfers.search.ChemStyle;
import stepanovep.fut22.core.page.transfers.search.League;
import stepanovep.fut22.core.page.transfers.search.Nationality;
import stepanovep.fut22.core.page.transfers.search.Position;
import stepanovep.fut22.core.page.transfers.search.Quality;
import stepanovep.fut22.core.page.transfers.search.Rarity;

import javax.annotation.Nonnull;
import java.util.Optional;

@Builder(setterPrefix = "with")
@ToString
public class ClubPlayersSearchOptions {

    private final String name;
    private final Quality quality;
    private final Rarity rarity;
    private final Nationality nationality;
    private final League league;
    private final Position position;
    private final ChemStyle chemStyle;

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
}
