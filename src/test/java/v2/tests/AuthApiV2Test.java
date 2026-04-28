package v2.tests;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import v2.core.BaseApiTestV2;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.emptyOrNullString;

@Feature("V2 API Framework")
public class AuthApiV2Test extends BaseApiTestV2 {

    @Test
    @DisplayName("V2-登录成功并获取Token")
    @Story("Auth")
    void login_should_success() {
        Response response = authApi.login(config.getUsername(), config.getPassword());
        response.then()
                .statusCode(200)
                .body("code", equalTo(0))
                .body("msg", equalTo("success"))
                .body("data.access_token", not(emptyOrNullString()));
    }

    @Test
    @DisplayName("V2-未授权登出")
    @Story("Auth")
    void logout_should_fail_when_token_missing() {
        Response response = authApi.logout("");
        response.then()
                .statusCode(200)
                .body("code", equalTo(401));
    }
}
