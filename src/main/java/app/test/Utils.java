package app.test;

import app.test.driver.DriverManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@Slf4j
public class Utils {

    public static void errorStep(String error) {
        log.error(error);
        throw new AssertionError(error);
    }

    public static boolean isElementLoaded(WebElement element) {
        try {
            if (isVisible(element)) {
                return true;
            } else {
                try {
                    waitUntilElementBeVisible(element, 5);
                } catch (TimeoutException | NoSuchElementException e) {
                    return false;
                }
                return isVisible(element);
            }
        } catch (NullPointerException e) {
            return false;
        }
    }

    public static boolean isVisible(WebElement element) {
        return element.isDisplayed();
    }

    public static void wait(int sec) {
        try {
            Thread.sleep(sec*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void waitUntilElementBeVisible(WebElement element, int seconds) throws TimeoutException{
        WebDriverWait webDriverWait = new WebDriverWait(DriverManager.getCurrentDriver(), seconds);
        if (isVisible(element)) {
            return;
        }
        webDriverWait.until(ExpectedConditions.visibilityOf(element));
    }

    public static String generateRandomString(int size) {
        return RandomStringUtils.random(size, true, false);
    }
}
