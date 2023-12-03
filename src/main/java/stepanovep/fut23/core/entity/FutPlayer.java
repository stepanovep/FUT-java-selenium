package stepanovep.fut23.core.entity;

import lombok.extern.slf4j.Slf4j;
import net.gcardone.junidecode.Junidecode;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import stepanovep.fut23.core.driver.FutWebDriver;
import stepanovep.fut23.core.locators.FutElementLocators;
import stepanovep.fut23.utils.FutPriceUtils;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class FutPlayer extends FutItem {

    private static int BIDS_COUNT = 0;
    private static final int BID_TRIES = 4;

    private final WebElement auctionContainer;

    private static final Pattern EXPIRATION_TIME_REGEX = Pattern.compile("(<?)(\\d+)(.+)");

    public FutPlayer(FutWebDriver driver, WebElement webElement) {
        super(driver, webElement);
        this.auctionContainer = webElement.findElement(By.cssSelector(".auction"));
    }

    public BidResult makeBid() {
        focus();
        driver.sleep(250, 500);
        if (!driver.findElement(FutElementLocators.BID_BUTTON).isEnabled()) {
            return BidResult.BID_BUTTON_INACTIVE;
        }
        driver.clickElement(FutElementLocators.BID_BUTTON);
        for (int tries = 0; tries < BID_TRIES; tries++) {
            driver.sleep(500, 1000);

            Optional<BidResult> dialogBidResult = handleDialogIfPresent();
            if (dialogBidResult.isPresent()) {
                return dialogBidResult.get();
            }

            if (isBid()) {
                log.info("Success bid count: {}", ++BIDS_COUNT);
                return BidResult.SUCCESS;
            }
            if (isOutbid()) {
                return BidResult.OUTBID;
            }
            List<WebElement> errorPopups = driver.findElements(By.cssSelector("#NotificationLayer .negative"));
            if (errorPopups.size() > 0) {
                String errorText = errorPopups.get(0).findElement(By.cssSelector("p")).getText();
                if (errorText.contains("Bid status changed")) {
                    if (tries == BID_TRIES - 1)
                        return BidResult.BID_CHANGED_ERROR;
                    continue;
                }
                return BidResult.TOO_MANY_ACTIONS_ERROR;
            }
        }

        return BidResult.IGNORED;
    }

    public boolean isBid() {
        driver.sleep(100);
        return webElement.getAttribute("class").contains("highest-bid");
    }

    public boolean isOutbid() {
        driver.sleep(100);
        return webElement.getAttribute("class").contains("outbid");
    }

    public boolean isWon() {
        return webElement.getAttribute("class").contains("won");
    }

    public String getName() {
        return Junidecode.unidecode(entityContainer.findElement(By.cssSelector(".name")).getText());
    }

    public Optional<Integer> getNextBid() {
        WebElement startPriceDiv = auctionContainer.findElements(By.cssSelector(".auctionValue")).get(0);
        if (!startPriceDiv.findElement(By.cssSelector(".label")).getText().contains("START PRICE")) {
            log.error("Start price element is incorrect: auctionContainer={}", auctionContainer.getText());
            return Optional.empty();
        }

        WebElement currentBidDiv = auctionContainer.findElements(By.cssSelector(".auctionValue")).get(1);
        if (!currentBidDiv.findElement(By.cssSelector(".label")).getText().contains("BID")) {
            log.error("Start price element is incorrect: auctionContainer={}", auctionContainer.getText());
            return Optional.empty();
        }

        String startPriceStr = startPriceDiv.findElement(By.cssSelector(".value")).getText();
        String currentBidStr = currentBidDiv.findElement(By.cssSelector(".value")).getText();
        if (currentBidStr.equals("---")) {
            return Optional.of(Integer.parseInt(startPriceStr.replace(",", "")));
        }

        return Optional.of(FutPriceUtils.getNextBid(Integer.parseInt(currentBidStr.replace(",", ""))));
    }

    public int getBuyNowPrice() {
        WebElement buyNowElement = auctionContainer.findElements(By.cssSelector(".auctionValue")).get(2);
        if (!buyNowElement.findElement(By.cssSelector(".label")).getText().toUpperCase().contains("BUY NOW")) {
            log.error("Buy now price element is incorrect: auctionContainer={}", auctionContainer.getText());
            throw new IllegalStateException("Bid price element is incorrect");
        }

        return Integer.parseInt(buyNowElement.findElement(By.cssSelector(".value")).getText().replace(",", ""));
    }

    public int getRating() {
        return Integer.parseInt(entityContainer.findElement(By.cssSelector(".rating")).getText());
    }

    public String getPosition() {
        return entityContainer.findElement(By.cssSelector(".position")).getText();
    }

    public int getBoughtPrice() {
        WebElement boughtPriceElement = driver.findElement(FutElementLocators.BOUGHT_PRICE);
        return Integer.parseInt(boughtPriceElement.getText().replace(",", ""));
    }

    public Duration getExpirationTime() {
        WebElement expirationTimeElement = auctionContainer.findElement(By.cssSelector(".auction-state > .time"));
        String expirationTimeStr = expirationTimeElement.getText();
        if (expirationTimeStr.equals("Processing...") || expirationTimeStr.equals("Expired")) {
            return Duration.ZERO;
        }

        Matcher matcher = EXPIRATION_TIME_REGEX.matcher(expirationTimeStr);
        if (!matcher.find()) {
            throw new IllegalStateException("Expiration time cannot be parsed");
        }

        long time = Long.parseLong(matcher.group(2));

        if (expirationTimeStr.contains("Second")) {
            return Duration.ofSeconds(time);

        } else if (expirationTimeStr.contains("Minute")) {
            if (matcher.group(1).equals("<")) {
                return Duration.ofMinutes(1);
            }
            return Duration.ofMinutes(time+1);

        } else if (expirationTimeStr.contains("Hour")) {
            return Duration.ofHours(time);

        } else {
            return Duration.ofDays(1L);
        }
    }
}
