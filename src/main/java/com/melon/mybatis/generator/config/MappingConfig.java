package com.melon.mybatis.generator.config;

import com.melon.mybatis.generator.enums.ConstVal;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * @author melon
 * @version 1.0
 * @since 2019-10-31
 */
public class MappingConfig {
    private static final Properties MYSQL_JAVA_MAPPING = new Properties();
    static {
        try {
            FileInputStream fis = new FileInputStream(ConstVal.FILE_ROOT+"mysql-java-mapping.properties");
            MYSQL_JAVA_MAPPING.load(fis);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getJavaMapping(String columnType) {
        return MYSQL_JAVA_MAPPING.getProperty(columnType);
    }
}
