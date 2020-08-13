/*
 * @Author: sevncz.wen
 * @Date: 2020-05-18 18:04:34
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-05-18 18:07:28
 */
package com.puhuilink.qbs.auth.controller.auth;

import com.puhuilink.qbs.core.base.enums.ResultCode;
import com.puhuilink.qbs.core.base.exception.WarnException;
import com.puhuilink.qbs.core.base.vo.Result;
import com.puhuilink.qbs.core.common.utils.CreateVerifyCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

@Slf4j
@Api(tags = "验证码相关接口")
@RequestMapping("/api/auth/captcha")
@RestController
@Transactional
public class CaptchaController {

    @RequestMapping(value = "/init-mobile/{mobile}", method = RequestMethod.GET)
    @ApiImplicitParams({@ApiImplicitParam(name = "mobile", value = "手机号", defaultValue = "18600001111")})
    @ApiOperation(value = "初始化手机验证码")
    public Result initMobileCaptcha(@PathVariable String mobile, HttpServletResponse response) {
        long codeL = System.nanoTime();
        String codeStr = Long.toString(codeL);
        String code = codeStr.substring(codeStr.length() - 6);
        log.info("mobile: {} code: {}", mobile, code);
        Cookie cookie = new Cookie(mobile, code);
        cookie.setMaxAge(5 * 60);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return Result.ok();
    }

    @RequestMapping(value = "/init", method = RequestMethod.GET)
    @ApiOperation(value = "初始化验证码")
    public Result initCaptcha(HttpServletResponse response) {

        String captchaId = UUID.randomUUID().toString().replace("-", "");
        String code = new CreateVerifyCode().randomStr(4);
        // 缓存验证码
//        redissonClient.getBucket(captchaId, new StringCodec()).set(code, 5L, TimeUnit.MINUTES);
        log.info("captchaId: {} code: {}", captchaId, code);
        Cookie cookie = new Cookie(captchaId, code);
        cookie.setMaxAge(5 * 60);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return Result.ok().data(captchaId);
    }

    @RequestMapping(value = "/draw/{captchaId}", method = RequestMethod.GET)
    @ApiImplicitParams({@ApiImplicitParam(name = "captchaId", value = "验证码ID", defaultValue = "00")})
    @ApiOperation(value = "根据验证码ID获取图片")
    public void drawCaptcha(@PathVariable("captchaId") String captchaId, HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        // 得到验证码 生成指定验证码
//        RBucket<String> bucket = redissonClient.getBucket(captchaId, new StringCodec());
        Cookie[] cookies = request.getCookies();
        String code = null;
        if (cookies != null) {
            code = Arrays.stream(cookies)
                    .filter(c -> captchaId.equals(c.getName())).findAny().toString();
        }
        if (StringUtils.isBlank(code)) {
            throw new WarnException(ResultCode.BAD_REQUEST_PARAMS.getCode(), "验证码ID失效");
        }
        CreateVerifyCode vCode = new CreateVerifyCode(116, 36, 4, 10, code);
        response.setContentType("image/png");
        vCode.write(response.getOutputStream());
    }
}
