package com.melon.mybatis.generator.enums;

import lombok.Getter;

/**
 * @author melon
 * @version 1.0
 * @since JDK1.8
 */
@Getter
public enum InjectionType {
    /**
     * Autowired
     */
    AUTOWIRED("Autowired"),
    /**
     * Resource
     */
    RESOURCE("Resource");

    private final String type;

    InjectionType(String type) {
        this.type = type;
    }
}
