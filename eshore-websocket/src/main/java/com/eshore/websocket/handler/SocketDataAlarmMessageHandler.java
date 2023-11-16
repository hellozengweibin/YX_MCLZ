package com.eshore.websocket.handler;


import com.eshore.websocket.vo.MsgType;
import com.eshore.websocket.vo.SocketData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;

/**
 * 告警记录socket
 * @author Admin
 */
@Slf4j
@Service(MsgType.ALARM)
public class SocketDataAlarmMessageHandler extends com.eshore.websocket.handler.AbstractSocketDataHander implements com.eshore.websocket.handler.SocketDataHandler {

    @Resource
    WebSocketHandler myHandler;

    @Override
    public void handle(WebSocketSession session, TextMessage message, SocketData socketData) throws Exception {
//        myHandler.sendMessage(message,socketData.getTarget());
        //处理逻辑
    }
}
