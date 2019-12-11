package ${packages.serviceImplPackage};

import org.springframework.stereotype.Service;
<#if !plus>
<#if auto>
import org.springframework.beans.factory.annotation.Autowired;
<#else>
import javax.annotation.Resource;
</#if>
</#if>
import ${packages.mapperPackage}.${entityName}Mapper;
import ${packages.servicePackage}.${entityName}Service;
import java.util.List;
import ${packages.paramPackage}.${entityName}SaveParam;
import ${packages.paramPackage}.${entityName}QueryParam;
import ${packages.dtoPackage}.${entityName}DTO;
import ${packages.entityPackage}.${entityNameWithSuffix};
<#if plus>
import lombok.val;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
<#else>
import com.melon.sql.conditions.SqlOperateBuilder;
</#if>
<#if pagedName.paged&&!plus>
import ${packages.pagedInfoPackage};
</#if>
<#if dtoUtils?? && packages.superDtoUtilsPackage??>
import ${packages.superDtoUtilsPackage};
</#if>
<#assign entityDTOName= entityName+"DTO"/>
<#assign returlType= (plus)?string('boolean', 'int')/>

/**
 * ${tableRemark}业务层实现
 *
 * @author ${author}
 * @version 1.0
 * @since JDK1.8
 */
<#assign uncapEntityName= entityName?uncap_first/>
<#assign entityMapper= uncapEntityName+"Mapper"/>
<#assign capDbKey= dbKey?cap_first/>
@Service("${uncapEntityName}Service")
public class ${entityName}ServiceImpl<#if plus> extends ServiceImpl<${entityName}Mapper, ${entityNameWithSuffix}></#if> implements ${entityName}Service {
<#if !plus>

	@${injection}
	private ${entityName}Mapper ${entityMapper};
</#if>

<#if plus>
	@Override
    public ${returlType} insert(${entityDTOName} record) {
		return super.save(record.toDO());
	}

    @Override
    public ${returlType} insertOrUpdate(${entityName}SaveParam condition) {
        return super.saveOrUpdate(${dtoUtils}.copy(condition, ${entityNameWithSuffix}.class));
    }

	@Override
	public ${entityDTOName} get(${dbKeyType} ${dbKey}) {
		return ${dtoUtils}.toDTO(super.getById(${dbKey}), ${entityDTOName}.class);
	}

	@Override
    public List<${entityDTOName}> gets(${entityName}QueryParam condition) {
        val wrapper = new LambdaQueryWrapper<${entityNameWithSuffix}>();
        //TODO 查询条件
		return ${dtoUtils}.toDTOs(super.list(wrapper), ${entityDTOName}.class);
	}

	@Override
    public IPage<${entityDTOName}> paged(${entityName}QueryParam condition) {
        val wrapper = new LambdaQueryWrapper<${entityNameWithSuffix}>();
        //TODO 分页条件
		return ${dtoUtils}.toPage(super.page(new Page<>(), wrapper), ${entityDTOName}.class);
	}

	@Override
    public ${returlType} updateByCondition(${entityName}SaveParam condition) {
        val wrapper = new LambdaUpdateWrapper<${entityNameWithSuffix}>();
        //TODO 修改条件
        return super.update(wrapper);
    }
<#else>
	@Override
    public int insert(${entityDTOName} record) {
		return ${entityMapper}.insert(record.toDO());
	}

    @Override
    public int insertOrUpdate(${entityName}SaveParam condition) {
        if (condition.get${dbKey?cap_first}() == null) {
            return this.insert(${dtoUtils}.copy(condition, ${entityDTOName}.class));
        }
        return this.updateByCondition(condition);
    }

	@Override
	public ${entityDTOName} get(${dbKeyType} ${dbKey}) {
		return ${dtoUtils}.toDTO(${entityMapper}.findBy${capDbKey}(${dbKey}).orElseThrow(RuntimeException::new), ${entityDTOName}.class);
	}

	@Override
    public List<${entityDTOName}> gets(${entityName}QueryParam condition) {
		return ${dtoUtils}.toDTOs(${entityMapper}.findByBuilder(this.buildQueryBuilder(condition)), ${entityDTOName}.class);
	}
<#if pagedName.paged>

	@Override
	public ${pagedName.pagedInfoName}<ShopRoleDTO> paged(${entityName}QueryParam condition) {
		return ${dtoUtils}.toPage(${entityMapper}.findByBuilder(this.buildQueryBuilder(condition)),  ${entityDTOName}.class);
	}
</#if>

	@Override
    public int updateByCondition(${entityName}SaveParam condition) {
        return ${entityMapper}.updateByBuilder(this.buildUpdateBuilder(condition));
    }

    @Override
    public int deleteBy${capDbKey}(${dbKeyType} ${dbKey}) {
        return ${entityMapper}.deleteBy${capDbKey}(${dbKey});
	}

	private SqlOperateBuilder<${entityNameWithSuffix}> buildQueryBuilder(${entityName}QueryParam condition) {
	    if (condition == null) {
	        return new SqlOperateBuilder<>();
	    }
        return new SqlOperateBuilder<${entityNameWithSuffix}>()
             <#list allColumnData as columnData>
                <#assign capEntityName= columnData.filedName?cap_first/>
                .eq(${entityNameWithSuffix}::get${columnData.filedName?cap_first}, condition.get${columnData.filedName?cap_first}())<#if !columnData_has_next>;</#if>
             </#list>
	}

	private SqlOperateBuilder<${entityNameWithSuffix}> buildUpdateBuilder(${entityName}SaveParam condition) {
	    if (condition == null) {
	        return new SqlOperateBuilder<>();
	    }
        return new SqlOperateBuilder<${entityNameWithSuffix}>()
             <#list allColumnData as columnData>
                <#assign capEntityName= columnData.filedName?cap_first/>
                .set(${entityNameWithSuffix}::get${columnData.filedName?cap_first}, condition.get${columnData.filedName?cap_first}())
             </#list>
             <#list allColumnData as columnData>
                <#assign capEntityName= columnData.filedName?cap_first/>
                .eq(${entityNameWithSuffix}::get${columnData.filedName?cap_first}, condition.get${columnData.filedName?cap_first}())<#if !columnData_has_next>;</#if>
             </#list>
	}

</#if>

}
