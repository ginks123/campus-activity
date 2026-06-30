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

            String envUrl = System.getenv("DB_URL");
            if (envUrl != null && !envUrl.isEmpty()) {
                props.setProperty("url", envUrl);
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
            }
        } catch (Exception e) {
            throw new RuntimeException("DBUtil init failed: " + e.getMessage(), e);
        }
    }

    public static Connection getConnection() throws SQLException {
        if (usePool) {
            return dataSource.getConnection();
        }
        try {
            com.mysql.cj.jdbc.Driver driver = new com.mysql.cj.jdbc.Driver();
            Properties info = new Properties();
            info.setProperty("user", username);
            info.setProperty("password", password);
            // TiDB Cloud 需要 SSL
            info.setProperty("sslMode", "REQUIRED");
            info.setProperty("allowPublicKeyRetrieval", "true");
            Connection conn = driver.connect(url, info);
            if (conn == null) {
                throw new SQLException("Driver.connect returned null, URL not accepted: " + url);
            }
            return conn;
        } catch (SQLException e) {
            throw e;
        } catch (Exception e) {
            throw new SQLException("Failed to create connection: " + e.getMessage(), e);
        }
    }
}
