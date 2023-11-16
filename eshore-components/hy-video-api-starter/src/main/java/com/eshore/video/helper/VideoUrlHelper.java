package com.eshore.video.helper;

import cn.hutool.core.util.StrUtil;
import com.eshore.video.constants.AreaCodeEnum;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Objects;

/**
 * @author shanglangxin
 * @since 2022/10/24 15:04
 */
@Slf4j
@Component
public class VideoUrlHelper {

    private final Map<String, VideoRequestUrl> urlByAreaCode = Maps.newHashMapWithExpectedSize(21);

    @Value("${cms.url-config.default.ip}")
    private String defaultIp;

    @Value("${cms.url-config.default.timeIp}")
    private String defaultTimeIp;

    @Value("${cms.url-config.default.forOutIp}")
    private String defaultForOutIp;

    @Value("${cms.url-config.default.netType}")
    private Integer defaultNetType;

    @Autowired
    private Environment environment;

    @PostConstruct
    public void init() {
        // 查询层级底下的地市编号
        int index = 1;
        while (true) {
            String prefix = "cms.url-config.config-" + index;
            String areaCode = environment.getProperty(prefix + ".areaCode");
            if (StrUtil.isBlank(areaCode)) {
                break;
            }
            VideoRequestUrl url = new VideoRequestUrl();
            url.setTimeIp(environment.getProperty(prefix + ".timeIp", defaultTimeIp));
            url.setIp(environment.getProperty(prefix + ".ip", defaultIp));
            url.setForOutIp(environment.getProperty(prefix + ".forOutIp", defaultForOutIp));
            url.setNetType(environment.getProperty(prefix + ".netType", Integer.class, defaultNetType));
            urlByAreaCode.put(areaCode, url);
            index++;
            log.info("加载 areaCode: {}, 配置: {}", areaCode, url);
        }
    }

    public String getCmsIpByAreaCode(String areaCode) {
        String name = AreaCodeEnum.getAliasByCode(areaCode);
        VideoRequestUrl videoRequestUrl = urlByAreaCode.get(areaCode);
        String url = Objects.isNull(videoRequestUrl) ? defaultIp : videoRequestUrl.getIp();
        return String.format(url, name);
    }

    public String getCmsTimeIpByAreaCode(String areaCode) {
        String name = AreaCodeEnum.getAliasByCode(areaCode);
        VideoRequestUrl videoRequestUrl = urlByAreaCode.get(areaCode);
        String url = Objects.isNull(videoRequestUrl) ? defaultTimeIp : videoRequestUrl.getTimeIp();
        return String.format(url, name);
    }

    public String getForOutIpByAreaCode(String areaCode) {
        String name = AreaCodeEnum.getAliasByCode(areaCode);
        VideoRequestUrl videoRequestUrl = urlByAreaCode.get(areaCode);
        String url = Objects.isNull(videoRequestUrl) ? defaultForOutIp : videoRequestUrl.getForOutIp();
        return String.format(url, name);
    }

    public Integer getNetTypeByAreaCode(String areaCode) {
        VideoRequestUrl videoRequestUrl = urlByAreaCode.get(areaCode);
        return Objects.isNull(videoRequestUrl) ? defaultNetType : videoRequestUrl.getNetType();
    }

    @Data
    static class VideoRequestUrl {
        private String ip;

        private String timeIp;

        private String forOutIp;

        private Integer netType;
    }

}
