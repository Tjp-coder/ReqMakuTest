package testcase.user;

import api.User;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.TestInstance;

@Feature("用户管理模块")
@Story("保存")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SaveTest {
    private User user = new User();




}
