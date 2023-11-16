package com.eshore.websocket.handler;


import com.alibaba.fastjson.JSONObject;
import com.eshore.websocket.vo.MsgType;
import com.eshore.websocket.vo.SocketData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * 心跳 socket
 *
 * @author Admin
 */
@Slf4j
@Service(MsgType.HEART)
public class SocketDataHeartMessageHandler extends AbstractSocketDataHander implements SocketDataHandler {


    @Override
    public void handle(WebSocketSession session, TextMessage message, SocketData socketData) throws Exception {
        SocketData data = new SocketData();
        data.setType(MsgType.HEART);
        String tempMessage = JSONObject.toJSONString(data);
        TextMessage tempTextMessage = new TextMessage(tempMessage);
        session.sendMessage(tempTextMessage);
    }
}
