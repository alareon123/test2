package app.test.pages.admin;

import app.test.Utils;
import app.test.driver.DriverManager;
import app.test.pages.FieldName;
import app.test.pages.Page;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Component("Авторизация")
@Scope(SCOPE_PROTOTYPE)
@Lazy
public class LoginPage implements Page {

    @FieldName("Логотип")
    @FindBy(xpath = "//section[@class='logo']")
    private WebElement blockLogo;

    @FieldName("Поле логин")
    @FindBy(xpath = "//input[@id='UserLogin_username']")
    private WebElement fieldLogin;

    @FieldName("Поле пароль")
    @FindBy(xpath = "//input[@id='UserLogin_password']")
    private WebElement fieldPassword;

    @FieldName("Авторизация")
    @FindBy(xpath = "//input[@value='Sign in']")
    private WebElement buttonAuth;

    public LoginPage() {
        PageFactory.initElements(DriverManager.getCurrentDriver(), this);
    }

    @Override
    public boolean isPageLoaded() {
        return Utils.isElementLoaded(blockLogo);
    }
}
