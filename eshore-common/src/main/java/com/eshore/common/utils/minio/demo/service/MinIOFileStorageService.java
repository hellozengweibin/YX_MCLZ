package com.eshore.common.utils.minio.demo.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.eshore.common.exception.BizException;
import com.eshore.common.utils.minio.demo.MinIOConfigProperties;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author cjy
 * @Date 2022/7/12 17:50
 * @Version 1.0
 */
@Slf4j
@Service
public class MinIOFileStorageService implements com.eshore.common.utils.minio.demo.service.FileStorageService {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinIOConfigProperties minIOConfigProperties;

    private final static String separator = "/";

    /**
     * 上传图片文件
     *
     * @param prefix      文件前缀
     * @param filename    文件名
     * @param inputStream 文件流
     * @return 文件全路径
     */
    @Override
    public String uploadImgFile(String prefix, String filename, InputStream inputStream) {
        // 默认的bucket
        String defaultBucket = minIOConfigProperties.getBucket().get(0);
        return uploadImgFile(defaultBucket, prefix, filename, inputStream);
    }

    @Override
    public String uploadImgFile(String bucket, String prefix, String filename, InputStream inputStream) {
        // 构建文件路径名
        // 文件上传到minio上的Name把文件后缀带上，不然下载出现格式问题
        String filePath = builderFilePath(prefix, filename);
        try {
            // 文件名、上传的文件类型
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .object(filePath)
                    .contentType("image/jpg")
                    .bucket(bucket).stream(inputStream, inputStream.available(), -1)
                    .build();
            minioClient.putObject(putObjectArgs);
            // 拼接文件访问的URL
            return filePath;
        } catch (Exception ex) {
            log.error("minio put file error.", ex);
            throw new RuntimeException("上传文件失败");
        }
    }

