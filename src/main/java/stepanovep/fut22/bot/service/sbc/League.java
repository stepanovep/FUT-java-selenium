package stepanovep.fut22.bot.service.sbc;

import stepanovep.fut22.utils.Enums;

public enum League implements Enums.StringRepr {

    PREMIER_LEAGUE("ENG 1"),
    LA_LIGA("ESP 1"),
    BUNDESLIGA("GER 1"),
    SERIE_A("ITA 1"),
    LIGUE_1("FRA 1");

    private final String code;

    League(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
