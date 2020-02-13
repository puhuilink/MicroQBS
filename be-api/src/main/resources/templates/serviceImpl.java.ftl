package ${package.ServiceImpl?substring(10)};

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ${superServiceImplClassPackage};
import ${package.Mapper?substring(10)}.${table.mapperName};
import ${package.Entity?substring(10)}.${entity};
import ${package.Service?substring(10)}.${table.serviceName};
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.utils.BusApiUtil;
import com.phlink.bus.api.common.utils.SortUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
* @author ${author}
*/
@Service
<#if kotlin>
open class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>(), ${table.serviceName} {

}
<#else>
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}> implements ${table.serviceName} {

    @Override
    public ${entity} findById(Long id){
        return this.getById(id);
    }

    @Override
    public IPage<${entity}> list${entity}s(QueryRequest request, ${entity} ${entity?uncap_first}){
        QueryWrapper<${entity}> queryWrapper = new QueryWrapper<>();
        //TODO:查询条件
        Page<${entity}> page = new Page<>(request.getPageNum(), request.getPageSize());
        String sort= StringUtils.isNotBlank(request.getSortOrder())?request.getSortOrder():"id";
        String order=StringUtils.isNotBlank(request.getSortField())?request.getSortField():BusApiConstant.ORDER_DESC;
        SortUtil.handlePageSort(request, page, sort, order, true);
        return this.page(page, queryWrapper);
    }

    @Override
    @Transactional
    public void create${entity}(${entity} ${entity?uncap_first}) {
        ${entity?uncap_first}.setCreateTime(LocalDateTime.now());
        ${entity?uncap_first}.setCreateBy(BusApiUtil.getCurrentUser().getUserId());
        this.save(${entity?uncap_first});
    }

    @Override
    @Transactional
    public void modify${entity}(${entity} ${entity?uncap_first}) {
        ${entity?uncap_first}.setModifyTime(LocalDateTime.now());
        ${entity?uncap_first}.setModifyBy(BusApiUtil.getCurrentUser().getUserId());
        this.updateById(${entity?uncap_first});
    }

    @Override
    public void delete${entity}s(String[] ${entity?uncap_first}Ids) {
        List<Long> list = Stream.of(${entity?uncap_first}Ids)
        .map(Long::parseLong)
        .collect(Collectors.toList());
        removeByIds(list);
    }
}
</#if>