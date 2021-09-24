package stepanovep.fut22.appconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import stepanovep.fut22.core.Platform;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class AppProperties {

    private String platform;
    private String chromeDriverExecutablePath;
    private String chromeUserDataDir;
    private boolean headless;

    public Platform getPlatform() {
        return Enum.valueOf(Platform.class, platform);
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getChromeDriverExecutablePath() {
        return chromeDriverExecutablePath;
    }

    public void setChromeDriverExecutablePath(String chromeDriverExecutablePath) {
        this.chromeDriverExecutablePath = chromeDriverExecutablePath;
    }

    public String getChromeUserDataDir() {
        return chromeUserDataDir;
    }

    public void setChromeUserDataDir(String chromeUserDataDir) {
        this.chromeUserDataDir = chromeUserDataDir;
    }

    public boolean isHeadless() {
        return headless;
    }

    public void setHeadless(boolean headless) {
        this.headless = headless;
    }
}
