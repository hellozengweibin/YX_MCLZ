package com.eshore.common.core.domain.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName LoginResponseBody
 * @Description 登录响应
 * @Author jianlin.liu
 * @Date 2022/9/15
 * @Version 1.0
 **/
@Data
@ApiModel(value = "LoginResponseBody")
public class LoginResponseBody {

    @ApiModelProperty(value = "用户token")
    private String token;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "用户类型：0超级管理员")
    private String userType;

    @ApiModelProperty(value = "装维人员id，装维人员时返回")
    private Long installerId;

    @ApiModelProperty(value = "是否为初始账号：0-是，1-否")
    private Integer init;

    @ApiModelProperty(value = "用户密码是否已到期：true-密码已到期，false-密码未过期，可正常使用")
    private boolean pwdExpire;
}
