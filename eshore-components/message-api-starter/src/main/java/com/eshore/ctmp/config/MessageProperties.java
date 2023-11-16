package com.eshore.ctmp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName MessageProperties
 * @Description 短信配置
 * @Author jianlin.liu
 * @Date 2023/4/21
 * @Version 1.0
 **/
@ConfigurationProperties(prefix = "message")
@Data
public class MessageProperties {
    private Boolean enabled;
    /**
     * 策略
     */
    private Integer strategy = 1;
    private Config config;

    @Data
    public static class Config {
        private SMGPProperties smgp;
        private JTMessageProperties jt;
    }
}
