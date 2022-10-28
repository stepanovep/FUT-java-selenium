package stepanovep.fut23;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import stepanovep.fut23.mongo.AuctionService;

@Component
public class ApplicationStartupListener implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private AuctionService auctionService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        auctionService.cleanUpOldAuctions();
    }
}
