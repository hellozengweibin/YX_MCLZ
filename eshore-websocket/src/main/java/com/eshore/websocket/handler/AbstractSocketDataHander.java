package com.eshore.websocket.handler;


import com.eshore.websocket.vo.SocketData;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author Admin
 */
@Component
public class AbstractSocketDataHander implements SocketDataHandler{


    @Override
    public void handle(WebSocketSession session, TextMessage message, SocketData socketData) throws Exception {

    }
}
