package com.eshore.web.controller.monitor;

import com.eshore.common.core.response.ResponseGenerator;
import com.eshore.common.core.response.CommonResult;
import com.eshore.framework.web.domain.Server;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 服务器监控
 *
 * @author eshore
 */
@RestController
@RequestMapping("/monitor/server")
public class ServerController {
    @PreAuthorize("@ss.hasPermi('monitor:server:list')")
    @GetMapping()
    public CommonResult<?> getInfo() throws Exception {
        Server server = new Server();
        server.copyTo();
        return ResponseGenerator.genSuccessResult(server);
    }
}
