package v2.core;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import v2.config.TestConfig;

import java.util.Map;

public class ApiClient {
    private final RequestSpecification baseSpec;

    public ApiClient(TestConfig config) {
        if (config.isLogOnValidationFailure()) {
            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        }
        this.baseSpec = new RequestSpecBuilder()
                .setBaseUri(config.getBaseUri())
                .setPort(config.getPort())
                .setContentType(ContentType.JSON)
                .build();
    }

    private RequestSpecification request(String path, String token) {
        RequestSpecification specification = RestAssured.given()
                .spec(baseSpec)
                .basePath(path);
        if (token != null && !token.trim().isEmpty()) {
            specification.header("Authorization", token);
        }
        return specification;
    }

    public Response get(String path, String token, Map<String, ?> queryParams) {
        RequestSpecification specification = request(path, token);
        if (queryParams != null && !queryParams.isEmpty()) {
            specification.queryParams(queryParams);
        }
        return specification.when().get().then().extract().response();
    }

    public Response post(String path, String token, Object body) {
        RequestSpecification specification = request(path, token);
        if (body != null) {
            specification.body(body);
        }
        return specification.when().post().then().extract().response();
    }

    public Response put(String path, String token, Object body) {
        RequestSpecification specification = request(path, token);
        if (body != null) {
            specification.body(body);
        }
        return specification.when().put().then().extract().response();
    }

    public Response delete(String path, String token, Object body) {
        RequestSpecification specification = request(path, token);
        if (body != null) {
            specification.body(body);
        }
        return specification.when().delete().then().extract().response();
    }
}
