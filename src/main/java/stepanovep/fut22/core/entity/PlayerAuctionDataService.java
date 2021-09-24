package stepanovep.fut22.core.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import stepanovep.fut22.core.driver.FutWebDriver;
import stepanovep.fut22.utils.JsExecuteUtilsKt;

import java.io.IOException;

@Component
public class PlayerAuctionDataService {

    @Autowired
    private FutWebDriver driver;

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
}
