package com.melon.mybatis.generator.config;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 数据库配置
 *
 * @author melon
 * @version 1.0
 * @since 2019-10-31
 */

@Getter
@Setter
@Accessors(chain = true)
public class DataSourceConfig {
    /**
     * 数据库加载驱动，暂时只支持mysql
     */
    private String driverName = "com.mysql.cj.jdbc.Driver";
    /**
     * 连接地址
     */
    private String url;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;

}
