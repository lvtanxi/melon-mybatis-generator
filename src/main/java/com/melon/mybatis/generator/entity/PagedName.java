package com.melon.mybatis.generator.entity;

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
public class PagedName {

    private boolean paged;

    /**
     * 分页注解名称
     */
    private String pagedAnnotationName;
    /**
     * 分页返回实体名称
     */
    private String pagedInfoName;
}
