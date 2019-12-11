package ${packages.paramPackage};
<#if useLombok>
import lombok.Getter;
import lombok.Setter;
</#if>
<#assign currentParam= paramName />
<#list paramImports as packageImport>
<#if packageImport?contains("import java.")>
${packageImport}
</#if>
</#list>
<#if currentParam.supperName?default("")?trim?length=0>
import java.io.Serializable;
</#if>
<#if currentParam.pkg??>
import ${currentParam.pkg};
</#if>

/**
 * ${tableRemark}参数
 *
 * @author ${author}
 * @version 1.0
 * @since JDK1.8
 */
<#if  useLombok>
@Getter
@Setter
</#if>
<#if currentParam.supperName??>
public class ${currentParam.name} extends ${currentParam.supperName}{
<#else>
public class ${currentParam.name} implements Serializable {
</#if>
<#list allColumnData as columnData>

    <#if columnData.columnRemark !="">
    /**
     * ${columnData.columnRemark}
     */
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
