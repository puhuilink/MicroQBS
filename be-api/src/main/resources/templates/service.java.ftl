package ${package.Service?substring(10)};

import ${package.Entity?substring(10)}.${entity};
import ${superServiceClassPackage};
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.phlink.bus.api.common.domain.QueryRequest;
/**
 * @author ${author}
 */
<#if kotlin>
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {


    /**
    * 获取详情
    */
    ${entity} findById(Long id);

    /**
    * 查询列表
    * @param request
    * @param ${entity?uncap_first}
    * @return
    */
    IPage<${entity}> list${entity}s(QueryRequest request, ${entity} ${entity?uncap_first});

    /**
    * 新增
    * @param ${entity?uncap_first}
    */
    void create${entity}(${entity} ${entity?uncap_first});

    /**
    * 修改
    * @param ${entity?uncap_first}
    */
    void modify${entity}(${entity} ${entity?uncap_first});

    /**
    * 批量删除
    * @param ${entity?uncap_first}Ids
    */
    void delete${entity}s(String[] ${entity?uncap_first}Ids);
}
</#if>