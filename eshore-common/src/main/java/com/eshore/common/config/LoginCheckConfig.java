package com.eshore.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 登录验证配置 ymh
 */
@Configuration
@ConfigurationProperties(prefix = "login")
@Data
public class LoginCheckConfig {

    /**
     * 是否校验手机验证码
     */
    private boolean checkMessageCode;

    /**
     * 超级验证码，使用此验证码可跳过短信验证
     */
    private String superMessageCode = "77889900";

    /**
     * 最大登录失败次数（在 errorForbiddenTime/60 分钟内，连续登录 errorMaxTimes 次登录失败，账号锁定 errorForbiddenTime/60 分钟）
     */
    private int errorMaxTimes;

    /**
     * 登录失败次数/账号锁定 时间间隔，单位秒
     */
    private int errorForbiddenTime;

    /**
     * 是否限制单用户登录
     */
    private boolean loginSingle;

    /**
     * 平台检测开关，为true时，不同平台的账号只能登录账号自身所属平台
     */
    private boolean checkPlatform;

}
