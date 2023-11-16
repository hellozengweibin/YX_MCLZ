package com.eshore.ctmp.config;


import lombok.Data;

/**
 * @ClassName SMGPProperties
 * @Description SMGP属性配置
 * @Author jianlin.liu
 * @Date 2023/4/21
 * @Version 1.0
 **/
// @ConfigurationProperties(prefix = "message.config.smgp")
@Data
public class SMGPProperties {

    private String host;
    private int port;
    private String clientId;
    private String password;
    private byte loginMode;
    private byte version;
    private int soTimeout = 30000;

}
