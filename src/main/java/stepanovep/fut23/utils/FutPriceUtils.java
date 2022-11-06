package stepanovep.fut23.utils;

public class FutPriceUtils {

    public static int getNextBid(int startingBid, int currentBid) {
        if (currentBid != 0) {
            int inc = getIncrement(currentBid);
            return currentBid + inc;
        }

        return startingBid;
    }

    public static int getNextBid(int currentBid) {
        int inc = getIncrement(currentBid);
        return currentBid + inc;
    }

    public static int roundToValidFutPrice(int price) {
        int inc = getIncrement(price);
        return (int) ((price * 1.0 / inc) + 0.5) * inc;
    }

    private static int getIncrement(int price) {
        if (price < 1000)
            return 50;
        if (price < 10_000)
            return 100;
        if (price < 50_000)
            return 250;
        if (price < 100_000)
            return 500;
        return 1000;
    }
}
