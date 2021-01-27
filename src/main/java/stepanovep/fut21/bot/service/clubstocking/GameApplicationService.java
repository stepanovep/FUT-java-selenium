package stepanovep.fut21.bot.service.clubstocking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * Keyboard and mouse clicker for FIFA.exe application
 *
 * Only for PC
 */
public class GameApplicationService {

    private static final Logger log = LoggerFactory.getLogger(GameApplicationService.class);

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

    private void clickFifaAppIconInDesktop() throws InterruptedException {
        Thread.sleep(5000);
        pressAndRelease(KeyEvent.VK_WINDOWS, KeyEvent.VK_D);
        Thread.sleep(500);

        robot.mouseMove(FIFA_21_ICON_POSITION[0], FIFA_21_ICON_POSITION[1]);
        clickAndRelease();
        clickAndRelease();
        Thread.sleep(12000);
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

    private void activateAppWindow() throws InterruptedException {
        robot.mouseMove(FIFA_21_WINDOW_POSITION[0], FIFA_21_WINDOW_POSITION[1]);
        clickAndRelease();
        Thread.sleep(5000);
    }

    private void skipIntros() throws InterruptedException {
        log.info("Skip intros, choose profile and wait menu loading");
        pressAndRelease(KeyEvent.VK_ENTER);
        Thread.sleep(6000);

        pressAndRelease(KeyEvent.VK_ENTER);
        Thread.sleep(5000);

        pressAndRelease(KeyEvent.VK_ENTER);
        Thread.sleep(5000);

        pressAndRelease(KeyEvent.VK_ENTER);
        Thread.sleep(20000);
    }

    private void loginToUltimateTeam() throws InterruptedException {
        log.info("Login to Ultimate Team");
        pressAndRelease(KeyEvent.VK_ENTER);
        Thread.sleep(15000);
    }

    private void moveTargetsToUnassigned() throws InterruptedException {
        pressAndRelease(KeyEvent.VK_RIGHT);
        Thread.sleep(1000);
        pressAndRelease(KeyEvent.VK_RIGHT);
        Thread.sleep(1000);
        pressAndRelease(KeyEvent.VK_RIGHT);
        Thread.sleep(1000);
        pressAndRelease(KeyEvent.VK_ENTER);
        Thread.sleep(3000);

        pressAndRelease(KeyEvent.VK_LEFT);
        Thread.sleep(1000);
        pressAndRelease(KeyEvent.VK_ENTER);
        Thread.sleep(3000);

        pressAndRelease(KeyEvent.VK_W);
        Thread.sleep(7500);
        log.info("Transfer targets moved to unassigned pile");
    }

    private void logoutAndCloseApp() throws InterruptedException {
        pressAndRelease(KeyEvent.VK_ESCAPE);
        Thread.sleep(1000);
        pressAndRelease(KeyEvent.VK_UP);
        pressAndRelease(KeyEvent.VK_ENTER);
        Thread.sleep(3000);

        pressAndRelease(KeyEvent.VK_ESCAPE);
        Thread.sleep(1000);

        pressAndRelease(KeyEvent.VK_ESCAPE);
        Thread.sleep(1000);
        pressAndRelease(KeyEvent.VK_UP);
        pressAndRelease(KeyEvent.VK_ENTER);
        Thread.sleep(10000);

        pressAndRelease(KeyEvent.VK_ALT, KeyEvent.VK_F4);
        Thread.sleep(10000);
        log.info("Application closed");
    }

    private void pressAndRelease(int keycode) {
        robot.keyPress(keycode);
        robot.keyRelease(keycode);
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

    public static void main(String[] args) {
        GameApplicationService gameApplicationService = new GameApplicationService();
        gameApplicationService.moveTransferTargetsToUnassignedPile();
    }
}
