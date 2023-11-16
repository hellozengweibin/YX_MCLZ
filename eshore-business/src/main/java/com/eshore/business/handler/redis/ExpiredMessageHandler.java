package com.eshore.business.handler.redis;

import org.springframework.data.redis.connection.Message;

/**
 * @author Admin
 */
public interface ExpiredMessageHandler {
    void handle(Message message, byte[] pattern) throws Exception;
}
