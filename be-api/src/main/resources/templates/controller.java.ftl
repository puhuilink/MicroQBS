package ${package.Controller?substring(10)};


import org.springframework.web.bind.annotation.RequestMapping;

<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import ${package.Mapper?substring(10)}.${table.mapperName};
import ${package.Entity?substring(10)}.${entity};
import ${package.Service?substring(10)}.${table.serviceName};
import com.phlink.bus.api.common.annotation.Log;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.wuwenze.poi.ExcelKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;


/**
* @author ${author}
*/
@Slf4j
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
@Api(tags ="${table.comment!}")
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>
    @Autowired
    public ${table.serviceName} ${entity?uncap_first}Service;

    @GetMapping
    @RequiresPermissions("${entity?uncap_first}:view")
    @ApiOperation(value = "列表", notes = "列表", tags = "${table.comment!}", httpMethod = "GET")
    public Map<String, Object> list${entity}(QueryRequest request, ${entity} ${entity?uncap_first}) {
        return getDataTable(this.${entity?uncap_first}Service.list${entity}s(request, ${entity?uncap_first}));
    }

    @GetMapping("/{id}")
    @RequiresPermissions("${entity?uncap_first}:get")
    @ApiOperation(value = "${table.comment!}详情", notes = "${table.comment!}详情", tags = "${table.comment!}", httpMethod = "GET")
    public ${entity} detail(@PathVariable Long id) {
        return this.${entity?uncap_first}Service.findById(id);
    }

    @Log("添加${table.comment!}")
    @PostMapping
    @RequiresPermissions("${entity?uncap_first}:add")
    @ApiOperation(value = "添加${table.comment!}", notes = "添加${table.comment!}", tags = "${table.comment!}", httpMethod = "POST")
    public void add${entity}(@RequestBody @Valid ${entity} ${entity?uncap_first}) throws BusApiException {
        this.${entity?uncap_first}Service.create${entity}(${entity?uncap_first});
    }

    @Log("修改${table.comment!}")
    @PutMapping
    @RequiresPermissions("${entity?uncap_first}:update")
    @ApiOperation(value = "修改${table.comment!}", notes = "修改${table.comment!}", tags = "${table.comment!}", httpMethod = "PUT")
    public void update${entity}(@RequestBody @Valid ${entity} ${entity?uncap_first}) throws BusApiException{
        this.${entity?uncap_first}Service.modify${entity}(${entity?uncap_first});
    }

    @Log("删除${table.comment!}")
    @DeleteMapping("/{${entity?uncap_first}Ids}")
    @RequiresPermissions("${entity?uncap_first}:delete")
    @ApiOperation(value = "删除${table.comment!}", notes = "删除${table.comment!}", tags = "${table.comment!}", httpMethod = "DELETE")
    public void delete${entity}(@NotBlank(message = "{required}") @PathVariable String ${entity?uncap_first}Ids) throws BusApiException{
        String[] ids = ${entity?uncap_first}Ids.split(StringPool.COMMA);
        this.${entity?uncap_first}Service.delete${entity}s(ids);
    }

    @PostMapping("export")
    @RequiresPermissions("${entity?uncap_first}:export")
    @ApiOperation(value = "导出", notes = "导出", tags = "${table.comment!}", httpMethod = "POST")
    public void export${entity}(QueryRequest request, @RequestBody ${entity} ${entity?uncap_first}, HttpServletResponse response) throws BusApiException{
        List<${entity}> ${entity?uncap_first}s = this.${entity?uncap_first}Service.list${entity}s(request, ${entity?uncap_first}).getRecords();
        ExcelKit.$Export(${entity}.class, response).downXlsx(${entity?uncap_first}s, false);
    }
}
</#if>
