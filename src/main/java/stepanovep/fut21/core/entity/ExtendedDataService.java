package stepanovep.fut21.core.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import stepanovep.fut21.core.driver.FutWebDriver;
import stepanovep.fut21.utils.JsExecuteUtilsKt;

import java.io.IOException;

@Component
public class ExtendedDataService {

    @Autowired
    private FutWebDriver driver;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public FutElementExtendedData getFutElementExtendedData() {
        Object executeResult = driver.executeScript(JsExecuteUtilsKt.getFutExtendedDataScript());
        try {
            String json = objectMapper.writeValueAsString(executeResult);
            return objectMapper.readValue(json, FutElementExtendedData.class);

        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }
}
