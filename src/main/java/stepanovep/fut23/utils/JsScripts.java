package stepanovep.fut23.utils;

public class JsScripts {

    public static String GET_FUT_EXTENDED_DATA_SCRIPT = """
            function getFutEntity() {
                var listController = getAppMain().getRootViewController().getPresentedViewController().getCurrentViewController().getCurrentController()._listController;
            
                if (listController) {
                    current = listController.getIterator().current();
                    return {
                        resourceId: current.definitionId,
                        name: current._staticData.name,
                        rating: current.rating,
                        type: current.type,
                        playStyle: current.playStyle,
                        auction: {
                            expires: current._auction.expires,
                            startingBid: current._auction.startingBid,
                            currentBid: current._auction.currentBid,
                            buyNowPrice: current._auction.buyNowPrice,
                            bidState: current._auction.bidState,
                            tradeState: current._auction.tradeState,
                            tradeId: current._auction.tradeId,
                            isUpdating: current._auction.isUpdating
                        }
                    }
                }
            
                return -1
            }
            
            return getFutEntity()
            """;
}
