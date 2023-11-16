package com.eshore.domain.model.vo.common;

import com.eshore.domain.annotation.EnumValue;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author yhq
 */
@Data
public class MessageSendVo {

    /**
     * 手机号码
     */
    @Pattern(regexp = "^1[3|4|5|7|8|9][0-9]{9}$", message = "手机号码格式不对")
    private String phoneNum;

    /**
     * 验证码
     */
    private String messageCode;

    /**
     * 新密码
     */
    private String newUpwd;

    /**
     * 短信类型，0登录，1修改密码 2重置密码(忘记密码) 3 修改手机号
     */

    @ApiModelProperty(value = "短信类型；取值范围 0：登录 | 1：修改密码 | 2：重置密码 | 3:修改手机号")
    @NotNull(message = "短信类型不能为空")
    @Max(3)
    @Min(0)
    private Integer type;

    /**
     * type重置密码时候 不为null
     */
    private String account;

    /**
     * 平台类型
     */
    @EnumValue(intValues = {1, 2, 3, 4}, require = true, message = "平台类型不能为空")
    private Integer platform;

    private String appCode;

    public static final int TYPE_LOGIN = 0;
}
