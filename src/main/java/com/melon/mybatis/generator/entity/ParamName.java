package com.melon.mybatis.generator.entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author melon
 * @version 1.0
 * @since JDK1.8
 */

@Getter
@Setter
@AllArgsConstructor
public class ParamName {
    private String name;
    private String supperName;
    private String pkg;
}
