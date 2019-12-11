package ${packages.mapperPackage};

<#if plus>
import ${packages.entityPackage}.${entityNameWithSuffix};
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
<#else>
import ${packages.entityPackage}.${entityNameWithSuffix};
import com.melon.sql.mapper.BaseMapper;
</#if>
<#assign uncapEntityName= entityName?uncap_first/>

/**
 * ${tableRemark}Mapper
 *
 * @author melon
 * @version 1.0
 * @since JDK1.8
 */
public interface ${entityName}Mapper extends BaseMapper<${entityNameWithSuffix}> {

}