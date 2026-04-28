package v2.core;

import v2.api.AuthApiV2;
import v2.config.TestConfig;

public class AuthSession {
    private final AuthApiV2 authApi;
    private final TestConfig config;
    private String accessToken;

    public AuthSession(AuthApiV2 authApi, TestConfig config) {
        this.authApi = authApi;
        this.config = config;
    }

    public String login() {
        this.accessToken = authApi.loginAndGetToken(config.getUsername(), config.getPassword());
        return accessToken;
    }

    public String loginIfNeeded() {
        if (accessToken == null || accessToken.trim().isEmpty()) {
            return login();
        }
        return accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
