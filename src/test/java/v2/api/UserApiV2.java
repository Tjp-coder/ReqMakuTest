package v2.api;

import io.restassured.response.Response;
import v2.core.ApiClient;

import java.util.List;
import java.util.Map;

public class UserApiV2 {
    private final ApiClient client;

    public UserApiV2(ApiClient client) {
        this.client = client;
    }

    public Response page(String token, Map<String, Object> query) {
        return client.get("/sys/user/page", token, query);
    }

    public Response info(String token, Integer id) {
        return client.get("/sys/user/" + id, token, null);
    }

    public Response save(String token, Map<String, Object> body) {
        return client.post("/sys/user", token, body);
    }

    public Response delete(String token, List<Integer> userIds) {
        return client.delete("/sys/user", token, userIds);
    }
}
