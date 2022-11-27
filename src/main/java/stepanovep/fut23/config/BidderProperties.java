package stepanovep.fut23.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "bidder")
@Getter
@Setter
public class BidderProperties {

    private int minPlayerPriceForMassBidder;
    private int maxPlayerPriceForMassBidder;
    private Duration maxExpirationTime;
    private int maxActiveBidsPerPlayer;
    private int minExpectedProfit;
    private int extraMarginWhenListing;
}
