package com.melon.mybatis.generator.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 列的相关属性
 *
 * @author melon
 * @version 1.0
 * @since 2019-10-31
 */

@Getter
@Setter
@Accessors(chain = true)
public class ColumnData {
    /**
     * 列名
     */
    private String columnName;
    /**
     * 列类型
     */
    private String columnType;
    /**
     * 列类型
     */
    private String columnRemark;
    /**
     * 转后后java属性
     */
    private String filedName;

    /**
     * 转后后java属性
     */
    private String filedType;

    /**
     * 是否是主键
     */
    private boolean pkColumn;

    /**
     * 逻辑删除
     */
    private boolean logicDelete;
}
