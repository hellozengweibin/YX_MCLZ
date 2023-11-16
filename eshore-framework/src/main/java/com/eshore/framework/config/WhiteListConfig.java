package com.eshore.framework.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author cjy
 * @Date 2022/10/18 9:32
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "whitelist")
public class WhiteListConfig {

    /**
     * 是否开启
     */
    private boolean enable;

    /**
     * 路径
     */
    private List<String> paths;
}
