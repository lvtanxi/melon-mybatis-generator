package ${packages.controllerPackage};

<#if auto>
import org.springframework.beans.factory.annotation.Autowired;
<#else>
import javax.annotation.Resource;
</#if>
import ${packages.servicePackage}.${entityName}Service;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ${packages.paramPackage}.${entityName}SaveParam;
import ${packages.paramPackage}.${entityName}QueryParam;
import ${packages.dtoPackage}.${entityName}DTO;
<#if plus>
import com.baomidou.mybatisplus.core.metadata.IPage;
<#else>
<#if pagedName.paged>
import ${packages.pagedAnnotationPackage};
import ${packages.pagedInfoPackage};
<#else>
import java.util.List;
</#if>
</#if>

/**
 * ${tableRemark}控制器
 *
 * @author ${author}
 * @version 1.0
 * @since JDK1.8
 */
<#assign entityDTOName= entityName+"DTO"/>
<#assign entityService= entityName?uncap_first+"Service"/>
<#assign entityId= entityName?uncap_first+dbKey?cap_first/>
@RestController
@RequestMapping("${requestMapping}")
public class ${entityName}Controller {

    @${injection}
    private ${entityName}Service ${entityService};

<#if plus>
    /**
     * 列表查询
     */
    @GetMapping
    public IPage<${entityDTOName}> paged(${entityName}QueryParam condition) {
        return ${entityService}.paged(condition);
    }

    /**
     * 详情
     */
    @GetMapping("/{${entityId}}")
    public ${entityDTOName} detail(@PathVariable Integer ${entityId}) {
        return ${entityService}.get(${entityId});
    }

    /**
     * 新增或者修改
     */
    @PostMapping
    public boolean saveOrUpdate(@Valid ${entityName}SaveParam condition) {
        return ${entityService}.insertOrUpdate(condition);
    }

    /**
     * 删除数据
     */
    @DeleteMapping("/{${entityId}}")
    public boolean delete(@PathVariable Integer ${entityId}) {
        return ${entityService}.removeById(${entityId});
    }
<#else>
    /**
     * 列表查询
     */
    @GetMapping
<#if pagedName.paged>
    @${pagedName.pagedAnnotationName}
    public ${pagedName.pagedInfoName}<${entityDTOName}> paged(${entityName}QueryParam condition) {
        return ${entityService}.paged(condition);
    }
<#else>
    public List<${entityDTOName}> list(${entityName}QueryParam condition) {
        return ${entityService}.gets(condition);
    }
</#if>

    /**
     * 详情
     */
    @GetMapping("/{${entityId}}")
    public ${entityDTOName} detail(@PathVariable Integer ${entityId}) {
        return ${entityService}.get(${entityId});
    }

    /**
     * 新增或者修改
     */
    @PostMapping
    public int insertOrUpdate(@Valid ${entityName}SaveParam condition) {
        return ${entityService}.insertOrUpdate(condition);
    }

    /**
     * 删除数据
     */
    @DeleteMapping("/{${entityId}}")
    public int delete(@PathVariable Integer ${entityId}) {
        return ${entityService}.deleteBy${dbKey?cap_first}(${entityId});
    }
</#if>

}
