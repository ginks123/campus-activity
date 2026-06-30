package com.campus.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {

    private static String url;
    private static String username;
    private static String password;
    private static com.alibaba.druid.pool.DruidDataSource dataSource;
    private static boolean usePool = true;

    static {
        try {
            InputStream in = DBUtil.class.getClassLoader().getResourceAsStream("db.properties");
            Properties props = new Properties();
            props.load(in);

            // 环境变量优先（云端部署用），没有则用 db.properties（本地开发用）
            String envUrl = System.getenv("DB_URL");
            if (envUrl != null && !envUrl.isEmpty()) {
                // 清除旧版 MySQL SSL 参数，替换为兼容 TiDB Cloud 的配置
                envUrl = envUrl
                    .replaceAll("[&?]useSSL=(true|false)", "")
                    .replaceAll("[&?]requireSSL=(true|false)", "");
                envUrl += "&sslMode=REQUIRED&allowPublicKeyRetrieval=true&enabledTLSProtocols=TLSv1.2,TLSv1.3";
                props.setProperty("url", envUrl);
                // 云端不使用连接池，避免 Druid 初始化问题
                usePool = false;
            }
            String envUser = System.getenv("DB_USER");
            if (envUser != null && !envUser.isEmpty()) {
                props.setProperty("username", envUser);
            }
            String envPass = System.getenv("DB_PASSWORD");
            if (envPass != null && !envPass.isEmpty()) {
                props.setProperty("password", envPass);
            }

            url = props.getProperty("url");
            username = props.getProperty("username");
            password = props.getProperty("password");

            if (usePool) {
                Class.forName(props.getProperty("driverClassName"));
                dataSource = new com.alibaba.druid.pool.DruidDataSource();
                dataSource.setUrl(url);
                dataSource.setUsername(username);
                dataSource.setPassword(password);
                dataSource.setInitialSize(Integer.parseInt(props.getProperty("initialSize", "5")));
                dataSource.setMaxActive(Integer.parseInt(props.getProperty("maxActive", "20")));
                dataSource.setMaxWait(Integer.parseInt(props.getProperty("maxWait", "3000")));
                dataSource.init();
            } else {
                // Tomcat webapp 类加载器下 DriverManager 找不到驱动，直接用 Driver 实例
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        if (usePool) {
            return dataSource.getConnection();
        }
        try {
            java.sql.Driver driver = new com.mysql.cj.jdbc.Driver();
            java.util.Properties info = new java.util.Properties();
            info.setProperty("user", username);
            info.setProperty("password", password);
            return driver.connect(url, info);
        } catch (SQLException e) {
            throw e;
        } catch (Exception e) {
            throw new SQLException("Failed to create DB connection", e);
        }
    }

}
