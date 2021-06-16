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

import java.util.List;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Component("Игроки")
@Scope(SCOPE_PROTOTYPE)
@Lazy
public class PlayersPage implements Page {

    @FieldName("Таблица игроков")
    @FindBy(xpath = "//div[@id='payment-system-transaction-grid']/table")
    private WebElement tableUsers;

    @FieldName("Сортировка имя пользователя")
    @FindBy(xpath = "//th[@id='payment-system-transaction-grid_c1']/a")
    private WebElement buttonUserNameSort;

    @FieldName("Имена пользователей")
    @FindBy(xpath = "//a[contains(@href, '/user/player/details?id=') and not (contains(@class, 'btn'))]")
    private List<WebElement> usersList;

    public PlayersPage() {
        PageFactory.initElements(DriverManager.getCurrentDriver(), this);
    }

    @Override
    public boolean isPageLoaded() {
        return Utils.isElementLoaded(tableUsers);
    }
}
