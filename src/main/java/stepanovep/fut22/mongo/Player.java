package stepanovep.fut22.mongo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import stepanovep.fut22.core.Platform;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class Player {

    private String resourceId;
    private String futbinId;
    private String name;
    private Integer rating;
    private String league;
    private String nation;
    private Integer pcPrice;
    private Integer consolePrice;
    private LocalDateTime priceUpdatedDt;
    private LocalDateTime bidDt;

    public Integer getPrice(Platform platform) {
        switch (platform) {
            case PC:
                return pcPrice;
            case CONSOLE:
                return consolePrice;
            default:
                throw new IllegalArgumentException("Unknown platform: " + platform);
        }
    }
}
