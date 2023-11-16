package com.eshore.ctmp;

import cn.hutool.core.util.ObjectUtil;
import com.eshore.ctmp.config.JTMessageProperties;
import com.eshore.ctmp.config.MessageProperties;
import com.eshore.ctmp.config.SMGPProperties;
import com.eshore.ctmp.handler.MessageApi;
import com.eshore.ctmp.handler.impl.JTMessageApiServiceImpl;
import com.eshore.ctmp.handler.impl.SMGPApiServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @ClassName SMGPAutoConfigure
 * @Description 短信api服务自动装配类，根据策略自动实例化一个短信发送服务api
 * @Author jianlin.liu
 * @Date 2023/4/21
 * @Version 1.0
 **/
@Configuration
@EnableConfigurationProperties(MessageProperties.class)
@Slf4j
public class MessageServiceAutoConfigure {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "message.enabled", havingValue = "true", matchIfMissing = false)
    public MessageApi messageApi(MessageProperties messageProperties) {
        log.info(">>>>>>>>>>message config strategy:{}", messageProperties.getStrategy());
        if (messageProperties.getConfig() == null) {
            log.error(">>>>>>>>>>[messageApi] => config is null");
            throw new RuntimeException("短信网关配置为空");
        }
        MessageApi messageApi;
        switch (messageProperties.getStrategy()) {
            case 1: // 集团短信网关策略（http协议）
                JTMessageProperties jtMessageProperties = messageProperties.getConfig().getJt();
                log.info(">>>>>>>>>>[messageConfig] => {}", jtMessageProperties);
                messageApi = new JTMessageApiServiceImpl(jtMessageProperties);
                break;
            case 2: // 省企信短信网关策略（SMGP协议）
                SMGPProperties properties = messageProperties.getConfig().getSmgp();
                messageApi = new SMGPApiServiceImpl(properties);
                try {
                    log.info(">>>>>>>>>>>[SMGP] => [{}] => start connect", properties);
                    int result = messageApi.connect_();
                    log.info(">>>>>>>>>>>[SMGP] => result={} connect successful", result);
                } catch (IOException e) {
                    log.error(">>>>>>>>>>[SMGP] => host:{} connect fail:{}",
                            properties.getHost() + ":" + properties.getPort(), e);
                    // 省企信短信网关无法连接时，尝试使用集团短信方式
                    if (ObjectUtil.isNotEmpty(messageProperties.getConfig().getJt())) {
                        messageApi = new JTMessageApiServiceImpl(messageProperties.getConfig().getJt());
                    }
                }
                break;
            default:
                throw new RuntimeException("短信策略配置错误");
        }
        return messageApi;
    }
}
