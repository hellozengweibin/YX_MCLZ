package com.eshore.ctmp.model;

import lombok.Data;

/**
 * @ClassName MessageVo
 * @Description 短信消息实体
 * @Author jianlin.liu
 * @Date 2023/4/21
 * @Version 1.0
 **/
@Data
public class MessageVo {
    /**
     * 流水号
     */
    private int sequenceNumber = 0;
    /**
     * 目标手机号
     */
    private String phone;

    /**
     * 短信内容
     */
    private String content;

    /**
     * 优先级
     */
    private byte priority = 3;

    /**
     * 消息格式
     */
    private byte msgFmt = 8;
}
