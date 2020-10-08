package stepanovep.fut21.core.entity;

/**
 * Результат попытки сделать ставку
 */
public enum BidResult {

    SUCCESS,
    OUTBID,
    IGNORED,
    BID_BUTTON_INACTIVE,
    BID_CHANGED_ERROR,
    TOO_MANY_ACTIONS_ERROR,

}
