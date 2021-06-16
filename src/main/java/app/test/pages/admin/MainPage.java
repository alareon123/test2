package app.test.pages.admin;

import app.test.Utils;
import app.test.driver.DriverManager;
import app.test.pages.FieldName;
import app.test.pages.Page;
import com.sun.tools.javac.Main;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Component("Главная")
@Scope(SCOPE_PROTOTYPE)
@Lazy
public class MainPage implements Page {

    @FieldName("Список игроков")
    @FindBy(xpath = "//a[@href='/user/player/admin']//div[@class='panel mini-box']")
    private WebElement buttonPlayersList;

    public MainPage() {
        PageFactory.initElements(DriverManager.getCurrentDriver(), this);
    }

    @Override
    public boolean isPageLoaded() {
        return Utils.isElementLoaded(buttonPlayersList);
    }
}
