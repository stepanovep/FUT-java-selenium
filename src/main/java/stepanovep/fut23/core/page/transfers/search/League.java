package stepanovep.fut23.core.page.transfers.search;

import stepanovep.fut23.utils.Enums;

public enum League implements Enums.StringRepr {

    PREMIER_LEAGUE("Premier League (ENG 1)"),
    LA_LIGA("LaLiga Santander (ESP 1)"),
    BUNDESLIGA("Bundesliga (GER 1)"),
    SERIE_A("Serie A TIM (ITA 1)"),
    LIGUE_1("Ligue 1 Uber Eats (FRA 1)"),
    EREDIVISIE("Eredivisie (NED 1)"),
    LIGA_NOS("Liga NOS (POR 1)");

    private final String code;

    League(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}