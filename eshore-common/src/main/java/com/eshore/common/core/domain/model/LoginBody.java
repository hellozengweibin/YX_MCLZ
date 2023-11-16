package com.eshore.common.core.domain.model;

import com.eshore.domain.annotation.EnumValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 用户登录对象
 *
 * @author eshore
 */
@ApiModel(value = "LoginBody", description = "用户登录对象")
@Data
public class LoginBody {
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    @NotNull(message = "登录账号不能为空")
    @Length(max = 100, message = "账号输入错误")
    private String username;

    /**
     * 用户密码
     */
    @ApiModelProperty(value = "密码")
    @NotNull(message = "密码不能为空")
    @Length(max = 100, message = "密码长度过长")
    private String password;

    /**
     * 验证码
     */
    @ApiModelProperty(value = "图片验证码")
    private String code;

    /**
     * 唯一标识
     */
    @ApiModelProperty(value = "图片验证码UUID（APP登录时不必填）")
    private String uuid;

    @ApiModelProperty(value = "手机号（手机号登录时必填）")
    private String phone;
    /**
     * 短信验证码
     */
    @ApiModelProperty(value = "短信验证码")
    @NotNull(message = "短信验证码不能为空")
    private String messageCode;

    @ApiModelProperty(value = "平台类型 1:数据决策大屏 2:防溺水预警平台 3:智能云广播系统 4:运维管理系统 5:移动端")
    @EnumValue(require = true, intValues = {1, 2, 3, 4, 5}, message = "平台类型不能为空")
    private Integer platform;

    @ApiModelProperty(value = "登录方式 1-账号密码登录  2-手机验证码登录(暂不支持)")
    @EnumValue(require = true, intValues = {1, 2}, message = "登录方式不能为空")
    private Integer loginWay;

    @ApiModelProperty(value = "非必填字段")
    private String appCode;
}
