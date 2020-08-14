/*
 * @Author: sevncz.wen
 * @Date: 2020-05-18 18:04:34
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-05-18 18:07:28
 */
package com.puhuilink.qbs.auth.controller.auth;

import com.puhuilink.qbs.auth.utils.CookieUtil;
import com.puhuilink.qbs.auth.utils.SecurityConstant;
import com.puhuilink.qbs.core.base.vo.Result;
import com.puhuilink.qbs.core.common.utils.CreateVerifyCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Api(tags = "验证码相关接口")
@RequestMapping("${qbs.api.path}" + "/auth/captcha")
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
        CookieUtil.setCookie(response, mobile, code);
        return Result.ok();
    }

    @RequestMapping(value = "/draw", method = RequestMethod.GET)
    @ApiOperation(value = "获取验证码图片")
    public void drawCaptcha(HttpServletResponse response)
            throws IOException {
        String code = new CreateVerifyCode().randomStr(4);
        CreateVerifyCode vCode = new CreateVerifyCode(116, 36, 4, 10, code);
        response.setContentType("image/png");
        log.info("draw captcha code: {}", code);
        CookieUtil.setCookie(response, SecurityConstant.COOKIE_CAPTCHA_CODE, code);
        vCode.write(response.getOutputStream());
    }
}
