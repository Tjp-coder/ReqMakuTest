package v2.api;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import v2.core.ApiClient;

import java.util.HashMap;
import java.util.Map;

public class AuthApiV2 {
    private final ApiClient client;

    public AuthApiV2(ApiClient client) {
        this.client = client;
    }

    public Response login(String username, String password) {
        Map<String, Object> payload = new HashMap<String, Object>();
        payload.put("username", username);
        payload.put("password", password);
        return client.post("/sys/auth/login", null, payload);
    }

    public String loginAndGetToken(String username, String password) {
        Response response = login(username, password);
        response.then().statusCode(200);
        Integer code = response.jsonPath().getInt("code");
        if (code == null || code != 0) {
            throw new IllegalStateException("login failed, response: " + response.asString());
        }
        return JsonPath.from(response.asString()).getString("data.access_token");
    }

    public Response logout(String token) {
        return client.post("/sys/auth/logout", token, null);
    }
}
