package stepanovep.fut21.core.page.transfers.filter;

import stepanovep.fut21.utils.Enums;

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
