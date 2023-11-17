package com.eshore.business.domain.form;

//添加mybatisplus的三个包引用
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

//如果自己的包名修改了，则需要改成对应的包名
import com.eshore.domain.base.BaseEntity;
import com.eshore.domain.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

/**
 * 创建二级设备对象 channeltbl
 *
 * @author eshore
 * @date 2023-11-16
 */
@ApiModel(value = "创建二级设备对象")
@Data
@TableName("channeltbl")
public class ChanneltblForm extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 自增ID； */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "PlatID")
    private Long id;

    /** 一级设备账号； */
    @Excel(name = "一级设备账号；")
    @ApiModelProperty(value = "一级设备账号；")
    private String devpubid;

    /** 通道昵称； */
    @Excel(name = "通道昵称；")
    @ApiModelProperty(value = "通道昵称；")
    private String nickname;

    /** 二级设备类型； */
    @Excel(name = "二级设备类型；")
    @ApiModelProperty(value = "二级设备类型；")
    private Long type;

    /** Alive */
    @Excel(name = "Alive")
    @ApiModelProperty(value = "Alive")
    private Long alive;

    /** 子类型； */
    @Excel(name = "子类型；")
    @ApiModelProperty(value = "子类型；")
    private Long subtype;

    /** 通道数； */
    @Excel(name = "通道数；")
    @ApiModelProperty(value = "通道数；")
    private Long channel;

    /** 能力（弃用）； */
    @Excel(name = "能力", readConverterExp = "弃=用")
    @ApiModelProperty(value = "能力")
    private Long capability;

    /** ChanPubID */
    @Excel(name = "ChanPubID")
    @ApiModelProperty(value = "ChanPubID")
    private String chanpubid;

    /** DMarker */
    @Excel(name = "DMarker")
    @ApiModelProperty(value = "DMarker")
    private Long dmarker;

    /** StorageID */
    @Excel(name = "StorageID")
    @ApiModelProperty(value = "StorageID")
    private Long storageid;

    /** PlatID */
    @Excel(name = "PlatID")
    @ApiModelProperty(value = "PlatID")
    private Long platid;

}
