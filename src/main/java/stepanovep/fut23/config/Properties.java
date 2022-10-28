package stepanovep.fut23.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import stepanovep.fut23.core.Platform;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
@Getter
@Setter
public class Properties {

    private String platform;
    private String chromeDriverExecutablePath;
    private String chromeUserDataDir;
    private boolean headless;

    public Platform getPlatform() {
        return Enum.valueOf(Platform.class, platform);
    }
}
