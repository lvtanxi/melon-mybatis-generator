package com.melon.mybatis.generator;

import com.melon.mybatis.generator.config.*;
import com.melon.mybatis.generator.core.GeneratorCode;
import com.melon.mybatis.generator.enums.MybatisType;
import lombok.var;

/**
 * 自动生成主类
 *
 * @author melon
 * @version 1.0
 * @since 2019-10-31
 */
public class AutoGenerator {
    private final static String PACKAGE_NAME = "com.demo.mx";
    private final static String DB_URL = "jdbc:mysql://10.248.224.12:21202/xs_settlement?useUnicode=true&characterEncoding=utf-8&useSSL=true";
    private final static String DB_USER_NAME = "scm";
    private final static String DB_PASSWORD = "3AiZXRGBIFE9V18";
    private final static String OUTPUT_DIR = "C://Users//melon//Desktop//springboot-log";


    public static void main(String[] args) {
        String tables = "cx_pay_bill";
        generateByTables(tables.split(","));
    }


    private static void generateByTables(String... tableNames) {
        var dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl(DB_URL)
                .setUsername(DB_USER_NAME)
                .setPassword(DB_PASSWORD);

        var generatorConfig = new GeneratorConfig()
                .setAuthor("melon")
                .setSuffix("DO")
                .setMybatisType(MybatisType.SQL_BUILDER)
                .setEntityLombokModel(true)
                .setSuperEntityClass("com.demo.mx.domain.entity.BaseDO")
                .setSuperQueryParam("com.demo.mx.domain.param.PageQuery")
                .setSuperEntityColumns("id", "create_time", "update_time","yn")
                .setSuperDtoClass("com.demo.mx.domain.dto.BaseDTO")
                .setSuperDtoColumns("id")
                .setDtoUtils("com.demo.mx.infr.utils.DTOUtils")
                .setWishList(tableNames);

        var packageConfig = new PackageConfig()
                .setEntity("domain.entity")
                .setService("service.beans")
                .setServiceImpl("service.beans.impl")
                .setDto("domain.dto")
                .setParam("domain.param")
                .setOutputDir(OUTPUT_DIR)
                .setParent(PACKAGE_NAME);

        var cabinConfig = new CabinConfig()
                .setGlobalName("SlmGlobal")
                .setCabinPackage("supplier_slm");

        var pagedConfig = new PagedConfig()
                .setPagedInfo("com.demo.mx.infr.paged.PagedInfo")
                .setPagedAnnotation("com.demo.mx.infr.paged.Paged");
        new GeneratorCode()
                .setDataSourceConfig(dataSourceConfig)
                .setGeneratorConfig(generatorConfig)
                .setPackageConfig(packageConfig)
                .setCabinConfig(cabinConfig)
                .setPagedConfig(pagedConfig)
                .execute();
    }


}
