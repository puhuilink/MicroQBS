package com.puhuilink.qbs.graphapi.resolver;

import com.puhuilink.qbs.core.logtrace.entity.LogTrace;
import com.puhuilink.qbs.core.logtrace.service.LogTraceService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LogTraceQuery implements GraphQLQueryResolver {
    @Autowired
    public LogTraceService logTraceService;

    public List<LogTrace> logTraces() {
        return logTraceService.list();
    }

    public LogTrace logTrace(String id) {
        return logTraceService.getById(id);
    }
}