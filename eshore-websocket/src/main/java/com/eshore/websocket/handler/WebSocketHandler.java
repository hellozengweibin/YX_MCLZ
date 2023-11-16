package com.eshore.websocket.handler;

import com.alibaba.fastjson.JSONObject;
import com.eshore.websocket.interceptor.WebSocketInterceptor;
import com.eshore.websocket.utils.AesUtil;
import com.eshore.websocket.vo.SocketClient;
import com.eshore.websocket.vo.SocketData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * websocket连接，关闭，消息接收
 *
 * @author Admin
 */
@Component
public class WebSocketHandler extends TextWebSocketHandler {



    @Value("${websocket.dataEncrypt:false}")
    protected boolean websocketDataEncrypt;

    @Value("${websocket.dataEncryptKey:e577774eaa06fd6c557539af6929681d}")
    protected String websocketDataEncryptKey;

    @Autowired
    private final Map<String, SocketDataHandler> handlerMap = new HashMap<>();
    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(WebSocketHandler.class);

    /**
     * 静态变量，用来记录当前在线连接数
     */
    private static AtomicInteger onlineNum = new AtomicInteger();

    /**
     * 存放每个客户端连接对象
     */
    private static ConcurrentHashMap<String, SocketClient> clients = new ConcurrentHashMap<>();

    /**
     * 在线客户端加一
     */
    public static void addOnlineCount() {
        onlineNum.incrementAndGet();
        log.info("addOnlineCount onlineNum:{}",onlineNum.get());
    }

    /**
     * 在线客户端减一
     */
    public static void subOnlineCount() {
        onlineNum.decrementAndGet();
        log.info("subOnlineCount onlineNum:{}",onlineNum.get());
    }

    /**
     * 接受客户端消息
     * 客户端给服务器端发送消息时，就会执行以下方法,作为逻辑处理
     * @param session session
     * @param message message
     * @throws IOException e
     */
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // todo session.sendMessage(new TextMessage(“已收到服务端发送的消息”));
        String content = message.getPayload(); // 接收客户端发送的消息，作为逻辑判断
        if(websocketDataEncrypt){
            content=AesUtil.decryptByKey(content,websocketDataEncryptKey);
        }
        SocketData socketData = JSONObject.parseObject(content, SocketData.class);
        String type = socketData.getType();
        SocketDataHandler socketDataHandler = handlerMap.get(type);
        log.info("handleTextMessage 服务端收到客户端:【sessionId:{} 】content:{}",session.getId(),content);
        if (socketDataHandler == null) {
            log.warn("handleTextMessage 未找到topic类型【{}】 对应的处理类", type);
            return;
        }
        socketDataHandler.handle(session, message, socketData);
    }

    /**
     * 建立连接后
     *
     * @param session session
     * @throws Exception e
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        String userId = getAttributes(session, WebSocketInterceptor.KEY_USER_ID);
        String userIp = getAttributes(session, WebSocketInterceptor.KEY_USER_IP);
        SocketClient client = new SocketClient(session,userId,userIp);
        SocketClient put = clients.put(client.getId(), client);
        if (put == null) {
            log.info("afterConnectionEstablished 客户端:【clientId:{}】连接成功",client.getId());

            addOnlineCount();
        }
    }

    /**
     * 连接关闭后
     *
     * @param session session
     * @param status  status
     * @throws Exception e
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        getSessionBy(session.getId()).forEach(client -> {

            log.info("afterConnectionClosed 客户端:【clientId:{}】连接关闭",client.getId());
            clients.remove(client.getId());
            subOnlineCount();

        });
    }


    /**
     * 信息发送的方法，通过filters条件
     * 拿到其对应的session，调用信息推送的方法
     *
     * @param message
     * @param filters
     */
    public synchronized void sendMessage(String message, String filters) {
        // 服务端发送的消息
        sendMessage(new TextMessage(message), filters);


    }

    /**
     * 信息发送的方法，通过客户端的userId
     * 拿到其对应的session，调用信息推送的方法
     *
     * @param message
     * @param userId
     */
    public synchronized void sendMessageByUserId(String message, String userId) {
        sendMessage(new TextMessage(message), "uid-" + userId + "-uid");
    }
    public synchronized void sendMessageByUserId(String message, Long userId) {
        sendMessage(new TextMessage(message), "uid-" + userId + "-uid");
    }

    /**
     * 信息发送的方法，通过模糊过滤查找客户端
     * 拿到其对应的session，调用信息推送的方法
     *
     * @param message
     * @param filters
     */
    public synchronized void sendMessage(TextMessage message, String filters) {

        getSessionBy(filters).forEach(client -> {

            try {
                if(client.getWebSocketSession().isOpen()){
                    if(websocketDataEncrypt){
                        String payload=message.getPayload();
                        //payload=加密后的数据
                        payload= AesUtil.encryptByKey(payload,websocketDataEncryptKey);
                        TextMessage tempTextMessage=new TextMessage(payload);
                        client.getWebSocketSession().sendMessage(tempTextMessage);
                    }else{
                        client.getWebSocketSession().sendMessage(message);
                    }
                    log.info("sendMessage 服务端推送给客户端:【clientId:{}】 content:{} filters:{}", client.getId(),message.getPayload(),filters);

                }else{
                    log.info("client is not open sendMessage 服务端推送给客户端:【clientId:{}】 content:{} filters:{}", client.getId(),message.getPayload(),filters);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }

    /**
     * 模糊匹配查询出SocketClient
     * @param filters
     * @return
     */
    public List<SocketClient> getSessionBy(String filters) {
        List<SocketClient> list = clients.entrySet().stream()
                .filter((e) -> checkKey(e.getKey(), filters)).
                        collect(Collectors.mapping(Map.Entry::getValue, Collectors.toList()));
        log.info("getSessionBy by:{} list:{}",filters,list);
        return list;

    }

    /**
     * 通过indexof匹配想要查询的字符
     */
    private static boolean checkKey(String key, String filters) {
        if (key.indexOf(filters) > -1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取ws连接参数
     * @param session
     * @param key
     * @return
     */
    private String getAttributes(WebSocketSession session, String key) {
        return session.getAttributes().get(key).toString();
    }

    public static ConcurrentHashMap<String, SocketClient> getClients() {
        return clients;
    }
}


