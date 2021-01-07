package stepanovep.fut21.bot.service.sbc;

public class SbcOption {

    private final String title;
    private final League league;

    private SbcOption(String title, League league) {
        this.title = title;
        this.league = league;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String title;
        private League league;

        private Builder() {
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withLeague(League league) {
            this.league = league;
            return this;
        }

        public SbcOption build() {
            return new SbcOption(title, league);
        }
    }
}
