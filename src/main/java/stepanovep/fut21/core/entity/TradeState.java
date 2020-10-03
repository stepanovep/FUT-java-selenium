package stepanovep.fut21.core.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import stepanovep.fut21.utils.Enums;

import javax.annotation.Nonnull;

public enum TradeState implements Enums.StringRepr {

    ACTIVE("active"),
    CLOSED("closed"),
    EXPIRED("expired");

    private final String code;

    TradeState(@Nonnull String code) {
        this.code = code;
    }

    @JsonValue
    @Override
    public String getCode() {
        return code;
    }
}
