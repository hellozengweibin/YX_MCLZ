package com.eshore.ctmp.config;

import lombok.Data;

/**
 * @ClassName JTMessageProperties
 * @Description 集团短信配置
 * @Author jianlin.liu
 * @Date 2023/4/21
 * @Version 1.0
 **/
// @ConfigurationProperties(prefix = "message.config.jt")
@Data
public class JTMessageProperties {
    private String url;
    private String msgPrefix;
    private String account;
    private String password;
}
