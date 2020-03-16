package com.phlink.core.security.validator.sms;

import cn.hutool.core.util.StrUtil;
import com.google.gson.Gson;
import com.phlink.core.common.enums.CommonResultInfo;
import com.phlink.core.common.utils.ResponseUtil;
import com.phlink.core.config.properties.CaptchaProperties;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

@Slf4j
@Configuration
public class SmsValidateFilter extends OncePerRequestFilter {

    @Autowired
    private CaptchaProperties captchaProperties;

    @Autowired
    private RedissonClient redissonClient;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        // 判断URL是否需要验证
        Boolean flag = false;
        String requestUrl = request.getRequestURI();
        PathMatcher pathMatcher = new AntPathMatcher();
        for (String url : captchaProperties.getSms()) {
            if (pathMatcher.match(url, requestUrl)) {
                flag = true;
                break;
            }
        }
        if (flag) {

            try (InputStream is = request.getInputStream()) {
                Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
                SmsValidateVO validateVO = new Gson().fromJson(reader, SmsValidateVO.class);

                if (StrUtil.isBlank(validateVO.getMobile()) || StrUtil.isBlank(validateVO.getCode())) {
                    ResponseUtil.out(response, ResponseUtil.resultMap(false, CommonResultInfo.INTERNAL_SERVER_ERROR, "请输入手机号和验证码"));
                    return;
                }

                RBucket<String> bucket = redissonClient.getBucket(validateVO.getMobile(), new StringCodec());
                String redisCode = bucket.get();
                if (StrUtil.isBlank(redisCode)) {
                    ResponseUtil.out(response, ResponseUtil.resultMap(false, CommonResultInfo.INTERNAL_SERVER_ERROR, "验证码已过期，请重新获取"));
                    return;
                }

                if (!redisCode.toLowerCase().equals(validateVO.getCode().toLowerCase())) {
                    log.info("验证码错误：code:" + validateVO.getCode() + "，redisCode:" + redisCode);
                    ResponseUtil.out(response, ResponseUtil.resultMap(false, CommonResultInfo.INTERNAL_SERVER_ERROR, "验证码输入错误"));
                    return;
                }
                // 已验证清除key
                redissonClient.getKeys().delete(validateVO.getMobile());
                chain.doFilter(request, response);
            } catch (IOException e) {
                e.printStackTrace();
            }
            chain.doFilter(request, response);
            return;
        }
        // 无需验证 放行
        chain.doFilter(request, response);
    }
}