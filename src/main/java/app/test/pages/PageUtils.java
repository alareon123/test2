package app.test.pages;

import lombok.extern.slf4j.Slf4j;
import okio.Utf8;
import org.openqa.selenium.WebElement;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;

import static app.test.Utils.errorStep;

@Component
@Slf4j
public class PageUtils implements ApplicationContextAware {

    private static ApplicationContext context;
    private static Map<String, WebElement> pageElements;
    private static Map<String, List<WebElement>> pageElementLists;
    private static String currentPageName;
    private static Page currentPage;

    public static void loadPage(String pageName) {
        pageElements = new HashMap<>();
        pageElementLists = new HashMap<>();
        currentPage = (Page) context.getBean(pageName);
        loadElements(currentPage, pageElements);
        loadElementLists(currentPage, pageElementLists);
    }

    public static WebElement getElementFromPage(String elementName) {
        checkElementMap(pageElements, elementName);
        return pageElements.get(elementName);
    }

    public static List<WebElement> getElementListFromPage(String listName) {
        checkElementMap(pageElementLists, listName);
        return pageElementLists.get(listName);
    }

    public static void checkIsPageLoaded(String pageName) {
        if (currentPage != null) {
            if (!currentPageName.equals(pageName)) {
                errorStep(String.format("Ошибка! Ожидается загрузка страницы %s " +
                                "но вместо неё была загружена страница %s",
                        pageName, currentPageName));
                return;
            }
            if (currentPage.isPageLoaded()) {
                log.info(String.format("Страница %s была загружена", currentPageName));
                return;
            }
        }
        errorStep(String.format("Ошибка! Страница %s не была загружена", currentPageName));
    }

    private static void loadElements(Page page, Map<String, WebElement> pageElements) {
        List<Field> fields = new ArrayList<>();
        Class<?> validationClass = page.getClass();
        Class<?> superClass = validationClass.getSuperclass();
        Class<?> superPuperClass = null;
        fields.addAll(Arrays.asList(validationClass.getDeclaredFields()));
        if (superClass != null) {
            fields.addAll(Arrays.asList(superClass.getDeclaredFields()));
            superPuperClass = superClass.getSuperclass();
        }
        if (superPuperClass != null) {
            fields.addAll(Arrays.asList(superPuperClass.getDeclaredFields()));
        }
        currentPageName = validationClass.getAnnotation(Component.class).value();
        for (Field field : fields) {
            if (field.getType() == WebElement.class) {
                field.setAccessible(true);
                try {
                    pageElements.put(field.getAnnotation(FieldName.class).value(), (WebElement) field.get(page));
                } catch (IllegalAccessException | NullPointerException e) {
                    errorStep(String.format("Ошибка при загрузке эллемента %s",
                            field.getAnnotation(FieldName.class).value()));
                }
            }
        }
    }

    private static void loadElementLists(Page page, Map<String, List<WebElement>> pageElementLists) {
        List<Field> fields = new ArrayList<>();
        Class<?> validationClass = page.getClass();
        Class<?> superClass = validationClass.getSuperclass();
        Class<?> superPuperClass = null;
        fields.addAll(Arrays.asList(validationClass.getDeclaredFields()));
        if (superClass != null) {
            fields.addAll(Arrays.asList(superClass.getDeclaredFields()));
            superPuperClass = superClass.getSuperclass();
        }
        if (superPuperClass != null) {
            fields.addAll(Arrays.asList(superPuperClass.getDeclaredFields()));
        }
        for (Field field : fields) {
            if (field.getType() == List.class) {
                field.setAccessible(true);
                try {
                    pageElementLists.put(field.getAnnotation(FieldName.class).value(),
                            (List<WebElement>) field.get(page));
                } catch (IllegalAccessException e) {
                    errorStep(String.format("Ошибка при загрузке списка элементов %s",
                            field.getAnnotation(FieldName.class).value()));
                }
            }
        }
    }

    private static void checkElementMap(Map map, String elementName) {
        if (map == null || map.isEmpty()) {
            errorStep("Ошибка! Элементы текущей страницы не были загружены");
        }
        if (map.get(elementName) == null) {
            StringBuilder sb = new StringBuilder();
            map.keySet().forEach(elem -> sb.append(elem).append("\n"));
            errorStep(String.format("Ошибка! Выполнено обращение к несуществующему элементу %s на странице %s \n" +
                            "Проверьте правильность написания имени элемента. \n Актуальный список элементов: \n %s", elementName,
                    currentPageName, sb));
        }
    }

    @Override
    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
