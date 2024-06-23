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
    public static String token = "";//测试类的全局token
    public String RESOURCES_PATH = "src/test/resources";//资源根路径
    public String JSONSCHEMA_PATH = "jsonschema";//由于matchesJsonSchemaInClasspath方法里有getResource方法，就不带Resources路径了
    //默认模板参数
    protected DocumentContext defaultData;
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
        jdbcTemplate.update("delete from sys_user where username != 'admin'");
    }

    @BeforeEach
    void setCurrentTime(){
        currentTime = FakerUtil.getNowTimeStr("yyyy-MM-dd HH:mm:ss");
    }

    @AfterAll
    void tearDown(){
        //重置数据库中所测试的数据
        jdbcTemplate.update("delete from sys_org");
        jdbcTemplate.update("delete from sys_post");
        jdbcTemplate.update("delete from sys_role");
        jdbcTemplate.update("delete from sys_user where username != 'admin'");
    }


    //JdbcTemplate省去连接数据库对象和关闭数据库步骤
}
