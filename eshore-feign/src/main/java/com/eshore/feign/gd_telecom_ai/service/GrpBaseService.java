package com.eshore.feign.gd_telecom_ai.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.eshore.feign.gd_telecom_ai.GrpApiConfig;
import com.eshore.feign.gd_telecom_ai.request.GrpRequest;
import com.eshore.feign.gd_telecom_ai.response.GrpCommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Optional;

/**
 * @author shanglangxin
 * @since 2022/7/12 15:30
 */
@Slf4j
public class GrpBaseService {

    @Autowired
    protected GrpApiConfig config;

    protected GrpCommonResponse doPostReturnNotNull(String url, GrpRequest request) {
        GrpCommonResponse response = doPost(url, request);
        if (StrUtil.isBlank(response.getResult())) {
            throw new RuntimeException(String.format("appRequestId: %s 获取请求结果为空", response.getAppRequestId()));
        }
        return response;
    }

    protected GrpCommonResponse doPost(String url, GrpRequest request) {
        Map<String, String> headers = request.buildRequestHeaders();
        String paramsJson = JSONUtil.toJsonStr(request.buildParams());
//        log.info("执行 post 请求 url: {}, header: {}, params: {}", url, headers, paramsJson);
        HttpResponse response = HttpUtil.createPost(url)
                .headerMap(headers, false)
                .body(paramsJson).execute();
        return responseConversion(response, request.getAppRequestId());
    }

    private GrpCommonResponse responseConversion(HttpResponse response, String appRequestId) {
        if (StrUtil.isBlank(response.body())) {
            throw new RuntimeException(String.format("appRequestId: %s 接口未返回任何数据!", appRequestId));
        }
        log.info("appRequestId: {}, 获取返回结果: {}", appRequestId, response.body());
        GrpCommonResponse grpResponse = JSONUtil.toBean(response.body(), GrpCommonResponse.class);
        appRequestId = Optional.ofNullable(grpResponse.getAppRequestId()).orElse(appRequestId);
        if (!grpResponse.isSuccess()) {
            throw new RuntimeException(String.format("appRequestId: %s 请求失败, 失败原因: %s", appRequestId, grpResponse.getMessage()));
        }
        if (StrUtil.isBlank(grpResponse.getResult())) {
            log.info("appRequestId: {} 获取返回结果为空", appRequestId);
        }
        return grpResponse;
    }

}
