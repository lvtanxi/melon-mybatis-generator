package ${packages.dtoPackage};
<#list paramImports as packageImport>
<#if packageImport?contains("import java.")>
${packageImport}
</#if>
</#list>
<#if superDtoClass?? && packages.superDtoPackage??>
import ${packages.superDtoPackage};
</#if>
import ${packages.entityPackage}.${entityNameWithSuffix};
<#assign entityDTOName= entityName+"DTO"/>

/**
 * ${tableRemark}DTO
 *
 * @author ${author}
 * @version 1.0
 * @since JDK1.8
 */
<#if superDtoClass??>
public class ${entityDTOName} extends ${superDtoClass}<${entityNameWithSuffix}> {
<#else>
public class ${entityDTOName} implements Serializable {
</#if>
<#if superDtoClass??>

    public ${entityDTOName}() {
		this(new ${entityNameWithSuffix}());
	}

    public ${entityDTOName}(${entityNameWithSuffix} ${entityName}) {
		super(${entityName});
	}
<#list dtoColumnData as columnData>
<#assign capEntityName= columnData.filedName?cap_first/>

   <#if columnData.columnRemark !="">
    /**
     * ${columnData.columnRemark}
     */
    </#if>
    public ${columnData.filedType} get${capEntityName}() {
        return this.entity.get${capEntityName}();
    }

   <#if columnData.columnRemark !="">
    /**
     * ${columnData.columnRemark}
     */
    </#if>
    public void set${capEntityName}(${columnData.filedType} ${columnData.filedName}) {
        this.entity.set${capEntityName}(${columnData.filedName});
    }
</#list>
<#else>
<#list allColumnData as columnData>

    <#if columnData.columnRemark !="">
    /**
     * ${columnData.columnRemark}
     */
    </#if>
    private ${columnData.filedType} ${columnData.filedName};
</#list>

<#if  !useLombok>
<#list allColumnData as columnData>
    public ${columnData.filedType} get${columnData.filedName?cap_first}() {
        return ${columnData.filedName};
    }

    public void set${columnData.filedName?cap_first}(${columnData.filedType} ${columnData.filedName}) {
        this.${columnData.filedName} = ${columnData.filedName};
    }
</#list>
</#if>
</#if>
}
