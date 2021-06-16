package app.test.steps;

import app.test.Utils;
import app.test.pages.PageUtils;
import app.test.services.UserService;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.cucumber.java.it.Ma;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

import static app.test.Utils.errorStep;

@CucumberContextConfiguration
@SpringBootTest
@Slf4j
public class Steps {

	@Autowired
	private UserService service;

	private Map<String, String> userParams;

	@When("получить токен гостя")
	public void getGuestAuthToken() {
		Response response = service.getUserToken();
		if (!response.jsonPath().get("access_token").toString().isEmpty()) {
			log.info("Гостевой токен получен");
		} else {
			errorStep("Ошибка! Не удалось получить гостевой токен");
		}
	}

	@When("зарегистрировать игрока со следующими данными:")
	public void registerTestUser(DataTable table) {
		userParams = new HashMap<>(table.asMap(String.class, String.class));
		userParams.put("username", Utils.generateRandomString(6));
		userParams.put("email", Utils.generateRandomString(5) + "@mail.ru");
		Response response = service.registerNewPlayer(userParams);
		log.info("Пользователь зарегестрирован");

		String expectedResult = "country_id=null, timezone_id=null, username=%s, email=%s, name=%s, surname=%s, " +
				"gender=null, phone_number=null, birthdate=null, bonuses_allowed=true, is_verified=false}";
		if (!response.jsonPath().get().toString().contains(String.format(expectedResult, userParams.get("username"), userParams.get("email"), userParams.get("name"),
				userParams.get("surname")))) {
			errorStep(String.format("Ошибка! Полученный результат не соотвествует ожидаемому. \n Ожидаемый результат - %s " +
					"\n Полученный результат - %s", expectedResult, response.jsonPath().get().toString()));
		} else {
			log.info("Полученный результат валиден, создан пользователь с именем " + userParams.get("username"));
			Integer id = response.jsonPath().get("id");
			userParams.put("id", String.valueOf(id));
		}
	}

	@And("авторизироватся созданным пользователем")
	public void authorizeByRegisteredUser() {
		Response response = service.authorization(userParams.get("username"), userParams.get("password"));
		if (!response.jsonPath().get("access_token").toString().isEmpty()) {
			log.info("Пользователь успешно авторизирован");
		} else {
			errorStep("Ошибка! Пользователь не получил токен после авторизации");
		}
	}

	@When("запросить данные текущего пользователя")
	public void getCurrentPlayerData() {
		Response response = service.getCurrentPlayerData(userParams.get("username"),
				userParams.get("password"), userParams.get("id"));
		if (response.getStatusCode() != 200) {
			errorStep("Ошибка! Не удалось получить данные пользователя, код ответа - " + response.getStatusCode());
		}
		String expectedResult = "country_id=null, timezone_id=null, username=%s, email=%s, name=%s, surname=%s, " +
				"gender=null, phone_number=null, birthdate=null, bonuses_allowed=true, is_verified=false}";
		if (!response.jsonPath().get().toString().contains(String.format(expectedResult, userParams.get("username"), userParams.get("email"), userParams.get("name"),
				userParams.get("surname")))) {
			errorStep(String.format("Ошибка! Полученный результат не соотвествует ожидаемому. \n Ожидаемый результат - %s " +
					"\n Полученный результат - %s", expectedResult, response.jsonPath().get().toString()));
		} else {
			log.info("Полученный результат валиден");
		}
	}

	@When("получить данные игрока с id {int}")
	public void getDataForPlayerWithId(int id) {
		Response response = service.getCurrentPlayerData(userParams.get("username"),
				userParams.get("password"), String.valueOf(id));
		if (response.getStatusCode() != 404) {
			errorStep("Ошибка! Код ответа не равен 404, актуальный код - " + response.getStatusCode());
		} else {
			log.info("Не удалось получить данные игрока с id " + id);
		}
	}

}
