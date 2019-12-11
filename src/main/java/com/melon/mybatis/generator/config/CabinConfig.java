package com.melon.mybatis.generator.config;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 前端配置
 *
 * @author melon
 * @version 1.0
 * @since JDK1.8
 */

@Getter
@Setter
@Accessors(chain = true)
public class CabinConfig {

    /**
     * 前端包名
     */
    private String cabinPackage;
    /**
     * 全局配置名字
     */
    private String globalName = "Global";

    /**
     * 表格多选
     */
    private boolean tableMultiple;



}
