package com.eshore.websocket.controller;

import com.eshore.common.core.response.CommonResult;
import com.eshore.common.core.response.ResponseGenerator;
import com.eshore.websocket.handler.WebSocketHandler;
import com.eshore.websocket.vo.SocketClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * websocketController
 *
 * @author eshore
 * @date 2023-05-09
 */

@Api(tags = "websocket模块")
@RestController
@RequestMapping("/websocket")
public class WebSocketController {

    /**
     * 查询websocket client列表
     */
    @ApiOperation(value = "查询websocket client列表")
    @GetMapping("/list")
    public CommonResult<List<SocketClient>> list() {

        return ResponseGenerator.genSuccessResult(WebSocketHandler.getClients().values().stream().collect(Collectors.toList()));

    }
}
