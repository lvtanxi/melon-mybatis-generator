package com.melon.mybatis.generator.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 逻辑删除的值
 *
 * @author melon
 * @version 1.0
 * @since JDK1.8
 */
@Getter
@Setter
public class LogicDelete {
    private String logicDeleteFieldName;
    private Object freeze = 1;

    public LogicDelete(String logicDeleteFieldName) {
        this.logicDeleteFieldName = logicDeleteFieldName;
    }

    public LogicDelete(String logicDeleteFieldName, Object freeze) {
        this.logicDeleteFieldName = logicDeleteFieldName;
        this.freeze = freeze;
    }
}
