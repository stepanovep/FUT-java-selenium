package stepanovep.fut21.core.page.transfers.filter;

import stepanovep.fut21.utils.Enums;

public enum Nationality implements Enums.StringRepr {

    ARGENTINA("Argentina"),
    BRAZIL("Brazil"),
    ENGLAND("England"),
    FRANCE("France"),
    GERMANY("Germany"),
    NETHERLANDS("Netherlands"),
    ITALY("Italy"),
    PORTUGAL("Portugal"),
    SPAIN("Spain");

    private final String code;

    Nationality(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
