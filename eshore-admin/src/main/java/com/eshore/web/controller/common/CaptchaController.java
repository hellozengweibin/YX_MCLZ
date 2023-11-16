package com.eshore.web.controller.common;

import com.eshore.common.annotation.EncryptIgnore;
import com.eshore.common.config.EshoreConfig;
import com.eshore.common.constant.Constants;
import com.eshore.common.core.redis.RedisCache;
import com.eshore.common.core.response.CommonResult;
import com.eshore.common.core.response.ResponseGenerator;
import com.eshore.common.utils.sign.Base64;
import com.eshore.common.utils.uuid.IdUtils;
import com.eshore.system.service.ISysConfigService;
import com.google.code.kaptcha.Producer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 验证码操作处理
 *
 * @author eshore
 */
@Api(tags = {"验证码操作处理"})
@RestController
public class CaptchaController {
    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysConfigService configService;

    /**
     * 生成验证码
     */
    @EncryptIgnore
    @ApiOperation(value = "生成验证码" , notes = "生成验证码" , httpMethod = "GET")
    @GetMapping("/captchaImage")
    public CommonResult<CaptchaResult> getCode(HttpServletResponse response) throws IOException {
        CaptchaResult result = new CaptchaResult();
        boolean captchaOnOff = configService.selectCaptchaOnOff();
        result.setCaptchaOnOff(captchaOnOff);
        if (!captchaOnOff) {
            return ResponseGenerator.genSuccessResult(result);
        }

        // 保存验证码信息
        String uuid = IdUtils.simpleUUID();
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;

        String capStr = null, code = null;
        BufferedImage image = null;

        // 生成验证码
        String captchaType = EshoreConfig.getCaptchaType();
        if ("math".equals(captchaType)) {
            String capText = captchaProducerMath.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = captchaProducerMath.createImage(capStr);
        } else if ("char".equals(captchaType)) {
            capStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        }

        redisCache.setCacheObject(verifyKey, code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", os);
        } catch (IOException e) {
            return ResponseGenerator.genFailResult(e.getMessage());
        }

        result.setUuid(uuid);
        result.setImg(Base64.encode(os.toByteArray()));
        return ResponseGenerator.genSuccessResult(result);
    }


    @ApiModel(value = "CaptchaResult")
    @Data
    public static class CaptchaResult {
        /**
         * 验证码开关
         */
        @ApiModelProperty(value = "V")
        private Boolean captchaOnOff;

        /**
         * UUID
         */
        @ApiModelProperty(value = "UUID")
        private String uuid;

        /**
         * 图片
         */
        @ApiModelProperty(value = "BASE64图片")
        private String img;
    }
}
