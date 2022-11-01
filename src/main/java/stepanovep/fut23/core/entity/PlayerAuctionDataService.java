package stepanovep.fut23.core.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import stepanovep.fut23.core.driver.FutWebDriver;
import stepanovep.fut23.utils.JsExecuteUtilsKt;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PlayerAuctionDataService {

    private final FutWebDriver driver;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public FutPlayerAuctionData getFutPlayerAuctionData() {
        Object executeResult = driver.executeScript(JsExecuteUtilsKt.getFutExtendedDataScript());
        try {
            String json = objectMapper.writeValueAsString(executeResult);
            return objectMapper.readValue(json, FutPlayerAuctionData.class);

        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    // TODO get list of players
    public List<FutPlayerAuctionData> getPlayers() {
        return List.of();
    }
}
