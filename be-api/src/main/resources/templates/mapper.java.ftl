package ${package.Mapper?substring(10)};

import ${package.Entity?substring(10)}.${entity};
import ${superMapperClassPackage};

/**
 * @author ${author}
 */
<#if kotlin>
interface ${table.mapperName} : ${superMapperClass}<${entity}>
<#else>
public interface ${table.mapperName} extends ${superMapperClass}<${entity}> {

}
</#if>
