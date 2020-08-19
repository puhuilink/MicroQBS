package com.puhuilink.qbs.graphapi.resolver;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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

    public List<LogTrace> listLogTrace() {
        return logTraceService.list();
    }

    public List<LogTrace> pageLogTrace(int pageNumber, int pageSize) {
        PageInfo<LogTrace> pages = PageHelper.startPage(pageNumber, pageSize).doSelectPageInfo(() -> logTraceService.list());
        return pages.getList();
    }

    public LogTrace getLogTrace(String id) {
        return logTraceService.getById(id);
    }
}