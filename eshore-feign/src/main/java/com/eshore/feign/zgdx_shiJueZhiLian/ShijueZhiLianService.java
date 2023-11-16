package com.eshore.feign.zgdx_shiJueZhiLian;

import com.eshore.feign.zgdx_shiJueZhiLian.req.AlarmQueryReq;

public interface ShijueZhiLianService {
    /**
     * 告警信息查询
     */
    void alarmQuery(AlarmQueryReq req);
}
