package stepanovep.fut23.utils

class FutPriceUtils {

    companion object {
        @JvmStatic
        fun getNextBid(startingBid: Int, currentBid: Int): Int {
            if (currentBid != 0) {
                val inc = getIncrement(currentBid)
                return currentBid + inc
            }

            return startingBid
        }

        @JvmStatic
        fun getNextBid(currentBid: Int): Int {
            val inc = getIncrement(currentBid)
            return currentBid + inc
        }

        @JvmStatic
        fun roundToValidFutPrice(price: Int): Int {
            val inc = getIncrement(price)
            return ((price * 1.0 / inc) + 0.5).toInt() * inc
        }

        private fun getIncrement(price: Int): Int = when {
                price < 1000 -> 50
                price < 10_000 -> 100
                price < 50_000 -> 250
                price < 100_000 -> 500
                else -> 1000
            }
    }
}