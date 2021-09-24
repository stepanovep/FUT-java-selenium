package stepanovep.fut22.core.page.transfers.filter;

import stepanovep.fut22.utils.Enums;

/**
 * Тип качества карточки
 */
public enum Quality implements Enums.StringRepr {
    BRONZE("Bronze"),
    SILVER("Silver"),
    GOLD("Gold"),
    SPECIAL("Special");

    private final String code;

    Quality(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}