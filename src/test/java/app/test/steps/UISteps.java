package app.test.steps;

import app.test.Utils;
import app.test.driver.DriverManager;
import app.test.pages.PageUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.manipulation.Ordering;
import org.openqa.selenium.WebElement;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static app.test.Utils.errorStep;

@SpringBootTest
@Slf4j
public class UISteps {

    private static final String MAIN_URL = "http://test-app.d6.dev.devcaz.com/";

    public static Map<String, String> pages = new HashMap<>();

    static {
        pages.put("Авторизация", MAIN_URL + "admin/login");
        pages.put("Главная", MAIN_URL + "configurator/dashboard/index");
        pages.put("Игроки", MAIN_URL + "user/player/admin");
    }

    @Given("открыть браузер")
    public void initBrowser() {
        DriverManager.getCurrentDriver().manage().window().maximize();
    }

    @When("открыть страницу {string}")
    public void openPage(String pageName) {
        if (pages.get(pageName) == null) {
            StringBuilder sb = new StringBuilder();
            pages.keySet().forEach(page -> sb.append(page).append("\n"));
            errorStep(String.format("Ошибка! Выполнено обращение к несуществующей странице %s. " +
                    "\n Актуальный список страниц: %s", pageName, sb));
        }
        navigateTo(pages.get(pageName));
        PageUtils.loadPage(pageName);
        PageUtils.checkIsPageLoaded(pageName);
    }

    @Then("страница {string} загружена")
    public void checkIsPageLoaded(String pageName) {
        if (pages.get(pageName) == null) {
            StringBuilder sb = new StringBuilder();
            pages.keySet().forEach(page -> sb.append(page).append("\n"));
            errorStep(String.format("Ошибка! Выполнено обращение к несуществующей странице %s. " +
                    "\n Актуальный список страниц: %s", pageName, sb));
        }
        PageUtils.loadPage(pageName);
        PageUtils.checkIsPageLoaded(pageName);
    }

    @When("ввести в поле {string} текст {string}")
    public void sendTextToField(String field, String text) {
        WebElement element = PageUtils.getElementFromPage(field);
        element.sendKeys(text);
        log.info(String.format("В поле %s введён текст %s", field, text));
    }

    @When("нажать на элемент {string}")
    public void clickOnElement(String element) {
        WebElement webElement = PageUtils.getElementFromPage(element);
        webElement.click();
        log.info(String.format("Выполнен клик по элементу %s", element));
    }

    @Then("элемент {string} отображается на странице")
    public void checkIsElementVisible(String element) {
        WebElement webElement = PageUtils.getElementFromPage(element);
        if (Utils.isVisible(webElement)) {
            log.info(String.format("Элемент %s отображается на странице", element));
        } else {
            errorStep(String.format("Ошибка! Элемент %s не отображается на странице", element));
        }
    }

    @Then("подтвердить что список элементов {string} был отсортирован по убыванию")
    public void checkIsListWereSorted(String list) {
        List<WebElement> elements = PageUtils.getElementListFromPage(list);
        List<String> baseList = new ArrayList<>();
        List<String> tempList = new ArrayList<>();
        elements.forEach(element -> {
            baseList.add(element.getText());
            tempList.add(element.getText());
        });

        Collections.sort(tempList);
        if (tempList.equals(baseList)) {
            log.info("Список отсортирован корректно");
        } else {
            errorStep("Ошибка! Список не был отсортирован корректно!");
        }
    }

    @When("обновить страницу")
    public void refresh() {
        DriverManager.getCurrentDriver().navigate().refresh();
        log.info("Выполнено обновление страницы");
    }

    @When("подождать {int} секунд")
    public void wait(int sec) {
        log.info("жду " + sec);
        Utils.wait(sec);
    }

    private void navigateTo(String url) {
        DriverManager.getCurrentDriver().get(url);
    }
}
