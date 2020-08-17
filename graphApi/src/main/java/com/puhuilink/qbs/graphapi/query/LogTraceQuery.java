package com.puhuilink.qbs.graphapi.query;

import com.puhuilink.qbs.core.logtrace.annotation.SystemLogTrace;
import com.puhuilink.qbs.core.logtrace.entity.LogTrace;
import com.puhuilink.qbs.core.logtrace.enums.LogType;
import com.puhuilink.qbs.core.logtrace.service.LogTraceService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LogTraceQuery implements GraphQLQueryResolver{
    @Autowired
    public LogTraceService logTraceService;

    @SystemLogTrace(description = "Query Graphql Api", type = LogType.QUERY)
    public List<LogTrace> listLogtrace() {
        return logTraceService.list();
    }
}