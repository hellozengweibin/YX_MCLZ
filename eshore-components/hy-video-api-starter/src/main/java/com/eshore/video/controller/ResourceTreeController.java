package com.eshore.video.controller;


import com.eshore.common.core.controller.BaseController;
import com.eshore.video.constants.CommonResult;
import com.eshore.video.domain.vo.CmsResponseVo;
import com.eshore.video.domain.vo.GetPlaybackUrlDto;
import com.eshore.video.domain.vo.PTZHandlerDto;
import com.eshore.video.service.ResourceTreeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 【资源组模块】Controller
 *
 * @author xieyongjian
 * @date 2022-06-20
 */
@RestController
@RequestMapping("/hyvideo")
@Api(tags = "资源组模块")
public class ResourceTreeController extends BaseController {
    @Resource
    private ResourceTreeService resourceTreeService;


    @ApiOperation("获取实时流播放地址")
    @ApiImplicitParam(name = "deviceId", value = "国标编码", required = true, dataType = "java.lang.String")
    @GetMapping("/getRealPlayUrl")
    public CommonResult<CmsResponseVo> getRealPlayUrl(@RequestParam("deviceId") String deviceId) throws Exception {
        return resourceTreeService.getRealPlayUrl(deviceId);
    }

    @ApiOperation("获取实时流播放地址,可切换清晰度接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceId", value = "国标编码", required = true, dataType = "java.lang.String"),
            @ApiImplicitParam(name = "type", value = "视频清晰度1为主码流(高清)，2为子码流(标清)", required = false, dataType = "java.lang.String", example = "1")
    })
    @GetMapping("/getRealPlayUrlChange")
    public CommonResult<CmsResponseVo> getRealPlayUrl(@RequestParam("deviceId") String deviceId, @RequestParam("type") String type) throws Exception {
        return resourceTreeService.getRealPlayUrl(deviceId, type);
    }

    @ApiOperation("获取回看流播放地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceId", value = "国标编码", required = true, dataType = "java.lang.String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "startTime", value = "开始时间", required = true, dataType = "java.lang.String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", required = true, dataType = "java.lang.String")
    })
    @GetMapping("/getPlaybackUrl")
    public CommonResult<CmsResponseVo> getPlaybackUrl(GetPlaybackUrlDto dto) throws Exception {
        return resourceTreeService.getPlaybackUrl(dto);
    }


    @ApiOperation("云台控制")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceId", value = "国标编码", required = true, dataType = "java.lang.String"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", required = true, dataType = "java.lang.String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", required = true, dataType = "java.lang.String")
    })
    @PostMapping("/PTZHandler")
    public CommonResult<CmsResponseVo> PTZHandler(@RequestBody PTZHandlerDto ptzDto) {
        return resourceTreeService.ptzHandler(ptzDto);
    }

    @ApiOperation("获取回看留时段视频")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceId", value = "国标编码", required = true, dataType = "java.lang.String"),
            @ApiImplicitParam(name = "time", value = "时间", required = true, dataType = "java.lang.String")
    })
    @GetMapping("/getTimeSlice")
    public CommonResult getTimeSlice(@RequestParam("deviceId") String deviceId, @RequestParam("time") String time) throws Exception {
        return resourceTreeService.getTimeSlice(deviceId, time);
    }


}
