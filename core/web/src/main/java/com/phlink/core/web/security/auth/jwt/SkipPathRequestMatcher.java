/*
 * @Author: sevncz.wen
 * @Date: 2020-05-06 10:24:33
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-05-18 18:10:26
 */
package com.phlink.core.web.security.auth.jwt;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

public class SkipPathRequestMatcher implements RequestMatcher {
    private OrRequestMatcher matchers;
    private RequestMatcher processingMatcher;

    public SkipPathRequestMatcher(List<String> pathsToSkip, String processingPath) {
        Assert.notEmpty(pathsToSkip, "pathsToSkip 不能为空");
        List<RequestMatcher> m = pathsToSkip.stream().map(path -> new AntPathRequestMatcher(path))
                .collect(Collectors.toList());
        matchers = new OrRequestMatcher(m);
        processingMatcher = new AntPathRequestMatcher(processingPath);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        if (matchers.matches(request)) {
            return false;
        }
        return processingMatcher.matches(request) ? true : false;
    }
}
