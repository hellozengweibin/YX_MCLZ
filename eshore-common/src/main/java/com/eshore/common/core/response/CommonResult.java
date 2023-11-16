package com.eshore.common.core.response;

import com.eshore.common.constant.HttpStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author jianlin.liu
 * @since 2022/07/01
 *
 * 统一API响应结果封装
 */
@ToString
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "CommonResult", description = "结果集")
public class CommonResult<T> implements Serializable {
    private static final long serialVersionUID = 453897489166439386L;

    @ApiModelProperty(value = "响应code")
    private int code;

    @ApiModelProperty(value = "响应信息")
    private String msg;



    @ApiModelProperty(value = "响应数据体")
    private T data;



    public CommonResult<T> setCode(ResponseCodeEnum responseCode) {
        this.code = responseCode.code();
        return this;
    }

    public CommonResult<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public CommonResult<T> setMessage(String message) {
        this.msg = message;
        return this;
    }

    public CommonResult<T> setData(T data) {
        this.data = data;
        return this;
    }

    public boolean isSuccess(){
        return this.code == HttpStatus.SUCCESS;
    }
}

