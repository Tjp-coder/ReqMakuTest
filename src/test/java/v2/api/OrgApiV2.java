package v2.api;

import io.restassured.response.Response;
import v2.core.ApiClient;

import java.util.Map;

public class OrgApiV2 {
    private final ApiClient client;

    public OrgApiV2(ApiClient client) {
        this.client = client;
    }

    public Response list(String token) {
        return client.get("/sys/org/list", token, null);
    }

    public Response info(String token, Integer id) {
        return client.get("/sys/org/" + id, token, null);
    }

    public Response save(String token, Map<String, Object> body) {
        return client.post("/sys/org", token, body);
    }

    public Response delete(String token, Integer id) {
        return client.delete("/sys/org/" + id, token, null);
    }
}
