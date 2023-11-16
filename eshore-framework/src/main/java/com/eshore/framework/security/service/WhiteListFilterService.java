package com.eshore.framework.security.service;

import com.eshore.common.utils.StringUtils;
import com.eshore.framework.config.WhiteListConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ClassName WhiteListFilterService
 * @Description 白名单过滤service
 * @Author jianlin.liu
 * @Date 2023/7/19
 * @Version 1.0
 **/
@Slf4j
@Component
public class WhiteListFilterService {

    @Resource
    private WhiteListConfig whiteListConfig;

    /**
     * 检查是否为白名单
     *
     * @param request
     * @return
     */
    public boolean verifyWhiteList(HttpServletRequest request) {
        String origin = request.getHeader("Origin");
        String referer = request.getHeader("Referer");

        // 未设置白名单，直接通过
        if (!whiteListConfig.isEnable()) return true;

        boolean isRefererInWhiteList = false;
        boolean isOriginInWhiteList = false;

        List<String> whiteListPaths = whiteListConfig.getPaths();
        if (StringUtils.isNotEmpty(referer)) {
            for (String whiteListPath : whiteListPaths) {
                // 判断refer是否存在于白名单
                isRefererInWhiteList = referer.contains(whiteListPath);
                if (isRefererInWhiteList) {
                    break;
                }
            }
        } else {
            isRefererInWhiteList = true;
        }

        if (StringUtils.isNotEmpty(origin)) {
            for (String whiteListPath : whiteListPaths) {
                isOriginInWhiteList = origin.contains(whiteListPath);
                if (isOriginInWhiteList) {
                    break;
                }
            }
        } else {
            isOriginInWhiteList = true;
        }

        // log.info("checkWhiteList Referer:{}  isRefererInWhiteList:{} ; Origin:{} isRefererInWhiteList:{}", referer, isRefererInWhiteList, origin, isOriginInWhiteList);
        return isOriginInWhiteList && isRefererInWhiteList;
    }

}
