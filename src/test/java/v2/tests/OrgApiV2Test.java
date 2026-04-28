package v2.tests;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import v2.core.BaseApiTestV2;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Feature("V2 API Framework")
public class OrgApiV2Test extends BaseApiTestV2 {

    @Test
    @DisplayName("V2-组织列表-鉴权成功")
    @Story("Org")
    void list_should_success_with_valid_token() {
        String token = session.loginIfNeeded();
        Response response = orgApi.list(token);
        response.then()
                .statusCode(200)
                .body("code", equalTo(0))
                .body("msg", equalTo("success"))
                .body("data", notNullValue());
    }

    @Test
    @DisplayName("V2-组织列表-未授权")
    @Story("Org")
    void list_should_fail_without_token() {
        Response response = orgApi.list("");
        response.then()
                .statusCode(200)
                .body("code", equalTo(401));
    }
}
