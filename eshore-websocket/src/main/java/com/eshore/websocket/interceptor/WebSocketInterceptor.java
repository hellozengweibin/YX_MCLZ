package com.eshore.websocket.interceptor;

import com.eshore.common.constant.Constants;
import com.eshore.common.core.redis.RedisCache;
import com.eshore.common.utils.ip.IpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Map;

/**
 * @author Admin
 */
@Component
public class WebSocketInterceptor implements HandshakeInterceptor {



    @Value("${websocket.connectAuth:false}")
    protected boolean websocketConnectAuth;

    @Resource
    private RedisCache redisCache;
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_USER_IP = "userIp";
    public static final String SYSTEM_CLIENT_USER_ID="system";
    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(WebSocketInterceptor.class);

    /**
     * 握手之前
     *
     * @param request    request
     * @param response   response
     * @param wsHandler  handler
     * @param attributes 属性
     * @return 是否握手成功：true-成功，false-失败
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) {
        ServletServerHttpRequest serverHttpRequest = (ServletServerHttpRequest) request;

        //获取参数
        String userId = serverHttpRequest.getServletRequest().getParameter(KEY_USER_ID);
        String userIp=IpUtils.getIpAddress(request);
        log.error("beforeHandshake 客户端:【userId:{}】 连接信息 ip:{}", userId,userIp);


        //todo 这里根据自己业务对userId和pwd进行校验，校验通过则返回true, 失败返回false
        //....
        //这里放入uid是个人需求，因为在MyWebSocketHandler我有对在线人数进行统计需要，还有对每个session存储对应关系。
        attributes.put(KEY_USER_ID, userId);
        attributes.put(KEY_USER_IP, userIp);
        boolean available=true;
        if(websocketConnectAuth){
            available=isAvailableClient(userId);
        }
        log.info("beforeHandshake 客户端:【userId:{}】 连接鉴权是否通过:{}",userId, available);
        return available;
    }

    /**
     * 握手后
     *
     * @param request   request
     * @param response  response
     * @param wsHandler wsHandler
     * @param exception exception
     */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception exception) {
        log.info("afterHandshake handshake success!");
    }

    /**
     * 判断是否是合法的client 连接
     * @param
     * @param userId
     * @return
     */
    public boolean isAvailableClient(String userId) {
        boolean available = false;

        if(SYSTEM_CLIENT_USER_ID.equals(userId)){
            available=true;
        }else{

            Collection<String> keys = redisCache.keys(Constants.LOGIN_TOKEN_KEY+userId+":"+ "*");

            if(keys!=null&&keys.size()>0) {
                available=true;
            }

        }
        //如果要鉴权 记得注释掉该行代码
//        available=true;
        return available;
    }
}

