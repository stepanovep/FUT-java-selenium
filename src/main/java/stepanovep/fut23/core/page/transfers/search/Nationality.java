package stepanovep.fut23.core.page.transfers.search;

import stepanovep.fut23.utils.Enums;

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
