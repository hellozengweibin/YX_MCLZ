package com.eshore.websocket.config;

import com.eshore.websocket.handler.WebSocketHandler;
import com.eshore.websocket.interceptor.WebSocketInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.Resource;

/**
 * @author Admin
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    /**
     * 注入拦截器
     */
    @Resource
    private WebSocketInterceptor webSocketInterceptor;
    @Resource
    private WebSocketHandler webSocketHandler;


    // 配置信息
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {

        webSocketHandlerRegistry
                //添加myHandler消息处理对象，和websocket请求的访问路径访问地址
                .addHandler(webSocketHandler, "/ws")
                //设置允许跨域访问
                .setAllowedOrigins("*")
                //添加拦截器可实现用户链接前进行权限校验等操作
                .addInterceptors(webSocketInterceptor);
    }

//    // 处理器
//    @Bean
//    public org.springframework.web.socket.WebSocketHandler myHandler() {
//        return new WebSocketHandler();
//    }

}


