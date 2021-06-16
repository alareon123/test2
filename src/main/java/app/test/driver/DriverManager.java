package app.test.driver;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

@Slf4j
public class DriverManager {

    private static WebDriver currentDriver;

    private static void initChrome() {
        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"/drivers/chromedriver.exe");
        currentDriver = new ChromeDriver();
    }

    public static WebDriver getCurrentDriver() {
        if (currentDriver == null) {
            initChrome();
        }
        return currentDriver;
    }

    public static void tearDown() {
        if (currentDriver != null) {
            currentDriver.quit();
            log.info("Выполняется закрытие драйвера");
        }
    }
}
