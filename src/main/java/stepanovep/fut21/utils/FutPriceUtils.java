package stepanovep.fut21.utils;

import stepanovep.fut21.core.entity.AuctionData;

public final class FutPriceUtils {

    private FutPriceUtils() {
    }

    public static Integer getNextBid(AuctionData auctionData) {
        if (auctionData.getCurrentBid() != 0) {
            int currentBid = auctionData.getCurrentBid();
            if (currentBid < 1000) {
                return currentBid + 50;
            }
            if (currentBid < 10000) {
                return currentBid + 100;
            }
            if (currentBid < 50000) {
                return currentBid + 250;
            }
            if (currentBid < 100_000) {
                return currentBid + 500;
            }
            return currentBid + 1000;
        } else {
            return auctionData.getStartingBid();
        }
    }
}
