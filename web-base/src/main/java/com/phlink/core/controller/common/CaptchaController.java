package com.phlink.core.controller.common;

import cn.hutool.core.util.StrUtil;
import com.phlink.core.common.enums.CommonResultInfo;
import com.phlink.core.common.exception.BizException;
import com.phlink.core.common.utils.CreateVerifyCode;
import com.phlink.core.common.utils.ResultUtil;
import com.phlink.core.common.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Api(tags = "验证码接口")
@RequestMapping("/common/captcha")
@RestController
@Transactional
public class CaptchaController {

    @Autowired
    private RedissonClient redissonClient;

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
    public void drawCaptcha(@PathVariable("captchaId") String captchaId, HttpServletResponse response) throws IOException {

        //得到验证码 生成指定验证码
        RBucket<String> bucket = redissonClient.getBucket(captchaId, new StringCodec());
        String code = bucket.get();
        if(StrUtil.isBlank(code)) {
            throw new BizException(CommonResultInfo.FAIL, "验证码ID失效");
        }
        CreateVerifyCode vCode = new CreateVerifyCode(116, 36, 4, 10, code);
        response.setContentType("image/png");
        vCode.write(response.getOutputStream());
    }
}