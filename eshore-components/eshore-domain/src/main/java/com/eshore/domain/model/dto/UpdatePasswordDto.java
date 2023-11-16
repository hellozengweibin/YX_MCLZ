package com.eshore.domain.model.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 修改密码请求
 */
@Data
public class UpdatePasswordDto {

 /**
  * 原密码
  */
 @NotNull(message = "原密码不能为空")
 private String oldPassword;

 /**
  * 新密码
  */
 @NotNull(message = "新密码不能为空")
 private String newPassword;

 /**
  * 验证码
  */
 @NotNull(message = "验证码不能为空")
 private String messageCode;

 /**
  * 平台类型
  */
 @Min(value = 1, message = "平台类型最小值为1")
 @Max(value = 5, message = "平台类型最大值为5")
 private Integer platform;
}
