package testcase.attachment;

import api.Attachment;
import api.UpLoad;
import base.BaseTest;
import com.jayway.jsonpath.JsonPath;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import util.JsonPathUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;

@Feature("附件管理模块")
@Story("删除")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeleteTest extends BaseTest {
    private Attachment attachment = new Attachment();
    private UpLoad upLoad = new UpLoad();
    private Integer id = 1;

    @BeforeAll
    void setUp(){
        defaultData = JsonPathUtils.jsonPathParseByFile("/template/attachment/save.json");
        //准备一些数据
        String path = RESOURCES_PATH+"/params/file/test.xlsx";
        File file = new File(path);

        upLoad.upload(token,file);
        String resUpLoad = upLoad.getResponse().asString();
        String filename = JsonPath.read(resUpLoad,"$.data.name");
        String url = JsonPath.read(resUpLoad,"$.data.url");
        Integer size = JsonPath.read(resUpLoad,"$.data.size");
        String platform = JsonPath.read(resUpLoad,"$.data.platform");

        defaultData.set("$.id",id);
        defaultData.set("$.name",filename);
        defaultData.set("$.url",url);
        defaultData.set("$.size",size);
        defaultData.set("$.platform",platform);
        attachment.save(token, defaultData.json());
    }

    @Test
    @DisplayName("删除用例")
    void test01(){
        List<Integer> list = new ArrayList<>();
        list.add(id);
        attachment.delete(token,list).then().assertThat().statusCode(200)
                .body("code", equalTo(0))
                .body("msg", equalTo("success"))
                .body("data", emptyOrNullString());
    }

    @Test
    @DisplayName("未授权访问")
    void test01_false01(){
        List<Integer> list = new ArrayList<>();
        list.add(id);
        attachment.delete("",list).then().assertThat().statusCode(200)
                .body("code", equalTo(401))
                .body("msg", equalTo("还未授权，不能访问"))
                .body("data", emptyOrNullString());
    }


}
