package testcase.role;

import api.Role;
import base.BaseTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import util.JsonPathUtils;

/**
 * 角色用户
 * 测试完用户管理再回来测试相关接口
 * */
@Deprecated
@Feature("角色管理模块")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RoleUserTest extends BaseTest {
    private Integer id = 1;
    private Role role = new Role();

    //前置数据
    //角色，用户
    @BeforeAll
    void setUp() {
        //添加20条角色数据
        for (int i=1;i<=20;i++) {
            defaultData = JsonPathUtils.jsonPathParseByFile("/template/role/save.json");
            defaultData.set("$.id", i);
            role.save(token,defaultData.json());
        }

        //添加20条用户数据



    }


    @Story("数据权限")
    @Test
    void test01(){

    }


    @Story("分配角色给用户列表")
    @Test
    void test02(){

    }

    @Story("删除角色用户")
    @Test
    void test03(){

    }


    @Story("角色用户-分页")
    @Test
    void test04(){

    }



}
