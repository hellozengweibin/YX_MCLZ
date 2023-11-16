package com.eshore.common.utils.minio.demo.service;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author cjy
 * @Date 2022/7/12 17:48
 * @Version 1.0
 */
public interface FileStorageService {

    /**
     * 上传图片文件
     *
     * @param prefix      文件前缀
     * @param filename    文件名
     * @param inputStream 文件流
     * @return 文件全路径
     */
    public String uploadImgFile(String prefix, String filename, InputStream inputStream);

    public String uploadImgFile(String bucket, String prefix, String filename, InputStream inputStream);

    /**
     * 上传html文件
     *
     * @param prefix      文件前缀
     * @param filename    文件名
     * @param inputStream 文件流
     * @return 文件全路径
     */
    public String uploadHtmlFile(String prefix, String filename, InputStream inputStream);

    public String uploadHtmlFile(String bucket, String prefix, String filename, InputStream inputStream);

    /**
     * 上传普通文件
     *
     * @param prefix
     * @param filename
     * @param inputStream
     * @return
     */
    String uploadCommonFile(String prefix, String filename, InputStream inputStream);

    String uploadCommonFile(String bucket, String prefix, String filename, InputStream inputStream);

    /**
     * 删除文件
     *
     * @param pathUrl 文件全路径
     */
    public void delete(String pathUrl);

    void delete(String bucket, String pathUrl);

    void delete(String bucket, List<String> filePathList);

    /**
     * 下载文件
     *
     * @param pathUrl 文件全路径
     * @return
     */
    public byte[] downloadFile(String pathUrl);

    public byte[] downloadFile(String bucket, String pathUrl);

    /**
     * 批量移除文件
     *
     * @param urls
     */
    void batchDelete(Collection<String> urls);

    String getObjectUrl(String bucketName, String objectName, Integer expiry, TimeUnit unit);
}
