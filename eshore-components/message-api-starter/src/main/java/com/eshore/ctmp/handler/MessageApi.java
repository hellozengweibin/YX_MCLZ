package com.eshore.ctmp.handler;

import com.eshore.ctmp.model.MessageVo;

import java.io.IOException;

/**
 * @InterfaceName MessageApi
 * @Description 短信接口
 * @Author jianlin.liu
 * @Date 2023/4/21
 * @Version 1.0
 **/
public interface MessageApi {

    /**
     * 连接短信网关服务
     *
     * @return
     * @throws IOException
     */
    int connect_() throws IOException;

    /**
     * 发送短信操作
     *
     * @param messageVo
     * @return
     * @throws IOException
     */
    int sendMsg(MessageVo messageVo);

}
