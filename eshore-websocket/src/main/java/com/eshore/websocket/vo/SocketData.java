package com.eshore.websocket.vo;

import lombok.Data;

/**
 * @author Admin
 */
@Data
public class SocketData {

    /**
     * 消息类型
     */
    private String type;

    /**
     * 转发目的地
     */
    private String target;

    /**
     *
     */
    private Object data;

    /**
     * 用于标记刷新的地方
     */
    private String tab;

    /**
     * 来源
     */
    private String source;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
