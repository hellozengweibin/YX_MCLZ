package com.eshore.feign.gd_telecom_ai.request;

import cn.hutool.core.annotation.Alias;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.eshore.feign.gd_telecom_ai.GrpApiConfig;
import com.eshore.feign.gd_telecom_ai.utils.GrpApiHelper;
import com.eshore.feign.gd_telecom_ai.utils.MapUtil;

import java.util.Map;
import java.util.UUID;

/**
 * @author shanglangxin
 * @since 2022/7/11 11:17
 */
public abstract class GrpRequest {

    /**
     * 接口配置, 设置参数是不用理会
     */
    protected GrpApiConfig config;

    /**
     * 请求 Id
     */
    protected String appRequestId;

    /**
     * 时间戳
     */
    protected long timestamp;

    /**
     * 用户 Id
     */
    private String userId;

    /**
     * 非必填 项目用户编码，待集团分配。若用户有拿到集团分配的用户编码，则这里传入，若没有拿到，则不用传入该参数，由省平台设置默认的编码传给集团
     */
    @Alias("project_id")
    private String projectId;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public GrpApiConfig getConfig() {
        return config;
    }

    public void setConfig(GrpApiConfig config) {
        this.config = config;
    }

    public String getAppRequestId() {
        return appRequestId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    /**
     * 处理入参，转为 map
     *
     * @return
     */
    public Map<String, Object> buildParams() {
        // 用户 Id 为空使用默认用户
        if (StrUtil.isBlank(userId)) {
            userId = config.getUserId();
        }
        Map<String, Object> params = MapUtil.objToMap(this);
        params.put("userId", userId);
        return params;
    }

    /**
     * 获取 request 的请求头
     *
     * @return
     */
    public Map<String, String> buildRequestHeaders() {
        if (ObjectUtil.isNull(config)) {
            throw new RuntimeException("接口配置属性不能为空");
        }
        timestamp = System.currentTimeMillis();
        appRequestId = StrUtil.replaceChars(UUID.randomUUID().toString(), "-", "");
        return GrpApiHelper.getCommonHeaderMap(this);
    }

}
