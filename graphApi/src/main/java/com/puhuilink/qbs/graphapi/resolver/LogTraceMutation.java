/*
 * @Author: sevncz.wen
 * @Date: 2020-08-19 16:58
 * @LastEditors: sevncz.wen
 * @LastEditTime: 2020-08-19 16:58
 */
package com.puhuilink.qbs.graphapi.resolver;

import com.puhuilink.qbs.core.logtrace.service.LogTraceService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @program: qbs-web
 * @description:
 * @author: sevncz.wen
 * @create: 2020-08-19 16:58
 **/
@Component
public class LogTraceMutation implements GraphQLMutationResolver {

    @Autowired
    private LogTraceService logTraceService;

    public boolean deleteLogTrace(String id) {
        return logTraceService.removeById(id);
    }
}
