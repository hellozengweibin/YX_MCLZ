package com.eshore.common.utils.minio.demo;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * @Author cjy
 * @Date 2022/7/12 17:47
 * @Version 1.0
 */
@Data
@Configuration
@EnableConfigurationProperties({MinIOConfigProperties.class})
public class MinIOConfig {
    @Autowired
    private MinIOConfigProperties minIOConfigProperties;

    @Bean
    public MinioClient buildMinioClient() {
        // 创建minio连接
        MinioClient minioClient =
                MinioClient
                        .builder()
                        .credentials(minIOConfigProperties.getAccessKey(), minIOConfigProperties.getSecretKey())
                        .endpoint(minIOConfigProperties.getApiFileIp())
                        .build();

        createBucket(minioClient, minIOConfigProperties);

        return minioClient;
    }

    /**
     * 初始化Bucket
     *
     * @throws Exception 异常
     */
    private void createBucket(MinioClient minioClient, MinIOConfigProperties minIOConfigProperties) {
        if (ObjectUtils.isNotEmpty(minIOConfigProperties)) {
            List<String> bucketList = minIOConfigProperties.getBucket();
            for (String bucket : bucketList) {
                try {
                    if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
                        minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
                    }
                } catch (ErrorResponseException e) {
                    e.printStackTrace();
                } catch (InsufficientDataException e) {
                    e.printStackTrace();
                } catch (InternalException e) {
                    e.printStackTrace();
                } catch (InvalidBucketNameException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (InvalidResponseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (ServerException e) {
                    e.printStackTrace();
                } catch (XmlParserException e) {
                    e.printStackTrace();
                } catch (RegionConflictException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
