package stepanovep.fut23.core.page.transfers.search;

import stepanovep.fut23.utils.Enums;

/**
 * Позиция игрока
 */
public enum Position implements Enums.StringRepr {
    DEFENDER("Defenders"),
    MIDFIELDER("Midfielders"),
    ATTACKER("Attackers"),
    GOAL_KEEPER("GK");

    private final String code;

    Position(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
