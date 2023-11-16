package com.eshore.business.handler.redis;


import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

/**
 * @author Admin
 */
@Component
@Slf4j
public class AbstractExpiredMessageHandler implements ExpiredMessageHandler {



    @Override
    public void handle(Message message, byte[] pattern) {

    }
}
