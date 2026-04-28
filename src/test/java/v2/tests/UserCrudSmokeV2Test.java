package v2.tests;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import v2.core.BaseApiTestV2;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Feature("V2 API Framework")
public class UserCrudSmokeV2Test extends BaseApiTestV2 {

    @Test
    @DisplayName("V2-用户新增与删除冒烟")
    @Story("User")
    void user_crud_smoke() {
        String token = session.loginIfNeeded();
        int uniqueId = (int) (System.currentTimeMillis() / 1000L);
        int orgId = uniqueId;
        int userId = uniqueId;
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        Map<String, Object> orgBody = new HashMap<String, Object>();
        orgBody.put("name", "v2-org-" + orgId);
        orgBody.put("pid", 0);
        orgBody.put("sort", 1);
        orgBody.put("children", new ArrayList<Object>());
        orgBody.put("id", orgId);
        orgBody.put("createTime", now);
        orgBody.put("parentName", "");

        Map<String, Object> userBody = new HashMap<String, Object>();
        userBody.put("id", userId);
        userBody.put("username", "v2user_" + userId);
        userBody.put("realName", "v2-user");
        userBody.put("avatar", "");
        userBody.put("orgId", orgId);
        userBody.put("password", "123456");
        userBody.put("gender", 0);
        userBody.put("email", "");
        userBody.put("mobile", "13" + String.valueOf(userId).substring(0, 9));
        userBody.put("roleIdList", new ArrayList<Integer>());
        userBody.put("postIdList", new ArrayList<Integer>());
        userBody.put("status", 1);
        userBody.put("superAdmin", 0);
        userBody.put("orgName", "v2-org-" + orgId);
        userBody.put("createTime", now);

        Response saveOrgResponse = orgApi.save(token, orgBody);
        saveOrgResponse.then()
                .statusCode(200)
                .body("code", equalTo(0));

        try {
            Response saveUserResponse = userApi.save(token, userBody);
            saveUserResponse.then()
                    .statusCode(200)
                    .body("code", equalTo(0));

            Response infoResponse = userApi.info(token, userId);
            infoResponse.then()
                    .statusCode(200)
                    .body("code", equalTo(0))
                    .body("data.id", equalTo(userId))
                    .body("data.username", equalTo("v2user_" + userId))
                    .body("data", notNullValue());
        } finally {
            List<Integer> ids = new ArrayList<Integer>();
            ids.add(userId);
            userApi.delete(token, ids);
            orgApi.delete(token, orgId);
        }
    }
}
