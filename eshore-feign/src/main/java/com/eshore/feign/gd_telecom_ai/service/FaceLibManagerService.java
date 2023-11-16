package com.eshore.feign.gd_telecom_ai.service;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.eshore.feign.gd_telecom_ai.request.DeleteFaceLibRequest;
import com.eshore.feign.gd_telecom_ai.request.SaveFaceLibRequest;
import com.eshore.feign.gd_telecom_ai.request.SearchFaceLibRequest;
import com.eshore.feign.gd_telecom_ai.response.GrpCommonResponse;
import com.eshore.feign.gd_telecom_ai.response.SaveFaceLibResponse;
import com.eshore.feign.gd_telecom_ai.response.SearchFaceLibResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shanglangxin
 * @since 2022/7/12 19:42
 */
@Slf4j
@Service
public class FaceLibManagerService extends GrpBaseService {

    public SaveFaceLibResponse saveFaceLib(SaveFaceLibRequest request) {
        request.setConfig(config);
        GrpCommonResponse response = doPostReturnNotNull("https://aicenter.gdtel.com:10443/v1/gdai/api/2xhjz306mcqo/create_index", request);
        return JSONUtil.toBean(response.getResult(), SaveFaceLibResponse.class);
    }

    public boolean deleteFaceLib(DeleteFaceLibRequest request) {
        request.setConfig(config);
        doPost("https://aicenter.gdtel.com:10443/v1/gdai/api/2xhokoteqbs4/delete_index", request);
        return true;
    }

    public List<SearchFaceLibResponse> searchFaceLib(SearchFaceLibRequest request) {
        request.setConfig(config);
        GrpCommonResponse response = doPostReturnNotNull("https://aicenter.gdtel.com:10443/v1/gdai/api/2xhonq4w97uo/search_index", request);
        return JSONObject.parseArray(response.getResult(), SearchFaceLibResponse.class);
    }
}
