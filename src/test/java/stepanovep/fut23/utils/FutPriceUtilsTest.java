package stepanovep.fut23.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FutPriceUtilsTest {

    @ParameterizedTest
    @MethodSource("provideBids")
    public void getNextBidTest(int currentBid, int expectedNextBid) {
        int actualNextBid = FutPriceUtils.getNextBid(currentBid);
        assertEquals(expectedNextBid, actualNextBid);
    }

    @ParameterizedTest
    @MethodSource("providePricesToRound")
    public void roundToValidFutPriceTest(int price, int expectedRoundedPrice) {
        int actualRoundedPrice = FutPriceUtils.roundToValidFutPrice(price);
        assertEquals(expectedRoundedPrice, actualRoundedPrice);
    }

    private static Stream<Arguments> provideBids() {
        return Stream.of(
                Arguments.of(200, 250),
                Arguments.of(1000, 1100),
                Arguments.of(5000, 5100),
                Arguments.of(12000, 12250),
                Arguments.of(50000, 50500),
                Arguments.of(111000, 112000)
        );
    }

    private static Stream<Arguments> providePricesToRound() {
        return Stream.of(
                Arguments.of(150, 150),
                Arguments.of(975, 1000),
                Arguments.of(1125, 1100),
                Arguments.of(15123, 15000),
                Arguments.of(15180, 15250),
                Arguments.of(111423, 111000)
        );
    }
}