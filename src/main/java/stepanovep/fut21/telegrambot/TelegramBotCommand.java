package stepanovep.fut21.telegrambot;

import stepanovep.fut21.utils.Enums;

public enum TelegramBotCommand implements Enums.StringRepr {
    LOGIN("/login"),
    STOP("/stop"),
    MASS_BID("/massbid"),
    CHECK_BIDS("/checkbids"),
    RELIST_ALL("/relistall"),
    SCREENSHOT("/screenshot");

    private final String code;

    TelegramBotCommand(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
