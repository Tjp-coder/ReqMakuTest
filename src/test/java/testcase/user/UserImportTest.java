package testcase.user;

import api.Auth;
import api.Org;
import api.User;
import base.BaseTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import util.FakerUtil;
import util.JsonPathUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;

@Feature("用户管理模块")
public class UserImportTest extends BaseTest {
    private User user = new User();

    private final Integer orgId = 1;
    private Integer id = 0;
    private List<Integer> integers = new ArrayList();

    @BeforeAll
    void setUp() {
        //准备前置数据
        //准备一条id为1的机构数据
        defaultData = JsonPathUtils.jsonPathParseByFile("/template/org/save.json");
        new Org().save(token, defaultData.set("$.id", orgId).json());

        //添加5个用户账号
        defaultData = JsonPathUtils.jsonPathParseByFile("/template/user/save.json");
        for (int i = 1; i <= 5; i++) {
            defaultData.set("$.id", i);
            defaultData.set("$.orgId", orgId);
            defaultData.set("$.username", "username" + i);
            defaultData.set("$.mobile", FakerUtil.getTel());
            user.save(token, defaultData.json());
        }
    }

    @BeforeEach
    void readDefault() {
        id++;
        integers.add(id);
    }

    @AfterAll
    void delete() {
        user.delete(token, integers)
                .then().statusCode(200);
    }

    @Test
    @DisplayName("导入用户-xlsx格式")
    @Story("导入用户")
    void test02() {
        File file = new File(RESOURCES_PATH+"/params/user/system_user_excel2022-12-19.xlsx");
        user.importUser(token,file).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(0))
                .body("msg", equalTo("success"))
                .body("data", emptyOrNullString());
    }

    @Test
    @DisplayName("导入用户-xls格式")
    @Story("导入用户")
    void test02_01() {
        File file = new File(RESOURCES_PATH+ "/params/user/system_user_excel2022-12-19 - 副本.xls");
        user.importUser(token,file).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(0))
                .body("msg", equalTo("success"))
                .body("data", emptyOrNullString());
    }

    @Test
    @DisplayName("导入用户-非xlsx或xls文件")
    @Story("导入用户")
    void test02_false01() {
        File file = new File(RESOURCES_PATH+"/params/user/system_user.txt");
        user.importUser(token,file).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(500))
                .body("msg", equalTo("请选择需要上传的文件"))
                .body("data", emptyOrNullString());
    }

}
