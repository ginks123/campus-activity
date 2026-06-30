package com.campus.util;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {

    private static DruidDataSource dataSource;

    static {
        try {
            InputStream in = DBUtil.class.getClassLoader().getResourceAsStream("db.properties");
            Properties props = new Properties();
            props.load(in);

            // 环境变量优先（云端部署用），没有则用 db.properties（本地开发用）
            String envUrl = System.getenv("DB_URL");
            if (envUrl != null && !envUrl.isEmpty()) {
                // 确保 URL 包含 TiDB 需要的 SSL 参数
                if (!envUrl.contains("sslMode")) {
                    envUrl += "&sslMode=REQUIRED&allowPublicKeyRetrieval=true";
                }
                props.setProperty("url", envUrl);
            }
            String envUser = System.getenv("DB_USER");
            if (envUser != null && !envUser.isEmpty()) {
                props.setProperty("username", envUser);
            }
            String envPass = System.getenv("DB_PASSWORD");
            if (envPass != null && !envPass.isEmpty()) {
                props.setProperty("password", envPass);
            }

            dataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(props);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

}
