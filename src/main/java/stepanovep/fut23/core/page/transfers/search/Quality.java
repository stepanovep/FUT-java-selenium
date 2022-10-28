package stepanovep.fut23.core.page.transfers.search;

import stepanovep.fut23.utils.Enums;

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
