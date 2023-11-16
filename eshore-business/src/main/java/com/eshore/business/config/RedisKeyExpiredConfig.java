package com.eshore.business.config;

import com.eshore.common.core.redis.RedisCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author Admin
 */
@Configuration
public class RedisKeyExpiredConfig {
    @Resource
    private RedisCache redisCache;
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        return container;
    }
    @PostConstruct
    public void init(){
//        String key= CommonConstants.REDIS_KEY_EXPIRED+ RedisMsgType.BOX_HEART+CommonConstants.REDIS_KEY_SEPARATOR+"admin";
//        redisCache.setCacheObject(key,1,20, TimeUnit.SECONDS);
//        System.out.println("Bean 开始初始化啦。。");
    }
}
