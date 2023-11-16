package com.eshore.common.utils.minio.demo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @Author cjy
 * @Date 2022/7/12 17:46
 * @Version 1.0
 */
@Data
@ConfigurationProperties(prefix = "minio")
public class MinIOConfigProperties {

    private String accessKey;
    private String secretKey;
    private List<String> bucket;
    private String apiFileIp; //MinIO服务所在地址
    private Integer urlExpiredSeconds;
    private String trustFileType;
    private Integer dayOffSet;
    private String outputAddress;  // 对外访问地址
//    private String readPath ;
}
