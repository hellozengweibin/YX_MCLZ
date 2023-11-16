package com.eshore.business.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.eshore.common.utils.Assert;
import com.eshore.common.utils.minio.demo.ImageConstant;
import com.eshore.common.utils.minio.demo.MinIOConfigProperties;
import com.eshore.common.utils.minio.demo.service.MinIOFileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author shanglangxin
 * @since 2022/10/18 20:02
 */
@Component
public class MinIOUtil {

    @Autowired
    private MinIOConfigProperties minIOConfigProperties;

    @Autowired
    private MinIOFileStorageService minIOFileStorageService;

    private final static String separator = "/";

    /**
     * 获取文件路径
     * 返回的是临时的地址
     *
     * @param list
     * @param source 字段名称来源, get
     * @param target 设置属性, set
     * @param <T>
     */
    public <T> void handleObjectUrl(List<T> list, Function<T, String> source, BiConsumer<T, String> target) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        for (T t : list) {
            if (Objects.isNull(t)) {
                return;
            }
            String apply = source.apply(t);
            target.accept(t, getObjectUrl(apply));
        }
    }

    public <T> void handleObjectUrl(List<T> list, Function<T, String> source, BiConsumer<T, String> target, Integer bucket) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        for (T t : list) {
            if (Objects.isNull(t)) {
                return;
            }
            String apply = source.apply(t);
            target.accept(t, getObjectUrl(apply, bucket));
        }
    }


    /**
     * 返回永久连接
     */
    public <T> void toPermUrl(List<T> list, Function<T, String> source, BiConsumer<T, String> target) {
        if (Objects.isNull(list)) {
            return;
        }
        for (T t : list) {
            if (Objects.isNull(t)) {
                return;
            }
            String apply = source.apply(t);
            target.accept(t, buildPermUrl(apply));
        }
    }

    /**
     * 获取不过期路径
     */
    public String buildPermUrl(String filePath) {
        return minIOConfigProperties.getApiFileIp() + "/" + minIOConfigProperties.getBucket().get(0) + "/" + filePath;
    }


    /**
     * 因为minio设置的访问限制，所以每次访问图片都需要先获取图片链接
     *
     * @param filePath 文件路径(桶之后的路径)
     * @return
     */
    public String getObjectUrl(String filePath) {
        String alarmBucket = minIOConfigProperties.getBucket().get(0);
        return getObjectUrl(alarmBucket, filePath);
    }

    public String getObjectUrl(String filePath, Integer bucket) {
        String alarmBucket = minIOConfigProperties.getBucket().get(bucket);
        return getObjectUrl(alarmBucket, filePath);
    }

    /**
     * 获取文件URL
     *
     * @param bucket   同名称
     * @param filePath 文件路径(桶之后的路径)
     * @return
     */
    public String getObjectUrl(String bucket, String filePath) {

        String url= minIOFileStorageService.getObjectUrl(bucket, filePath, minIOConfigProperties.getUrlExpiredSeconds(), TimeUnit.SECONDS);
        url = toOutputUrl(url);
        return url;
    }

    public String getObjectUrl(String filePath, Integer expiry, TimeUnit timeUnit) {
        return minIOFileStorageService.getObjectUrl(minIOConfigProperties.getBucket().get(0), filePath, expiry, timeUnit);
    }

    public String getObjectName(String url) {
        String key = url.replace(minIOConfigProperties.getApiFileIp() + "/", "");
        int index = key.indexOf(separator);
        String filePath = key.substring(index + 1);
        return filePath.split("\\?")[0];
    }

    public String getBucketName(String url) {
        String key = url.replace(minIOConfigProperties.getApiFileIp() + "/", "");
        int index = key.indexOf(separator);
        return key.substring(0, index);
    }

    public String refreshUrl(String url) {
        return getObjectUrl(getObjectName(url));
    }

    public boolean isTrustFileType(String type) {
        return minIOConfigProperties.getTrustFileType().contains(type);
    }

    public String uploadImageToAlarm(byte[] image) throws IOException {
        return uploadImage(minIOConfigProperties.getBucket().get(0), image);
    }


    /**
     * 上传base64到告警bucket
     */
    public String uploadBase64ToAlarm(String type,String base64) throws IOException {
        return uploadBase64Image(minIOConfigProperties.getBucket().get(0),type, base64);
    }


    public String uploadBase64Image(String bucket, String type,String base64Image) throws IOException {
        Assert.isFalse(StrUtil.isNotBlank(base64Image), "告警图片为空");
        byte[] decode = Base64.decode(base64Image);
        try (ByteArrayInputStream is = new ByteArrayInputStream(decode)) {
            return minIOFileStorageService.uploadImgFile(bucket, "", type+"-"+UUID.randomUUID().toString().replace("-", "") + ImageConstant.JPG, is);
        }
    }


    public String uploadImage(String bucket, byte[] image) throws IOException {
        Assert.isTrue(ArrayUtil.isEmpty(image), "告警图片为空");
        try (ByteArrayInputStream is = new ByteArrayInputStream(image)) {
            String s = minIOFileStorageService.uploadImgFile(bucket, "", UUID.randomUUID().toString().replace("-", "") + ImageConstant.JPG, is);
            return s;
        }
    }

    /**
     * 通过完整的 minIO url 下载文件
     *
     * @param fileUrl
     * @return
     */
    public byte[] download(String fileUrl) {
        return minIOFileStorageService.downloadFile(getObjectName(fileUrl));
    }

    /**
     * 通过完整的 minIO url 下载文件
     *
     * @param filePath
     * @return
     */
    public byte[] download(String bucket, String filePath) {
        return minIOFileStorageService.downloadFile(bucket, filePath);
    }

    public void removeAiPlatform(List<String> filePathList) {
        minIOFileStorageService.delete(minIOConfigProperties.getBucket().get(0), filePathList);
    }

    public void removeAlarm(List<String> filePathList) {
        minIOFileStorageService.delete(minIOConfigProperties.getBucket().get(1), filePathList);
    }

    public String toOutputUrl(String url) {
        return url.replace(minIOConfigProperties.getApiFileIp(), minIOConfigProperties.getOutputAddress());
    }
}
