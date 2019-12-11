package com.melon.mybatis.generator.config;

import com.melon.mybatis.generator.entity.LogicDelete;
import com.melon.mybatis.generator.enums.InjectionType;
import com.melon.mybatis.generator.enums.MybatisType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 生成配置
 *
 * @author melon
 * @version 1.0
 * @since 2019-10-31
 */
@Getter
@Setter
@Accessors(chain = true)
public class GeneratorConfig {
    /**
     * mybatis类型
     */
    private MybatisType mybatisType = MybatisType.SQL_BUILDER;

    /**
     * 允许的表
     */
    private String[] wishList;

    /**
     * 前缀
     */
    private String prefix;

    /**
     * 后缀
     */
    private String suffix = "";

    /**
     * 是否为lombok模型（默认 false）<br>
     */
    private boolean entityLombokModel = true;

    /**
     * 自定义继承的Entity类全称，带包名
     */
    private String superEntityClass;

    /**
     * 自定义继承的Dto类全称，带包名
     */
    private String superDtoClass;

    /**
     * 自定义继承的Utils类全称，带包名
     */
    private String dtoUtils;

    /**
     * 自定义QueryParam;类全称，带包名
     */
    private String superQueryParam;

    /**
     * 自定义SaveParam类全称，带包名
     */
    private String superSaveParam;

    /**
     * 自定义基础的Entity类，公共字段
     */
    @Setter(AccessLevel.NONE)
    private Set<String> superEntityColumns;

    /**
     * 自定义基础的Dto类，公共字段
     */
    @Setter(AccessLevel.NONE)
    private Set<String> superDtoColumns;

    /**
     * 逻辑删除属性名称
     */
    private LogicDelete logicDelete = new LogicDelete("yn", 1);

    /**
     * 创建线程数量
     */
    private int threadCount = 10;

    /**
     * Service等注入方式Resource
     */
    private InjectionType injection = InjectionType.RESOURCE;

    /**
     * 分片正则表达式
     */
    private String shardingPattern = "_\\d+$";

    /**
     * 作者
     */
    private String author = "melon";


    public GeneratorConfig setSuperEntityColumns(String... superEntityColumns) {
        this.superEntityColumns = new HashSet<>(Arrays.asList(superEntityColumns));
        return this;
    }

    public GeneratorConfig setSuperDtoColumns(String... superDtoColumns) {
        this.superDtoColumns = new HashSet<>(Arrays.asList(superDtoColumns));
        return this;
    }

}
