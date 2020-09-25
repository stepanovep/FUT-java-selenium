package stepanovep.fut21.core.page.transfers.filter;

import stepanovep.fut21.utils.Enums;

public enum ChemStyle implements Enums.StringRepr {
    SHADOW("SHADOW"),
    HUNTER("HUNTER");

    private final String code;

    ChemStyle(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
