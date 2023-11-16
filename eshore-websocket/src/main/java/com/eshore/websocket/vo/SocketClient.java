package com.eshore.websocket.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author Admin
 */
public class SocketClient {
    @JsonIgnore
    private WebSocketSession webSocketSession;
    /**
     * 用户id
     */
    private String userId;


    /**
     *
     */
    private String userIp;

    /**
     * WebSocketHandler.clients map的key
     */
    private String id;

    public SocketClient(WebSocketSession webSocketSession,String userId) {
        this.webSocketSession = webSocketSession;
        this.userId = userId;
    }
    public SocketClient(WebSocketSession webSocketSession,String userId,String userIp) {
        this.webSocketSession = webSocketSession;
        this.userId = userId;
        this.userIp=userIp;
    }

    public WebSocketSession getWebSocketSession() {
        return webSocketSession;
    }

    public void setWebSocketSession(WebSocketSession webSocketSession) {
        this.webSocketSession = webSocketSession;
    }



    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return "uid-" + userId + "-uid" +"sid-"+ webSocketSession.getId()+ "-sid";
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    @Override
    public String toString() {
        return "SocketClient{" +
                "userId='" + userId + '\'' +
                ", userIp='" + userIp + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
