package v2.core;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import v2.api.AuthApiV2;
import v2.api.OrgApiV2;
import v2.api.UserApiV2;
import v2.config.TestConfig;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseApiTestV2 {
    protected TestConfig config;
    protected ApiClient client;
    protected AuthApiV2 authApi;
    protected OrgApiV2 orgApi;
    protected UserApiV2 userApi;
    protected AuthSession session;

    @BeforeAll
    void beforeAllV2() {
        config = TestConfig.load();
        client = new ApiClient(config);
        authApi = new AuthApiV2(client);
        orgApi = new OrgApiV2(client);
        userApi = new UserApiV2(client);
        session = new AuthSession(authApi, config);
    }
}
