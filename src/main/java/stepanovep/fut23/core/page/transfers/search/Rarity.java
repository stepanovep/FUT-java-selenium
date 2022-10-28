package stepanovep.fut23.core.page.transfers.search;

import stepanovep.fut23.utils.Enums;

public enum Rarity implements Enums.StringRepr {

    NON_RARE("Common"),
    RARE("Rare"),
    FREEZE("FUT FREEZE"),
    HEADLINERS("HEADLINERS"),
    ICON("Icon"),
    OTW("Ones to Watch"),
    INFORM("Team of the Week"),
    UCL_NON_RARE("UEFA Champions League Common"),
    UCL_RARE("UEFA Champions League Rare");

    private final String code;

    Rarity(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
