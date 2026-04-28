package com.maku.apitest.config;

/*
 * 职责：统一环境配置加载，支持 -Denv=dev/test/prod 切换
 *
 * 核心概念：
 * - 饿汉式单例：static final INSTANCE 在类加载时立即初始化，整个测试进程只加载一次配置文件
 * - 读取优先级：系统属性(-Dkey=val) > 环境变量(KEY) > properties 文件 > 默认值
 *   这样既能在 IDE 中用 properties 文件开发，又能在 CI 环境通过环境变量注入敏感信息
 * - 加载路径：classpath:config/<env>.properties，env 默认 "test"，可通过 -Denv=prod 切换
 *
 * // v1 改进：v1 在 BaseTest.setAll() 里硬编码 "http://localhost" 和 8080，
 * //          换个环境就要改代码，v3 改为配置文件驱动，通过 -Denv 无感切换。
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class Env {

    // 饿汉式单例：JVM 类加载时就完成初始化，线程安全且无锁开销
    private static final Env INSTANCE = load();

    private final String baseUrl;
    private final int port;
    private final String dbUrl;
    private final String dbUsername;
    private final String dbPassword;
    private final String authUsername;
    private final String authPassword;

    private Env(Properties props) {
        this.baseUrl      = resolve(props, "baseUrl",      "http://localhost");
        this.port         = Integer.parseInt(resolve(props, "port", "8080"));
        this.dbUrl        = resolve(props, "dbUrl",        "jdbc:mysql://127.0.0.1:3306/maku_boot");
        this.dbUsername   = resolve(props, "dbUsername",   "root");
        this.dbPassword   = resolve(props, "dbPassword",   "root");
        this.authUsername = resolve(props, "authUsername", "admin");
        this.authPassword = resolve(props, "authPassword", "admin");
    }

    // 加载配置文件，找不到时静默降级到全默认值（方便 IDE 首次运行）
    private static Env load() {
        String env = System.getProperty("env", "test");
        String classpathPath = "config/" + env + ".properties";
        Properties props = new Properties();
        try (InputStream is = Env.class.getClassLoader().getResourceAsStream(classpathPath)) {
            if (is != null) {
                props.load(new InputStreamReader(is, StandardCharsets.UTF_8));
            }
        } catch (IOException ignored) {
            // 找不到配置文件时使用硬编码默认值，不抛异常，方便快速试跑
        }
        return new Env(props);
    }

    /*
     * 三级回退：系统属性 > 环境变量 > properties 文件 > 默认值
     * 为什么要环境变量支持：CI 流水线（Jenkins/GitHub Actions）通常用环境变量传密码，
     * 不会把密码写进 properties 文件提交到 git。
     */
    private static String resolve(Properties props, String key, String defaultValue) {
        String sysProp = System.getProperty(key);
        if (sysProp != null && !sysProp.isBlank()) return sysProp;

        String envVar = System.getenv(key.toUpperCase());
        if (envVar != null && !envVar.isBlank()) return envVar;

        String fileProp = props.getProperty(key);
        if (fileProp != null && !fileProp.isBlank()) return fileProp;

        return defaultValue;
    }

    /** 获取全局单例实例 */
    public static Env get() {
        return INSTANCE;
    }

    public String getBaseUrl()      { return baseUrl; }
    public int    getPort()         { return port; }
    public String getDbUrl()        { return dbUrl; }
    public String getDbUsername()   { return dbUsername; }
    public String getDbPassword()   { return dbPassword; }
    public String getAuthUsername() { return authUsername; }
    public String getAuthPassword() { return authPassword; }
}
