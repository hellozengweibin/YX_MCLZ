package com.eshore.business.listener;


import com.eshore.business.constant.CommonConstants;
import com.eshore.business.handler.redis.ExpiredMessageHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * RedisKey键监听以及业务逻辑处理
 *
 * @author: lll
 * @date: 2022年03月07日 14:03:49
 */
@Component
@Slf4j
public class RedisKeyExpiredListener extends KeyExpirationEventMessageListener {


    @Autowired
    private Map<String, ExpiredMessageHandler> handlerMap = new HashMap<>();


    /**
     * @param listenerContainer 监听器
     */
    public RedisKeyExpiredListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @SneakyThrows
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        if(expiredKey.startsWith(CommonConstants.REDIS_KEY_EXPIRED)){
           String[] temps= expiredKey.split(CommonConstants.REDIS_KEY_SEPARATOR);
            String type = temps[2];
            ExpiredMessageHandler messageHandler = handlerMap.get(type);
            if (messageHandler == null) {
                log.warn("未找到类型【{}】 对应的处理类", type);
                return;
            }
            messageHandler.handle(message, pattern);
            log.info("RedisKeyExpirationListener onMessage" + ":" + expiredKey);
        }


    }
}
