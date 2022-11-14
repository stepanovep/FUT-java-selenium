package stepanovep.fut23.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import stepanovep.fut23.core.Platform;

@ConfigurationProperties
@Getter
@Setter
public class WebDriverProperties {

    private String platform;
    private String chromeDriverExecutablePath;
    private String chromeUserDataDir;

    public Platform getPlatform() {
        return Enum.valueOf(Platform.class, platform);
    }
}
