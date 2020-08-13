/*
 * @Author: sevncz.wen
 * @Date: 2020-03-24 10:35:07
 * @LastEditors: sevncz.wen
 * @LastEditTime: 2020-05-19 09:17:56
 * @FilePath: /phlink-common-framework/core/web/src/main/java/com/phlink/core/web/config/mybatis/DataScopeInterceptor.java
 */
package com.puhuilink.qbs.core.common.config.mybatis;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.handlers.AbstractSqlParserHandler;
import com.google.common.base.Joiner;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 数据权限过滤器
 */
@Slf4j
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class DataScopeInterceptor extends AbstractSqlParserHandler implements Interceptor {

    @Override
    @SneakyThrows
    public Object intercept(Invocation invocation) {
        StatementHandler statementHandler = (StatementHandler) PluginUtils.realTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        this.sqlParser(metaObject);
        // 先判断是不是SELECT操作
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        if (!SqlCommandType.SELECT.equals(mappedStatement.getSqlCommandType())) {
            return invocation.proceed();
        }

        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        String originalSql = boundSql.getSql();
        Object parameterObject = boundSql.getParameterObject();

        //查找参数中包含DataScope类型的参数
        DataScope dataScope = findDataScopeObject(parameterObject);

        if (dataScope == null) {
            return invocation.proceed();
        } else {
            String scopeName = dataScope.getScopeName();
            List<Integer> deptIds = dataScope.getDeptIds();
            if (StringUtils.isNotBlank(scopeName) && CollectionUtils.isNotEmpty(deptIds)) {
                String join = Joiner.on(",").join(deptIds);
                originalSql = "select * from (" + originalSql + ") temp_data_scope where temp_data_scope." + scopeName + " in (" + join + ")";
                metaObject.setValue("delegate.boundSql.sql", originalSql);
            }
            return invocation.proceed();
        }
    }

    /**
     * 生成拦截对象的代理
     *
     * @param target 目标对象
     * @return 代理对象
     */
    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    /**
     * mybatis配置的属性
     *
     * @param properties mybatis配置的属性
     */
    @Override
    public void setProperties(Properties properties) {

    }

    /**
     * 查找参数是否包括DataScope对象
     *
     * @param parameterObj 参数列表
     * @return DataScope
     */
    private DataScope findDataScopeObject(Object parameterObj) {
        if (parameterObj instanceof DataScope) {
            return (DataScope) parameterObj;
        } else if (parameterObj instanceof Map) {
            for (Object val : ((Map<?, ?>) parameterObj).values()) {
                if (val instanceof DataScope) {
                    return (DataScope) val;
                }
            }
        }
        return null;
    }

}
