package ${packages.entityPackage};

<#if useLombok>
import lombok.Getter;
import lombok.Setter;
</#if>
<#list packageImports as packageImport>
${packageImport}
</#list>

/**
 * ${tableRemark}实体
 *
 * @author ${author}
 * @version 1.0
 * @since JDK1.8
 */
<#if  useLombok>
@Getter
@Setter
</#if>
<#if logogram&&plus>
@TableName("${tableName}")
</#if>
<#if superEntityClass??>
public class ${entityNameWithSuffix} extends ${superEntityClass}{
<#else>
public class ${entityNameWithSuffix} implements Serializable {
</#if>
<#list columnDatas as columnData>

    <#if columnData.columnRemark !="">
    /**
     * ${columnData.columnRemark}
     */
    </#if>
    <#if plus>
        <#if columnData.pkColumn>
    @TableId(value = "${columnData.columnName}", type = IdType.AUTO)
        </#if>
        <#if columnData.logicDelete>
    @TableLogic
         </#if>
    </#if>
    private ${columnData.filedType} ${columnData.filedName};
</#list>

<#if  !useLombok>
<#list columnDatas as columnData>
    public ${columnData.filedType} get${columnData.filedName?cap_first}() {
        return ${columnData.filedName};
    }

    public void set${columnData.filedName?cap_first}(${columnData.filedType} ${columnData.filedName}) {
        this.${columnData.filedName} = ${columnData.filedName};
    }
</#list>
</#if>

}
