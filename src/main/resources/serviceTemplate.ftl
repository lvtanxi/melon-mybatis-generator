package ${packages.servicePackage};

<#if plus>
import ${packages.entityPackage}.${entityNameWithSuffix};
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
</#if>
<#if pagedName.paged&&!plus>
import ${packages.pagedInfoPackage};
</#if>
import java.util.List;
import ${packages.paramPackage}.${entityName}SaveParam;
import ${packages.paramPackage}.${entityName}QueryParam;
import ${packages.dtoPackage}.${entityName}DTO;
<#assign uncapEntityName= entityName?uncap_first/>
<#assign entityDTOName= entityName+"DTO"/>
<#assign returlType= (plus)?string('boolean', 'int')/>

/**
 * ${tableRemark}业务接口
 *
 * @author ${author}
 * @version 1.0
 * @since JDK1.8
 */
public interface ${entityName}Service<#if plus> extends IService<${entityNameWithSuffix}></#if> {

    /**
     * 新增记录
     *
     * @param record 记录
     * @return 影响行数
     */
    ${returlType} insert(${entityDTOName} record);

    /**
     * 新增或者修改(主键不为空就判定为修改)
     *
     * @param condition 条件 {@link ${entityName}SaveParam}
     * @return 影响行数
     */
    ${returlType} insertOrUpdate(${entityName}SaveParam condition);

    /**
     * 根据主键查询
     *
     * @param ${dbKey} 主键
     * @return ${entityDTOName}
     */
    ${entityDTOName} get(${dbKeyType} ${dbKey});

    /**
     * 根据条件查询
     *
     * @param condition 条件 {@link ${entityName}QueryParam}
     * @return List<${entityDTOName}>
     */
    List<${entityDTOName}> gets(${entityName}QueryParam condition);
<#if plus>

    /**
     * 根据条件分页查询
     *
     * @param condition 条件 {@link ${entityName}QueryParam}
     * @return List<${entityDTOName}>
     */
    IPage<${entityDTOName}> paged(${entityName}QueryParam condition);
</#if>
<#if pagedName.paged&&!plus>

    /**
     * 根据条件分页查询
     *
     * @param condition 条件 {@link ${entityName}QueryParam}
     * @return List<${entityDTOName}>
     */
    ${pagedName.pagedInfoName}<${entityDTOName}> paged(${entityName}QueryParam condition);
</#if>

    /**
     * 条件修改(默认根据主键修改)
     *
     * @param condition 修改参数 {@link ${entityName}SaveParam}
     * @return 影响行数
     */
    ${returlType} updateByCondition(${entityName}SaveParam condition);
<#if !plus>

    /**
     * 根据主键删除
     *
     * @param ${dbKey} 主键
     * @return 影响行数
     */
    int deleteBy${dbKey?cap_first}(${dbKeyType} ${dbKey});
</#if>

}