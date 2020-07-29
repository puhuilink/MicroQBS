/*
 * @Author: sevncz.wen
 * @Date: 2020-03-24 10:35:07
 * @LastEditors: sevncz.wen
 * @LastEditTime: 2020-05-19 09:18:17
 * @FilePath: /phlink-common-framework/core/web/src/main/java/com/phlink/core/web/config/mybatis/SqlFilterArgumentResolver.java
 */
package com.puhuilink.qbs.core.web.config.mybatis;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.puhuilink.qbs.core.base.enums.ResultCode;

import com.puhuilink.qbs.core.base.exception.WarnException;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SqlFilterArgumentResolver implements HandlerMethodArgumentResolver {
    private final static String[] KEYWORDS = { "master", "truncate", "insert", "select", "delete", "update", "declare",
            "alter", "drop", "sleep" };

    /**
     * 判断Controller是否包含page 参数
     *
     * @param parameter 参数
     * @return 是否过滤
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Page.class);
    }

    /**
     * @param parameter     入参集合
     * @param mavContainer  model 和 view
     * @param webRequest    web相关
     * @param binderFactory 入参解析
     * @return 检查后新的page对象
     *         <p>
     *         page 只支持查询 GET .如需解析POST获取请求报文体处理
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        String ascs = request.getParameter("ascs");
        String descs = request.getParameter("descs");
        String current = request.getParameter("current");
        String size = request.getParameter("size");

        Page page = new Page();
        if (StrUtil.isNotBlank(current)) {
            page.setCurrent(Long.parseLong(current));
        }

        if (StrUtil.isNotBlank(size)) {
            page.setSize(Long.parseLong(size));
        }

        // 过滤 asc 条件
        List<OrderItem> ascList = sqlInject(ascs, "asc");
        // 过滤 desc条件
        List<OrderItem> descList = sqlInject(descs, "desc");

        List<OrderItem> orderItemList = new ArrayList<>();
        if (CollUtil.isNotEmpty(ascList)) {
            orderItemList.addAll(ascList);
        }

        if (CollUtil.isNotEmpty(descList)) {
            orderItemList.addAll(descList);
        }
        page.setOrders(orderItemList);
        return page;
    }

    /**
     * SQL注入过滤
     *
     * @param str 待验证的字符串
     * @return 返回标准的order 属性
     */
    private static List<OrderItem> sqlInject(String str, String type) {
        if (StrUtil.isBlank(str)) {
            return null;
        }
        // 转换成小写
        String inStr = str.toLowerCase();

        // 判断是否包含非法字符
        for (String keyword : KEYWORDS) {
            if (inStr.contains(keyword)) {
                log.error("查询包含非法字符 {}", keyword);
                throw new WarnException(ResultCode.FAIL.getCode(), keyword + "包含非法字符");
            }
        }

        List<OrderItem> orderItemList = new ArrayList<>();
        for (String in : str.split(StrUtil.COMMA)) {
            if ("asc".equals(type)) {
                orderItemList.add(OrderItem.asc(in));
            } else {
                orderItemList.add(OrderItem.desc(in));
            }
        }
        return orderItemList;
    }
}
