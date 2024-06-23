package testcase.org;

import api.Auth;
import api.Org;
import base.BaseInterface;
import base.BaseTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.jsonpath.JsonPath;
import io.qameta.allure.Allure;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import util.JsonPathUtils;
import util.ParamsUtil;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * 修改接口测试
 * */
@Feature("机构管理模块")
@Story("修改")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PutTest extends BaseTest{
    private Integer putID = 1;
    private Org org = new Org();
    @BeforeAll
    void setUp(){
    }

    @BeforeEach
    void readDefault(){
        putID++;
        //添加对应的机构
        defaultData = JsonPathUtils.jsonPathParseByFile("/template/org/save.json");
        defaultData.set("$.id",putID);
        defaultData.set("$.createTime",currentTime);
        new Org().save(token,defaultData.json());
        //装载模板数据
        defaultData = JsonPathUtils.jsonPathParseByFile("/template/org/put.json");
        defaultData.set("$.id",putID);
    }

    @AfterEach
    void delete(){
        //每个修改后都进行删除
        org.delete(token,putID)
                .then().statusCode(200);
    }

    @Test
    @DisplayName("不带子机构修改")
    void test01(){
        org.put(token,defaultData.json()).then().assertThat()
                .statusCode(200)
                .body("code",equalTo(0))
                .body("msg",equalTo("success"))
                .body("data",emptyOrNullString());

        //查询列表断言是否有对应id
        if(putID != null){
            String body = org.list(token).getBody().asString();
            List<?> idList = JsonPath.read(body,"$.."+ "id");
            assertThat(idList.toArray(),hasItemInArray(putID));
        }
    }

    @SneakyThrows
    @Test
    @DisplayName("带子机构修改")
    void test01_01() throws JsonProcessingException {
        //放入子机构
        String data = defaultData.jsonString();
        LinkedHashMap<String,Object> children = defaultData.json();
        List<LinkedHashMap<String,Object>> list = new ArrayList<>();
        list.add(children);
        Map<String,Object> map = ParamsUtil.strToMap(data);
        map.put("children",list);
        //断言
        org.put(token,map).then().assertThat()
                .statusCode(200)
                .body("code",equalTo(0))
                .body("msg",equalTo("success"))
                .body("data",emptyOrNullString());

        //查询列表断言是否有对应id
        if(putID != null){
            String body = org.list(token).getBody().asString();
            List<?> idList = JsonPath.read(body,"$.."+ "id");
            assertThat(idList.toArray(),hasItemInArray(putID));
        }
    }

    @ParameterizedTest
    @CsvSource({
            "id",
            "children",
            "createTime",
            "parentName"
    })
    @DisplayName("删除非必填项-异常用例")
    void test01_false01(String key){
        Allure.description("删除非必填项" + key + "字段");
        String jsonPathKey = "$." + key;
        //删除key
        defaultData.delete(jsonPathKey);
        org.put(token,defaultData.json()).then().assertThat()
                .statusCode(200)
                .body("code",equalTo(0))
                .body("msg",equalTo("success"))
                .body("data",emptyOrNullString());

        //查询列表断言是否包含对应id
        if(putID != null && !key.equals("id")){
            String body = org.list(token).getBody().asString();
            List<?> idList = JsonPath.read(body,"$.."+ "id");
            assertThat(idList.toArray(),hasItemInArray(putID));
        }

    }

    @ParameterizedTest
    @CsvSource({
            "pid,上级ID不能为空",
            "name,机构名称不能为空",
            "sort,服务器异常，请稍后再试"
    })
    @DisplayName("删除必填项-异常用例")
    void test01_false02(String key,String expectMsg) {
        Allure.description("删除必填项" + key + "字段");
        //删除key
        defaultData.delete("$." + key);
        org.put(token, defaultData.json()).then().assertThat()
                .statusCode(200)
                .body("code", equalTo(500))
                .body("msg", equalTo(expectMsg))
                .body("data", emptyOrNullString());
    }

    @Test
    @DisplayName("参数全部为空-异常用例")
    void test03_false03(){
        org.save(token,new HashMap()).then().assertThat()
                .statusCode(200)
                .body("code",equalTo(500))
                .body("msg",equalTo("机构名称不能为空"))
                .body("data",emptyOrNullString());
    }

    @Test
    @DisplayName("未授权访问-异常用例")
    void test01_false04(){
        org.put("",defaultData.json()).then().assertThat()
                .statusCode(200)
                .body("code",equalTo(401))
                .body("msg",equalTo("还未授权，不能访问"))
                .body("data",emptyOrNullString());
    }
}
