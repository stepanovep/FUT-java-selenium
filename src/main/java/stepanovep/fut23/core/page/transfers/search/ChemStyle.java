package stepanovep.fut23.core.page.transfers.search;

import stepanovep.fut23.utils.Enums;

/**
 * Стиль сыгранности игрока
 */
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
