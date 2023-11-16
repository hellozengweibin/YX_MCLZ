package com.eshore.web.controller.common;

import com.eshore.business.constant.CommonConstants;
import com.eshore.business.utils.MinIOUtil;
import com.eshore.common.annotation.EncryptIgnore;
import com.eshore.common.config.EshoreConfig;
import com.eshore.common.constant.HttpStatus;
import com.eshore.common.core.redis.RedisCache;
import com.eshore.common.core.response.CommonResult;
import com.eshore.common.core.response.ResponseGenerator;
import com.eshore.common.enums.MinioPrefixType;
import com.eshore.common.exception.ServiceException;
import com.eshore.common.utils.StringUtils;
import com.eshore.common.utils.file.FileUploadUtils;
import com.eshore.common.utils.file.FileUtils;
import com.eshore.common.utils.minio.demo.service.MinIOFileStorageService;
import com.eshore.domain.model.vo.common.UploadFileResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 通用请求处理
 *
 * @author eshore
 */
@Api(value = "/common", tags = {"通用文件上传与下载请求"})
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Autowired
    private MinIOFileStorageService minIOFileStorageService;

    @Autowired
    private MinIOUtil minIOUtil;

    @Resource
    private RedisCache redisCache;


    /**
     * 通用上传请求（单个）
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "文件", dataType = "file", paramType = "formData", required = true)
    })
    @ApiOperation(value = "通用上传请求（单个）", notes = "通用上传请求（单个）", httpMethod = "POST")
    @PostMapping("/upload")
    @EncryptIgnore
    public CommonResult<UploadFileResultVo> uploadFile(@RequestParam(value = "file") MultipartFile file, String type) {
        String errMsg = validateFile(type, FileUploadUtils.getExtension(file));
        if (errMsg != null) {
            return ResponseGenerator.genFailResult(HttpStatus.ERROR, errMsg);
        }
        try {
            return ResponseGenerator.genSuccessResult(uploadFileToMinio(file, type));
        } catch (Exception e) {
            return ResponseGenerator.genFailResult(e.getMessage());
        }
    }

    /**
     * 通用上传请求（多个）
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "文件数组", dataType = "array", paramType = "formData", required = true)
    })
    @ApiOperation(value = "通用上传请求（多个）", notes = "通用上传请求（多个）", httpMethod = "POST")
    @PostMapping("/uploads")
    @EncryptIgnore
    public CommonResult<List<UploadFileResultVo>> uploadFiles(@RequestParam(value = "file") List<MultipartFile> files, String type) throws Exception {
        try {
            List<UploadFileResultVo> resultVoList = new ArrayList<>();
            for (MultipartFile file : files.stream().filter(item -> {
                String message = validateFile(type, FileUploadUtils.getExtension(item));
                if (message != null) throw new ServiceException(message);
                return true;
            }).collect(Collectors.toList())) {
                resultVoList.add(this.uploadFileToMinio(file, type));
            }
            return ResponseGenerator.genSuccessResult(resultVoList);
        } catch (Exception e) {
            return ResponseGenerator.genFailResult(e.getMessage());
        }
    }

    /**
     * 刷新图片链接，因为图片链接是有时效性的，所有需要在获取详情时，重新请求一次图片的链接
     *
     * @param url 图片访问URL
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "url", value = "url", dataType = "string", paramType = "query", required = true)
    })
    @ApiOperation(value = "刷新文件链接有效地址", notes = "刷新图片链接，因为图片链接是有时效性的，所有需要在获取详情时，重新请求一次图片的链接")
    @GetMapping("/minio/refreshUrl")
    public CommonResult<String> getRefreshUrl(@RequestParam(value = "url") String url) {
        return ResponseGenerator.genSuccessResult(minIOUtil.refreshUrl(url));
    }

    /**
     * @param response
     * @param request
     */
    @ApiOperation(value = "下载文件", httpMethod = "GET")
    @GetMapping("/download")
    public void fileDownload(HttpServletResponse response, HttpServletRequest request) {
        String url = request.getParameter("url");
        String requestId = request.getParameter("requestId");
        String tempTimestamp = request.getParameter("timestamp");
        if (StringUtils.isEmpty(url) || StringUtils.isEmpty(requestId) || StringUtils.isEmpty(tempTimestamp)) {
            throw new ServiceException("下载参数异常，请稍后再试");
        }
        try {
            String fileName = FileUtils.getName(url);
            if (!FileUtils.checkAllowDownload(fileName)) {
                throw new Exception(StringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
            }
            String key = CommonConstants.DOWNLOAD_MAP + ":" + requestId;
            if (redisCache.hasCacheKey(key)) {
                int downCount = redisCache.getCacheObject(key);
                if (downCount > 1) {
                    log.info("fileDownload requestId:{}", requestId);
                    throw new ServiceException("请求重复，请稍后再试");
                }
            }
            long timestamp = Long.valueOf(tempTimestamp);
            if ((System.currentTimeMillis() - timestamp) > (300 * 1000)) {
                log.info("fileDownload requestId:{}", "请求时间戳异常，请稍后再试");
                throw new ServiceException("请求时间戳异常，请稍后再试");
            }
            if (redisCache.hasCacheKey(key)) {
                redisCache.increment(key, 1);
            } else {
                redisCache.setCacheObject(key, 1, 300, TimeUnit.SECONDS);
            }

            log.info("fileDownload requestId:{}", requestId);
            byte[] download = minIOUtil.download(url);
            // 设置响应头
            FileUtils.setAttachmentResponseHeader(response, fileName);
            // 从minio下载到本地临时目录
            String filePath = FileUtils.writeBytes(download, EshoreConfig.getDownloadPath());
            FileUtils.writeBytes(filePath, response.getOutputStream());
            // 删除本地临时文件
            FileUtils.deleteFile(filePath);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("下载文件失败", e);
        }
    }


    /**
     * 上传文件到minio
     *
     * @param file
     * @param type
     * @return
     */
    private UploadFileResultVo uploadFileToMinio(MultipartFile file, String type) {
        String filePath = null;
        try {
            filePath = minIOFileStorageService.uploadCommonFile(type, file.getOriginalFilename(), file.getInputStream());
        } catch (Exception e) {
            // 文件上传失败
            throw new ServiceException("文件上传失败");
        }
        if (filePath == null) throw new ServiceException("文件上传失败");
        // 上传文件路径
        String url = minIOUtil.getObjectUrl(filePath);
        // 上传并返回新文件名称
        double fileSize = (float) file.getSize() / 1024 / 1024;
        UploadFileResultVo uploadFileResultVo = UploadFileResultVo.builder()
                .url(filePath)
                .fileName(file.getOriginalFilename())
                .downloadUrl(url)
                .originalFileName(file.getOriginalFilename())
                .fileSize(String.format("%.2f", fileSize) + "M")
                .build();
        return uploadFileResultVo;
    }


    /**
     * 校验 文件
     *
     * @param type
     * @param fileExt
     * @return
     */
    private String validateFile(String type, String fileExt) {
        if (StringUtils.isNotEmpty(type)) {
            if (!MinioPrefixType.isPrefixAllow(type)) {
                return "上传文件业务类型不允许";
            }
            MinioPrefixType minioPrefixType = MinioPrefixType.findByType(type);
            if (!minioPrefixType.isFileTypeAllow(fileExt)) {
                return "上传文件类型不允许";
            }
        }
        return null;
    }


    // /**
    //  * 通用上传请求（多个）
    //  */
    // @ApiImplicitParams({
    //         @ApiImplicitParam(name = "file", value = "文件数组", dataType = "array", paramType = "formData", required = true)
    // })
    // @ApiOperation(value = "通用上传请求（多个）", notes = "通用上传请求（多个）", httpMethod = "POST")
    // @PostMapping("/uploads")
    // @EncryptIgnore
    // public CommonResult uploadFiles(List<MultipartFile> files) throws Exception {
    //     try {
    //         // 上传文件路径
    //         String filePath = EshoreConfig.getUploadPath();
    //         List<String> urls = new ArrayList<String>();
    //         List<String> fileNames = new ArrayList<String>();
    //         List<String> newFileNames = new ArrayList<String>();
    //         List<String> originalFilenames = new ArrayList<String>();
    //         for (MultipartFile file : files) {
    //             // 上传并返回新文件名称
    //             String fileName = FileUploadUtils.upload(filePath, file);
    //             String url = serverConfig.getUrl() + fileName;
    //             urls.add(url);
    //             fileNames.add(fileName);
    //             newFileNames.add(FileUtils.getName(fileName));
    //             originalFilenames.add(file.getOriginalFilename());
    //         }
    //         Map<String, Object> result = new HashMap<>();
    //         result.put("urls", StringUtils.join(urls, FILE_DELIMETER));
    //         result.put("fileNames", StringUtils.join(fileNames, FILE_DELIMETER));
    //         result.put("newFileNames", StringUtils.join(newFileNames, FILE_DELIMETER));
    //         result.put("originalFilenames", StringUtils.join(originalFilenames, FILE_DELIMETER));
    //         return ResponseGenerator.genSuccessResult(result);
    //     } catch (Exception e) {
    //         return ResponseGenerator.genFailResult(e.getMessage());
    //     }
    // }

    // /**
    //  * 本地资源通用下载
    //  */
    // @ApiImplicitParams({
    //         @ApiImplicitParam(paramType = "query", dataType = "string", name = "resource", value = ""),
    //         @ApiImplicitParam(paramType = "query", dataType = "HttpServletRequest", name = "request", value = ""),
    //         @ApiImplicitParam(paramType = "query", dataType = "HttpServletResponse", name = "response", value = "")
    // })
    // @ApiOperation(value = "本地资源通用下载", notes = "本地资源通用下载", httpMethod = "GET")
    // @GetMapping("/download/resource")
    // public void resourceDownload(String resource, HttpServletRequest request, HttpServletResponse response)
    //         throws Exception {
    //     try {
    //         if (!FileUtils.checkAllowDownload(resource)) {
    //             throw new Exception(StringUtils.format("资源文件({})非法，不允许下载。 ", resource));
    //         }
    //         // 本地资源路径
    //         String localPath = EshoreConfig.getProfile();
    //         // 数据库资源地址
    //         String downloadPath = localPath + StringUtils.substringAfter(resource, Constants.RESOURCE_PREFIX);
    //         // 下载名称
    //         String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
    //         response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
    //         FileUtils.setAttachmentResponseHeader(response, downloadName);
    //         FileUtils.writeBytes(downloadPath, response.getOutputStream());
    //     } catch (Exception e) {
    //         log.error("下载文件失败", e);
    //     }
    // }


    /*   *//**
     * author:walker
     * time: 2022/6/17 0017 10:53
     * description:  上传文件
     *//*
    @PostMapping("/uploadMinio")
    public Result<?> uploadMinio(MultipartFile file) throws IOException {
        String minIOUrl = fileStorageService.uploadImgFile("", file.getOriginalFilename(), file.getInputStream());
        MinioUtils.uploadFileMinio()
        // 将返回的数据封装成FileVo
        FileVo fileVo = new FileVo();
        fileVo.setFileName(file.getOriginalFilename());
        fileVo.setUrl(minIOUrl);
        return success(fileVo);
    }*/


    // /**
    //  * 获取测试token
    //  */
    // @ApiImplicitParams({
    //         @ApiImplicitParam(paramType = "query", dataType = "string", name = "username", value = ""),
    //         @ApiImplicitParam(paramType = "query", dataType = "string", name = "password", value = "")
    // })
    // @ApiOperation(value = "获取测试token", notes = "获取测试token", httpMethod = "GET")
    // @GetMapping("/getTestToken")
    // public CommonResult<String> getTestToken(String username, String password) {
    //     Authentication authentication = authenticationManager
    //             .authenticate(new UsernamePasswordAuthenticationToken(username, password));
    //     LoginUser loginUser = (LoginUser) authentication.getPrincipal();
    //     String token = tokenService.createToken(loginUser);
    //     return ResponseGenerator.genSuccessResult("Bearer " + token);
    // }

}
