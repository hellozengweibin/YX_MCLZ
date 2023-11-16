package com.eshore.web.controller.common;

import com.eshore.business.service.common.IMessageService;
import com.eshore.common.core.controller.BaseController;
import com.eshore.common.core.response.CommonResult;
import com.eshore.domain.model.vo.common.MessageSendVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 验证码操作处理
 *
 * @author eshore
 */
@Api(value = "/message", tags = {"短信验证码相关"})
@RestController
@RequestMapping("/message")
public class MessageController extends BaseController {


    @Autowired
    private IMessageService messageSendService;

    /**
     * 获取验证码
     */
    @ApiOperation(value = "发送短信验证码", notes = "发送短信验证码", httpMethod = "POST")
    @PostMapping("/getMessageCode")
    // @Anonymous
    public CommonResult getMessageCode(@RequestBody @Validated MessageSendVo messageSendVo) {
        return messageSendService.getMessageCode(messageSendVo);
    }

    /**
     * 获取验证码
     */
    // @Anonymous
    @ApiOperation(value = "校验验证码", notes = "获取验证码", httpMethod = "POST")
    @PostMapping("/validMessageCode")
    public CommonResult validMessageCode(@RequestBody @Validated MessageSendVo messageSendDto) {
        return toResult(messageSendService.validMessageCode(messageSendDto, false));

    }
}
