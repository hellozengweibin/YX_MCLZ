package com.eshore.domain.model.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


@ApiModel(value = "UploadFileResultVo", description = "上传文件返回数据结构")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UploadFileResultVo {

    @ApiModelProperty(value = "原文件名")
    private String originalFileName;

    @ApiModelProperty(value = "经过服务器处理后的新文件名")
    private String fileName;

    @ApiModelProperty(value = "预览地址url")
    private String url;

    @ApiModelProperty(value = "下载地址url")
    private String downloadUrl;

    @ApiModelProperty(value = "文件大小")
    private String fileSize;

}
