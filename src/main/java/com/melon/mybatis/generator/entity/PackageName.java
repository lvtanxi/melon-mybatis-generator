package com.melon.mybatis.generator.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author melon
 * @version 1.0
 * @since 2019-10-31
 */

@Getter
@Setter
@Accessors(chain = true)
public class PackageName {
    private String entityPackage;
    private String mapperPackage;
    private String paramPackage;
    private String servicePackage;
    private String serviceImplPackage;
    private String dtoPackage;
    private String controllerPackage;

    private String superQueryParamPackage;
    private String superDtoPackage;
    private String superDtoUtilsPackage;
    private String superSaveParamPackage;

    private String pagedAnnotationPackage;
    private String pagedInfoPackage;
}
