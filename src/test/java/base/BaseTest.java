package base;

import api.Auth;
import com.jayway.jsonpath.DocumentContext;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.springframework.jdbc.core.JdbcTemplate;
import util.FakerUtil;
import util.JDBCUtils;
import util.JsonPathUtils;
import util.ParamsUtil;

/**
 * 基础测试类
 * */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseTest {
    public static String token = "";
    //资源根路径
    public String RESOURCES_PATH = "src/test/resources";
    public String DEFAULT_PATH = "/template";//模板路径
    public String PARAMS_PATH = "/params";//参数路径

    //由于matchesJsonSchemaInClasspath方法有getResource方法，就不带Resources路径了
    public String JSONSCHEMA_PATH = "jsonschema";

    //默认模板参数
    protected DocumentContext defaultData;

    //JdbcTemplate省去连接数据库对象和关闭数据库步骤
    protected JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());

    protected String currentTime;
    @BeforeAll
    void setAll(){
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;

        //登录
        new Auth().login(JsonPathUtils.jsonPathParseByFile("/template/auth/login.json").json());
        BaseTest.token = BaseInterface.getToken();

        //重置数据库中所测试的数据
        jdbcTemplate.update("delete from sys_org");
        jdbcTemplate.update("delete from sys_post");
        jdbcTemplate.update("delete from sys_role");
//        jdbcTemplate.update("delete from sys_role_menu");

    }

    @BeforeEach
    void setCurrentTime(){
        currentTime = FakerUtil.getNowTimeStr("yyyy-MM-dd HH:mm:ss");
    }

    @AfterEach
    void afterToken(){
        //还原token状态
        token = BaseInterface.getToken();
    }

    @AfterAll
    void tearDown(){
        //重置数据库中所测试的数据
        jdbcTemplate.update("delete from sys_org");
        jdbcTemplate.update("delete from sys_post");
        jdbcTemplate.update("delete from sys_role");
//        jdbcTemplate.update("delete from sys_role_menu");

    }



}
