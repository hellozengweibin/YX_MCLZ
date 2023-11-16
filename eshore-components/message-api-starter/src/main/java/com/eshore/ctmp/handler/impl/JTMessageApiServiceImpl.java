package com.eshore.ctmp.handler.impl;

import cn.hutool.http.HttpRequest;
import com.eshore.ctmp.config.JTMessageProperties;
import com.eshore.ctmp.handler.AbstractMessageService;
import com.eshore.ctmp.handler.MessageApi;
import com.eshore.ctmp.model.MessageVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @ClassName JTMessageApiServiceImpl
 * @Description 集团网关发送短信实现
 * @Author jianlin.liu
 * @Date 2023/4/21
 * @Version 1.0
 **/
@Slf4j
public class JTMessageApiServiceImpl extends AbstractMessageService<JTMessageProperties> implements MessageApi {

    private JTMessageProperties jtMessageProperties;

    public void setJtMessageProperties(JTMessageProperties jtMessageProperties) {
        this.jtMessageProperties = jtMessageProperties;
    }

    public JTMessageApiServiceImpl() {

    }

    public JTMessageApiServiceImpl(JTMessageProperties jtMessageProperties) {
        this.buildConfig(jtMessageProperties);
    }


    @Override
    public int connect_() throws IOException {
        return 0;
    }

    @Override
    public int sendMsg(MessageVo messageVo) {
        if (messageVo == null) {
            log.error(">>>>>>>>>消息体格式不能为空 => {}", messageVo);
            return 0;
        }
        String content = messageVo.getContent();
        long result = -10000;
        String userid = jtMessageProperties.getAccount();
        int msgFmt = 8;
        String password = jtMessageProperties.getPassword();
        String message = jtMessageProperties.getMsgPrefix() + content;
        String mobile = messageVo.getPhone();
        String charset = msgFmt == 15 ? "gbk" : "utf-8";
        String ext = "";
        try {
            message = URLEncoder.encode(message, charset);
        } catch (UnsupportedEncodingException e) {
            log.error("调用服务发送短信失败,原因:" + e.getMessage());
        }
        String param = String.format("userid=%s&passwordMd5=%s&msg_fmt=%s&message=%s&mobile=%s&ext=", userid, password, msgFmt, message, mobile, ext);
        // 调用接口，注意：请求内容类型content-type为application/x-www-form-urlencoded
        String tempResult = HttpRequest.post(jtMessageProperties.getUrl()).body(param).execute().body();

        log.info("[sendMessage]====>>>>>>>>result:{}", tempResult);
        if (StringUtils.isNotEmpty(tempResult)) {
            result = Long.valueOf(tempResult);
        }
        if (result > 0) {
            return 1;
        } else {
            log.error("调用服务发送短信失败 => param:{},原因:" + tempResult, param);
            return 0;
        }
    }

    @Override
    protected JTMessageProperties buildConfig(JTMessageProperties properties) {
        if (properties == null) {
            throw new RuntimeException("短信配置项信息不能为空");
        }
        log.info(">>>>>>>>>>短信网关配置属性：{}", properties);
        this.jtMessageProperties = properties;
        return this.jtMessageProperties;
    }
}
