package stepanovep.fut22.core.entity;

/**
 * Результат попытки сделать ставку
 */
public enum BidResult {

    SUCCESS,
    OUTBID,
    IGNORED,
    LIMIT_REACHED,
    BID_BUTTON_INACTIVE,
    BID_CHANGED_ERROR,
    TOO_MANY_ACTIONS_ERROR,

}
