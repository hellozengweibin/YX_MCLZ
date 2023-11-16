package com.eshore.feign.yinzhu.constant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "yinzhu")
@Data
public class YinZhuProperties {

    private String url;
    //账号
    private String userName;
    //密码
    private String psd;
}
