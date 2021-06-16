package app.test.services;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@Service
public class UserService {

    private final String scope = "guest:default";
    private final String login = "front_2d6b0a8391742f5d789d7d915755e09e:";
    private final String grantType = "client_credentials";
    private final String url = "http://test-api.d6.dev.devcaz.com/";

    public Response getUserToken() {
        Map<String, String> parms = new HashMap<>();
        parms.put("grant_type", grantType);
        parms.put("scope", scope);
        return
                given()
                        .header("authorization", "Basic " + encode(login))
                        .contentType(ContentType.JSON)
                        .body(parms)
                        .post(url+"v2/oauth2/token")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();
    }

    public Response registerNewPlayer(Map<String, String> values) {
        String token = getUserToken().jsonPath().get("access_token").toString();
        Map<String, String> params = new HashMap<>();
        params.put("username", values.get("username"));
        params.put("password_change", encode(values.get("password")));
        params.put("password_repeat", encode(values.get("password")));
        params.put("email", values.get("email"));
        params.put("name", values.get("name"));
        params.put("surname", values.get("surname"));
        params.put("currency_code", values.get("currency"));
        return given()
                .header("authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(params)
                .post(url+"v2/players")
                .then()
                .statusCode(201)
                .extract()
                .response();
    }

    public Response authorization(String userName, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("grant_type", "password");
        params.put("username", userName);
        params.put("password", encode(password));
        return given()
                .header("authorization", "Basic " + encode(login))
                .contentType(ContentType.JSON)
                .body(params)
                .post(url+"v2/oauth2/token")
                .then()
                .statusCode(200)
                .extract()
                .response();
    }

    public Response getCurrentPlayerData(String user, String password, String id) {
        String token = authorization(user, password).jsonPath().get("access_token").toString();
        return given()
                .header("authorization", "Bearer " + token)
                .get(url+"v2/players/"+id);
    }

    private String encode(String str) {
        return new String(Base64.getEncoder().encode((str).getBytes()));
    }
}
