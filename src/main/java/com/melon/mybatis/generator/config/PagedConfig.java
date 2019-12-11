package com.melon.mybatis.generator.config;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 分页信息
 *
 * @author melon
 * @version 1.0
 * @since JDK1.8
 */
@Getter
@Setter
@Accessors(chain = true)
public class PagedConfig {
    /**
     * 分页注解全路径
     */
    private String pagedAnnotation;
    /**
     * 分页返回实体全路径
     */
    private String pagedInfo;
}
