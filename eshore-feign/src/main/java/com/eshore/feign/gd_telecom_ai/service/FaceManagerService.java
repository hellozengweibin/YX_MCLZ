package com.eshore.feign.gd_telecom_ai.service;

import cn.hutool.json.JSONUtil;
import com.eshore.feign.gd_telecom_ai.request.*;
import com.eshore.feign.gd_telecom_ai.response.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * todo url 配置
 *
 * @author shanglangxin
 * @since 2022/7/11 9:53
 */
@Slf4j
@Service
public class FaceManagerService extends GrpBaseService {

    public SaveFaceResponse saveFace(SaveFaceRequest request) {
        request.setConfig(config);
        GrpCommonResponse response = doPostReturnNotNull("https://aicenter.gdtel.com:10443/v1/gdai/api/2xhopfk28x04/create_feature", request);
        return JSONUtil.toBean(response.getResult(), SaveFaceResponse.class);
    }

    public DeleteFaceResponse deleteFace(DeleteFaceRequest request) {
        request.setConfig(config);
        GrpCommonResponse response = doPostReturnNotNull("https://aicenter.gdtel.com:10443/v1/gdai/api/2xhoqxlqa70g/delete_feature", request);
        return JSONUtil.toBean(response.getResult(), DeleteFaceResponse.class);
    }

    public SearchFaceResponse searchFace(SearchFaceRequest request) {
        request.setConfig(config);
        GrpCommonResponse response = doPostReturnNotNull("https://aicenter.gdtel.com:10443/v1/gdai/api/2xhoscy1iww0/search_feature", request);
        return JSONUtil.toBean(response.getResult(), SearchFaceResponse.class);
    }

    public FindByPicResponse findByPic(FindByPicRequest request) {
        request.setConfig(config);
        GrpCommonResponse response = doPostReturnNotNull("https://aicenter.gdtel.com:10443/v1/gdai/api/2xe5yagx0p44/retrieval_feature", request);
        return JSONUtil.toBean(response.getResult(), FindByPicResponse.class);
    }

    public AnalyseImagesForFaceResponse analyseImagesForFace(AnalyseImagesForFaceRequest request) {
        request.setConfig(config);
        GrpCommonResponse response = doPostReturnNotNull("https://aicenter.gdtel.com:10443/v1/gdai/api/2xhjx30mps00/get_feature", request);
        return JSONUtil.toBean(response.getResult(), AnalyseImagesForFaceResponse.class);
    }

}
