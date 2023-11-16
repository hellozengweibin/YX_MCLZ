package com.eshore.encrypt.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName ResultData
 * @Description 数据加密返回结果
 * @Author jianlin.liu
 * @Date 2022/9/13
 * @Version 1.0
 **/
@ApiModel(description = "数据加密返回结果")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EncryptResult {
    /**
     * 响应数据（加密）
     */
    @ApiModelProperty("响应数据（加密）")
    private String responseData;
}
