package v2.tests;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import v2.core.BaseApiTestV2;
import v2.util.JsonResource;

import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Feature("V2 API Framework")
public class UserApiV2Test extends BaseApiTestV2 {

    @Test
    @DisplayName("V2-用户分页查询")
    @Story("User")
    void page_should_success_with_valid_token() {
        String token = session.loginIfNeeded();
        Map<String, Object> query = JsonResource.readAsMap("v2/payloads/user-page.json");
        Response response = userApi.page(token, query);
        response.then()
                .statusCode(200)
                .body("code", equalTo(0))
                .body("msg", equalTo("success"))
                .body("data", notNullValue());
    }

    @Test
    @DisplayName("V2-用户分页查询-未授权")
    @Story("User")
    void page_should_fail_without_token() {
        Map<String, Object> query = JsonResource.readAsMap("v2/payloads/user-page.json");
        Response response = userApi.page("", query);
        response.then()
                .statusCode(200)
                .body("code", equalTo(401));
    }
}
