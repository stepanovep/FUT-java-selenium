package stepanovep.fut23.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "futbin")
@Getter
@Setter
public class FutbinProperties {

    private Map<String, String> deduplicationMap;
    private List<String> biddingSquadUrls;
    private Duration minTimeBetweenRequests;
}
