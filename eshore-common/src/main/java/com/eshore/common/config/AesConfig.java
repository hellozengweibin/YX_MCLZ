package com.eshore.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName RsaProperties
 * @Description AES加解密属性配置
 * @Author jianlin.liu
 * @Date 2022/9/13
 * @Version 1.0
 **/
@Configuration
@ConfigurationProperties(prefix = "aes.encrypt")
@Data
public class AesConfig {

    /**
     * 是否开启
     */
    private boolean enable;

    /**
     * 日志显示
     */
    private boolean showLog = false;

    /**
     * 偏移量
     */
    private String iv;

    /**
     * 加解密 秘钥
     */
    private String secretKey;

    /**
     * get请求参数键名
     */
    private String requestParamKeyName;

    /**
     * body加密参数键名
     */
    private String requestBodyKeyName;

}
