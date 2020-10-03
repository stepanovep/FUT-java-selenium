package stepanovep.fut21.core.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import stepanovep.fut21.utils.Enums;

import javax.annotation.Nonnull;
import java.util.Objects;

public enum BidState implements Enums.StringRepr {

    NONE("none"),
    HIGHEST("highest"),
    OUTBID("outbid");

    private final String code;

    BidState(@Nonnull String code) {
        this.code = Objects.requireNonNull(code, "code");
    }

    @JsonValue
    @Override
    public String getCode() {
        return code;
    }
}
