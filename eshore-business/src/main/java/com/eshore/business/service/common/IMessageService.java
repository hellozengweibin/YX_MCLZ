package com.eshore.business.service.common;


import com.eshore.common.core.response.CommonResult;
import com.eshore.domain.model.vo.common.MessageSendVo;

public interface IMessageService {

    /**
     * 短信验证码校验
     *
     * @param messageSendVo
     * @param isValidatedDelete 是否校验通过后删除缓存
     * @return
     */
    boolean validMessageCode(MessageSendVo messageSendVo, boolean isValidatedDelete);

    /**
     * 发送短信验证码
     *
     * @param messageSendVo
     * @return
     */
    CommonResult getMessageCode(MessageSendVo messageSendVo);

    /**
     * 调用短信网关发送短信
     *
     * @param phoneNum
     * @param content
     * @param messageSendVo
     * @return
     */
    CommonResult sendMessage(String phoneNum, String content, MessageSendVo messageSendVo);

}
