package stepanovep.fut23.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "futbin")
@Getter
@Setter
public class FutbinProperties {

    private Map<String, String> deduplicationMap;
}
