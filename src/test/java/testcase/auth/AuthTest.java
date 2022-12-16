package testcase.auth;

import api.Auth;

import base.BaseInterface;
import com.jayway.jsonpath.DocumentContext;
import io.qameta.allure.*;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import util.JsonPathUtils;

import java.util.Map;

import static base.BaseTest.token;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@Feature("认证管理模块")
public class AuthTest {
    private Auth auth;
    private final String DEFAULT_LOGIN_PATH = "/template/auth/login.json";

    @BeforeAll
    static void before() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Test
    @DisplayName("登录-正常用例")
    @Story("登录")
    void test01()  {
        Map<String,?> data = JsonPathUtils.jsonPathParseByFile(DEFAULT_LOGIN_PATH).json();
        new Auth().login(data).then().statusCode(200);
        token = BaseInterface.getToken();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/params/auth/falselogin.csv",numLinesToSkip = 1 )
    @DisplayName("登录-异常用例")
    @Story("登录")
    void test01_false01(String description,String username,String password)  {
        DocumentContext defaultData = JsonPathUtils.jsonPathParseByFile(DEFAULT_LOGIN_PATH);
        Allure.description(description + "登录");
        defaultData.set("$.username",username);
        defaultData.set("$.password",password);
        Map<String,?> data = defaultData.json();

        auth = new Auth();
        auth.login(data).then().statusCode(200).assertThat()
                .body("code",equalTo(500))
                .body("msg",equalTo("用户名或密码错误"));

        assertNull(auth.getResponse().path("data"));
    }
    
    @Test
    @DisplayName("登出-正常用例")
    @Story("登出")
    void test02() {
        Map<String,?> data = JsonPathUtils.jsonPathParseByFile(DEFAULT_LOGIN_PATH).json();
        auth = new Auth();
        auth.login(data).then().statusCode(200);
        auth.logout(token).then().statusCode(200).assertThat()
                .body("code",equalTo(0))
                .body("msg",equalTo("success"))
                .body("data",equalTo(null));
    }

    @Test
    @DisplayName("未授权访问-异常用例")
    @Story("登出")
    void test02_false01() {
        Map<String,?> data = JsonPathUtils.jsonPathParseByFile(DEFAULT_LOGIN_PATH).json();
        auth = new Auth();
        auth.login(data).then().statusCode(200);
        auth.logout("").then().assertThat()
                .statusCode(200)
                .body("code", equalTo(401))
                .body("msg", equalTo("还未授权，不能访问"))
                .body("data", emptyOrNullString());
    }


}
