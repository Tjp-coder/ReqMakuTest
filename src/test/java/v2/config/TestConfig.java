package v2.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class TestConfig {
    private final String baseUri;
    private final int port;
    private final String username;
    private final String password;
    private final boolean logOnValidationFailure;

    public TestConfig(String baseUri, int port, String username, String password, boolean logOnValidationFailure) {
        this.baseUri = baseUri;
        this.port = port;
        this.username = username;
        this.password = password;
        this.logOnValidationFailure = logOnValidationFailure;
    }

    public static TestConfig load() {
        Properties properties = loadProperties("v2/config.properties");
        String baseUri = read("v2.baseUri", "V2_BASE_URI", properties, "http://localhost");
        int port = Integer.parseInt(read("v2.port", "V2_PORT", properties, "8080"));
        String username = read("v2.username", "V2_USERNAME", properties, "admin");
        String password = read("v2.password", "V2_PASSWORD", properties, "admin");
        boolean logOnValidationFailure = Boolean.parseBoolean(
                read("v2.logOnValidationFailure", "V2_LOG_ON_VALIDATION_FAILURE", properties, "true")
        );
        return new TestConfig(baseUri, port, username, password, logOnValidationFailure);
    }

    private static Properties loadProperties(String classpathFile) {
        Properties properties = new Properties();
        try (InputStream inputStream = TestConfig.class.getClassLoader().getResourceAsStream(classpathFile)) {
            if (inputStream != null) {
                properties.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            }
        } catch (IOException ignored) {
        }
        return properties;
    }

    private static String read(String key, String envKey, Properties properties, String defaultValue) {
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.trim().isEmpty()) {
            return systemValue.trim();
        }

        String envValue = System.getenv(envKey);
        if (envValue != null && !envValue.trim().isEmpty()) {
            return envValue.trim();
        }

        String propertyValue = properties.getProperty(key);
        if (propertyValue != null && !propertyValue.trim().isEmpty()) {
            return propertyValue.trim();
        }

        return defaultValue;
    }

    public String getBaseUri() {
        return baseUri;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isLogOnValidationFailure() {
        return logOnValidationFailure;
    }
}