    private String builderFilePath(String dirPath, String filename) {
        StringBuilder stringBuilder = new StringBuilder(50);
        if (!StringUtils.isEmpty(dirPath)) {
            // 设置默认的bucket路径
            dirPath = minIOConfigProperties.getApiFileIp();
            stringBuilder.append(dirPath).append(separator);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String todayStr = sdf.format(new Date());
        stringBuilder.append(todayStr).append(separator);
        stringBuilder.append(filename);
        return stringBuilder.toString();
    }

    /**
     * 上传html文件
     *
     * @param dir         文件前缀 ——》指定文件存放的minio目录，可以不写
     * @param filename    文件名——》上传的文件名
     * @param inputStream 文件流
     * @return 文件全路径
     */
    @Override
    public String uploadHtmlFile(String dir, String filename, InputStream inputStream) {
        String defaultBucket = minIOConfigProperties.getBucket().get(0);
        return uploadHtmlFile(defaultBucket, dir, filename, inputStream);
    }

    @Override
    public String uploadHtmlFile(String bucket, String dir, String filename, InputStream inputStream) {
        //文件上传到minio上的Name把文件后缀带上，不然下载出现格式问题
        String filePath = builderFilePath(dir, filename);
        try {
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .object(filePath).contentType("text/html")
                    .bucket(bucket).stream(inputStream, inputStream.available(), -1).build();
            minioClient.putObject(putObjectArgs);
            StringBuilder urlPath = new StringBuilder(minIOConfigProperties.getApiFileIp());
            urlPath.append(separator + minIOConfigProperties.getBucket())
                    .append(separator)
                    .append(filePath);
            // 返回该文件在minio中存放的路径
            return urlPath.toString();
        } catch (Exception e) {
            log.error(" minio put file error:", e);
            throw new RuntimeException("上传文件失败");
        }
    }

    @Override
    public String uploadCommonFile(String prefix, String filename, InputStream inputStream) {
        String defaultBucket = minIOConfigProperties.getBucket().get(0);
        return uploadCommonFile(defaultBucket, prefix, filename, inputStream);
    }

    @Override
    public String uploadCommonFile(String bucket, String prefix, String filename, InputStream inputStream) {
        if (StringUtils.isEmpty(bucket)) {
            bucket = minIOConfigProperties.getBucket().get(0);
        }
        String filePath = "";
        if (StringUtils.isNotEmpty(prefix)) {
            filePath = prefix + separator;
        }
        String todayStr = DateUtil.format(new Date(), "yyyy/MM/dd");
        filePath = filePath + todayStr + separator + filename;
        try {
            //创建头部信息
            Map<String, String> headers = new HashMap<>(10);

            //添加存储类
            headers.put("X-Amz-Storage-Class", "REDUCED_REDUNDANCY");
            //添加自定义内容类型
            headers.put("Content-Type", "application/octet-stream");

            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(filePath)
                    .headers(headers)
                    .stream(inputStream, inputStream.available(), -1)
                    .build();
            minioClient.putObject(putObjectArgs);
            // StringBuilder urlPath = new StringBuilder(minIOConfigProperties.getApiFileIp());
            // urlPath.append(separator).append(bucket)
            //         .append(separator).append(filePath);
            // // 返回该文件在minio中存放的路径
            // return urlPath.toString();
            return filePath;
        } catch (Exception e) {
            log.error(" minio put file error:", e);
            throw new RuntimeException("上传文件失败");
        }
    }

    /**
     * 删除文件
     *
     * @param filePath 文件路径(桶之后的路径)
     */
    @Override
    public void delete(String filePath) {
        String defaultBucket = minIOConfigProperties.getBucket().get(0);
        delete(defaultBucket, filePath);
    }

    /**
     * 删除文件
     *
     * @param bucket   桶名称
     * @param filePath 文件路径(桶之后的路径)
     */
    @Override
    public void delete(String bucket, String filePath) {
        RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder().bucket(bucket).object(filePath).build();
        try {
            minioClient.removeObject(removeObjectArgs);
        } catch (Exception e) {
            log.error("minio remove file error.  pathUrl:{}", filePath);
            e.printStackTrace();
        }
    }

    public void deleteFile(String bucketName, String filePath) throws ServerException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // 获取文件夹下的所有内容
        String objectNames = filePath + "/";
        Iterable<Result<Item>> list = minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).prefix(objectNames).recursive(false).build());
        for (Result<Item> item : list) {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(item.get().objectName()).build());
        }
    }

    @Override
    public void delete(String bucket, List<String> filePathList) {
        List<DeleteObject> deleteObjectList = filePathList.stream().map(DeleteObject::new).collect(Collectors.toList());
        RemoveObjectsArgs args = RemoveObjectsArgs.builder().bucket(bucket).objects(deleteObjectList).build();
        try {
            Iterable<Result<DeleteError>> results = minioClient.removeObjects(args);
            for (Result<DeleteError> result : results) {
                DeleteError error = result.get();
                log.error("删除图片失败: buket: {},  filePath: {}, errorMsg: {}", bucket, error.objectName(), error.message());
            }
        } catch (Exception e) {
            log.error("minio remove file error. ", e);
        }
    }

    public List<String> getListObject(String filpath) {
        String defaultBucket = minIOConfigProperties.getBucket().get(0);
        return getListObject(defaultBucket, filpath);
    }

    /**
     * 获取文件夹下的所有对象
     */
    public List<String> getListObject(String bucketName, String filePath) {
        Iterable<Result<Item>> listObjects = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucketName)
                        .prefix(filePath)
                        .recursive(true)
                        .build());
        ArrayList<String> objectItems = new ArrayList<>();
        // todo 1.判断bucket中是否存在该文件、2.要判断没有该文件夹的问题
        try {
            // 判断是否为文件夹
            for (Result<Item> objectItem : listObjects) {
                Item item = objectItem.get();
                // 如果还有下层的文件夹
//                if (item.isDir()){
//                }
                // 将文件名设置到集合中
                // 数据库中的是拼接成完成的路径 => 图片
                // 数据库的路径 http://140.249.50.65:9000/aiplatform/2022/09/05/441271918113271100232022090515411429043.jpg
//                String storeName =minIOConfigProperties.getApiFileIp()+ "/" + minIOConfigProperties.getBucket()+ "/" + item.objectName();
                objectItems.add(item.objectName());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return objectItems;
    }


    /**
     * 下载文件
     *
     * @param filePath 文件路径(桶之后的路径)
     * @return
     */
    @Override
    public byte[] downloadFile(String filePath) {
        String defaultBucket = minIOConfigProperties.getBucket().get(0);
        return downloadFile(defaultBucket, filePath);
    }

    /**
     * 下载文件
     *
     * @param bucket   桶名称
     * @param filePath 文件路径(桶之后的路径)
     * @return
     */
    @Override
    public byte[] downloadFile(String bucket, String filePath) {
        try {
            String url = getObjectUrl(bucket, filePath, minIOConfigProperties.getUrlExpiredSeconds(), TimeUnit.SECONDS);
            return HttpUtil.downloadBytes(url);
        } catch (Exception e) {
            log.error("minio down file error.  pathUrl:{}", filePath);
            return null;
        }
    }

    @Override
    public void batchDelete(Collection<String> urls) {
        for (String url : urls) {
            delete(url);
        }
    }

    @Override
    public String getObjectUrl(String bucketName, String objectName, Integer expiry, TimeUnit unit) {
        if (StrUtil.isBlank(objectName)) {
            return StrUtil.EMPTY;
        }
        GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucketName)
                .object(objectName)
                .expiry(expiry, unit)
                .build();
        try {
            return minioClient.getPresignedObjectUrl(args);
        } catch (Exception e) {
            log.error("获取文件路径失败, ", e);
            throw new BizException("获取文件失败");
        }
    }
}
