package com.eshore.websocket.handler;

import com.eshore.websocket.vo.SocketData;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public interface SocketDataHandler {

    void handle(WebSocketSession session, TextMessage message, SocketData socketData) throws Exception;
}
