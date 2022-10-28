package stepanovep.fut23.bot.service.clubstocking;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.time.Duration;

/**
 * Keyboard and mouse clicker for FIFA.exe application
 *
 * Only for PC
 */
@Slf4j
public class GameApplicationService {

    /**
     * FIFA.exe launcher icon in desktop
     */
    private static final int[] FIFA_21_ICON_POSITION = new int[] {2930, 50};
    /**
     * FIFA.exe window position to focus
     */
    private static final int[] FIFA_21_WINDOW_POSITION = new int[] {50, 10};

    private final Robot robot;

    static {
        System.setProperty("java.awt.headless", "false");
    }

    public GameApplicationService() {
        try {
            this.robot = new Robot();
        } catch (AWTException exc) {
            throw new RuntimeException(exc);
        }
        robot.setAutoDelay(100);
    }

    /**
     * Start application and move won players from transfer targets to unassigned pile
     */
    public void moveTransferTargetsToUnassignedPile() {
        try {
            log.info("Starting script in 5 sec");
            clickFifaAppIconInDesktop();
            clickStartGameButton();
            activateAppWindow();
            skipIntros();

            loginToUltimateTeam();
            moveTargetsToUnassigned();

            logoutAndCloseApp();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    private void clickFifaAppIconInDesktop() {
        Thread.sleep(5000);
        pressAndRelease(KeyEvent.VK_WINDOWS, KeyEvent.VK_D);
        Thread.sleep(500);

        robot.mouseMove(FIFA_21_ICON_POSITION[0], FIFA_21_ICON_POSITION[1]);
        clickAndRelease();
        clickAndRelease(Duration.ofSeconds(12L));
    }

    private void clickStartGameButton() throws InterruptedException {
        pressAndRelease(KeyEvent.VK_TAB);
        pressAndRelease(KeyEvent.VK_TAB);
        pressAndRelease(KeyEvent.VK_TAB);
        pressAndRelease(KeyEvent.VK_TAB);
        pressAndRelease(KeyEvent.VK_ENTER);
        pressAndRelease(KeyEvent.VK_ALT, KeyEvent.VK_F4);
        Thread.sleep(20000);
    }

    @SneakyThrows
    private void activateAppWindow() {
        robot.mouseMove(FIFA_21_WINDOW_POSITION[0], FIFA_21_WINDOW_POSITION[1]);
        clickAndRelease();
        Thread.sleep(5000);
    }

    private void skipIntros() {
        log.info("Skip intros, choose profile and wait menu loading");
        pressAndRelease(KeyEvent.VK_ENTER, Duration.ofSeconds(6L));
        pressAndRelease(KeyEvent.VK_ENTER, Duration.ofSeconds(5L));
        pressAndRelease(KeyEvent.VK_ENTER, Duration.ofSeconds(5L));
        pressAndRelease(KeyEvent.VK_ENTER, Duration.ofSeconds(20L));
    }

    private void loginToUltimateTeam() {
        log.info("Login to Ultimate Team");
        pressAndRelease(KeyEvent.VK_RIGHT, Duration.ofSeconds(1L));
        pressAndRelease(KeyEvent.VK_ENTER, Duration.ofSeconds(30L));
    }

    private void moveTargetsToUnassigned() {
        pressAndRelease(KeyEvent.VK_RIGHT, Duration.ofSeconds(1L));
        pressAndRelease(KeyEvent.VK_RIGHT, Duration.ofSeconds(1L));
        pressAndRelease(KeyEvent.VK_ENTER, Duration.ofSeconds(3L));

        pressAndRelease(KeyEvent.VK_LEFT, Duration.ofSeconds(1L));
        pressAndRelease(KeyEvent.VK_ENTER, Duration.ofSeconds(3L));

        pressAndRelease(KeyEvent.VK_W, Duration.ofSeconds(8L));
        log.info("Transfer targets moved to unassigned pile");
    }

    @SneakyThrows
    private void logoutAndCloseApp() {
        pressAndRelease(KeyEvent.VK_ESCAPE, Duration.ofSeconds(1L));
        pressAndRelease(KeyEvent.VK_UP);
        pressAndRelease(KeyEvent.VK_ENTER, Duration.ofSeconds(3L));

        pressAndRelease(KeyEvent.VK_ESCAPE, Duration.ofSeconds(1L));

        pressAndRelease(KeyEvent.VK_ESCAPE, Duration.ofSeconds(1L));
        pressAndRelease(KeyEvent.VK_ENTER, Duration.ofSeconds(10L));

        pressAndRelease(KeyEvent.VK_ALT, KeyEvent.VK_F4);
        Thread.sleep(10000);
        log.info("Application closed");
    }

    private void pressAndRelease(int keycode) {
        robot.keyPress(keycode);
        robot.keyRelease(keycode);
    }

    @SneakyThrows
    private void pressAndRelease(int keycode, Duration pause) {
        robot.keyPress(keycode);
        robot.keyRelease(keycode);
        Thread.sleep(pause.toMillis());
    }

    private void pressAndRelease(int keycode1, int keycode2) {
        robot.keyPress(keycode1);
        robot.keyPress(keycode2);

        robot.keyRelease(keycode1);
        robot.keyRelease(keycode2);
    }

    private void clickAndRelease() {
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    @SneakyThrows
    private void clickAndRelease(Duration pause) {
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        Thread.sleep(pause.toMillis());
    }
}
