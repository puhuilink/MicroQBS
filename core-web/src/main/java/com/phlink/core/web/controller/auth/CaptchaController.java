/*
 * @Author: sevncz.wen
 * @Date: 2020-05-18 18:04:34
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-05-18 18:07:28
 */
package com.phlink.core.web.controller.auth;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import com.phlink.core.base.enums.ResultCode;
import com.phlink.core.base.exception.BizException;
import com.phlink.core.base.utils.CreateVerifyCode;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "验证码相关接口")
@RequestMapping("/api/auth/captcha")
@RestController
@Transactional
public class CaptchaController {

    @Autowired
    private RedissonClient redissonClient;

    @RequestMapping(value = "/init-mobile/{mobile}", method = RequestMethod.GET)
    @ApiOperation(value = "初始化手机验证码")
    public String initMobileCaptcha(@PathVariable String mobile) {
        Long codeL = System.nanoTime();
        String codeStr = Long.toString(codeL);
        String code = codeStr.substring(codeStr.length() - 6);
        // 缓存验证码
        redissonClient.getBucket(mobile, new StringCodec()).set(code, 5L, TimeUnit.MINUTES);
        return code;
    }

    @RequestMapping(value = "/init", method = RequestMethod.GET)
    @ApiOperation(value = "初始化验证码")
    public String initCaptcha() {

        String captchaId = UUID.randomUUID().toString().replace("-", "");
        String code = new CreateVerifyCode().randomStr(4);
        // 缓存验证码
        redissonClient.getBucket(captchaId, new StringCodec()).set(code, 5L, TimeUnit.MINUTES);
        return captchaId;
    }

    @RequestMapping(value = "/draw/{captchaId}", method = RequestMethod.GET)
    @ApiOperation(value = "根据验证码ID获取图片")
    public void drawCaptcha(@PathVariable("captchaId") String captchaId, HttpServletResponse response)
            throws IOException {

        // 得到验证码 生成指定验证码
        RBucket<String> bucket = redissonClient.getBucket(captchaId, new StringCodec());
        String code = bucket.get();
        if (StrUtil.isBlank(code)) {
            throw new BizException(ResultCode.FAIL, "验证码ID失效");
        }
        CreateVerifyCode vCode = new CreateVerifyCode(116, 36, 4, 10, code);
        response.setContentType("image/png");
        vCode.write(response.getOutputStream());
    }
}
