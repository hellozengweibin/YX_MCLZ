package com.eshore.feign.weifengDianXiang.constants;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "weifeng")
public class WeifengProperties {
    private String url;
}
