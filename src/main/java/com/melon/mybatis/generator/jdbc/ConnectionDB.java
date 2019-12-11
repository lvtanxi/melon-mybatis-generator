package com.melon.mybatis.generator.jdbc;


import com.melon.mybatis.generator.config.DataSourceConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 数据库连接类
 */
public class ConnectionDB {


    public static Connection getConnection(DataSourceConfig dataSourceConfig) {
        try {
            // 获取连接
            Class.forName(dataSourceConfig.getDriverName());
            Properties props = new Properties();
            props.setProperty("user", dataSourceConfig.getUsername());
            props.setProperty("password", dataSourceConfig.getPassword());
            props.setProperty("remarks", "true"); //设置可以获取remarks信息
            props.setProperty("useInformationSchema", "true");//设置可以获取tables remarks信息
            return DriverManager.getConnection(dataSourceConfig.getUrl(), props);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 关闭所有资源
     */
    public static void closeAll(Connection connection, ResultSet... resultSets) {
        // 关闭结果集对象
        for (ResultSet resultSet : resultSets) {
            if (resultSet == null)
                continue;
            try {
                resultSet.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        // 关闭Connection 对象
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}  