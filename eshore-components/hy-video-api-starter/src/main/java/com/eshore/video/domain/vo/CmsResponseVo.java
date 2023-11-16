package com.eshore.video.domain.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class CmsResponseVo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 2370487127314364816L;

    private JSONObject data;

    private String resultCode;

    private String resultMsg;
}
