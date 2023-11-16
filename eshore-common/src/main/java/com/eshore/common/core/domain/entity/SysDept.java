package com.eshore.common.core.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.eshore.domain.annotation.Excel;
import com.eshore.domain.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 部门表 sys_dept
 *
 * @author eshore
 */
@ApiModel(value = "SysDept", description = "部门表实体类")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysDept extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 部门ID
     */
    @ApiModelProperty(value = "部门ID")
    @TableId(type = IdType.AUTO)
    private Long deptId;

    /**
     * 父部门ID
     */
    @ApiModelProperty(value = "父部门ID")
    private Long parentId;

    /**
     * 祖级列表
     */
    @ApiModelProperty(value = "祖级列表")
    private String ancestors;

    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称")
    private String deptName;

    /**
     * 显示顺序
     */
    @ApiModelProperty(value = "显示顺序")
    private String orderNum;

    /**
     * 负责人
     */
    @ApiModelProperty(value = "负责人")
    private String leader;

    /**
     * 联系电话
     */
    @ApiModelProperty(value = "联系电话")
    private String phone;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    private String email;

    /**
     * 部门状态:0正常,1停用
     */
    @ApiModelProperty(value = "部门状态:0正常,1停用")
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @ApiModelProperty(value = "删除标志（0代表存在 2代表删除）")
    private String delFlag;

    /**
     * 父部门名称
     */
    @ApiModelProperty(value = "父部门名称")
    @TableField(exist = false)
    private String parentName;

    /**
     * 子部门
     */
    @ApiModelProperty(value = "子部门")
    @TableField(exist = false)
    private List<SysDept> children = new ArrayList<SysDept>();

    /**
     * 区域id
     */
    @ApiModelProperty(value = "区域id")
    private Long areaId;

    /**
     * 父部门名称
     */
    @ApiModelProperty(value = "区域名字")
    private String areaName;

    /**
     * 部门级别
     */
    @ApiModelProperty(value = "部门级别")
    private String deptLevel;

    /**
     * 部门编码
     */
    @ApiModelProperty(value = "部门编码")
    private String deptCode;

    /**
     * 简称
     */
    @ApiModelProperty(value = "简称")
    private String abbreviation;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String description;


    /**
     * 经度
     */
    @Excel(name = "经度")
    @ApiModelProperty(value = "经度")
    private BigDecimal longitude;

    /**
     * 纬度
     */
    @Excel(name = "纬度")
    @ApiModelProperty(value = "纬度")
    private BigDecimal latitude;

    /**
     * 平台类型
     */
    @ApiModelProperty(value = "平台类型")
    private String platformType;

    @NotBlank(message = "部门名称不能为空")
    @Size(min = 0, max = 30, message = "部门名称长度不能超过30个字符")
    public String getDeptName() {
        return deptName;
    }

    @Size(min = 0, max = 11, message = "联系电话长度不能超过11个字符")
    public String getPhone() {
        return phone;
    }

    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
    public String getEmail() {
        return email;
    }

    /**
     * 绑定的电箱ids
     */
    @TableField(exist = false)
    private Long[] bindDevBoxIds;
    /**
     * 绑定的摄像头ids
     */
    @TableField(exist = false)
    private Long[] bindDevCameraIds;

    /**
     * 绑定的音柱ids
     */
    @TableField(exist = false)
    private Long[] bindDevSoundIds;

}
