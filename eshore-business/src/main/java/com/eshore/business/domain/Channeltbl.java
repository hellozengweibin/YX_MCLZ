package com.eshore.business.domain;

//添加mybatisplus的三个包引用
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

//如果自己的包名修改了，则需要改成对应的包名
import com.eshore.domain.base.BaseEntity;
import com.eshore.domain.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

/**
 * 创建二级设备对象 channeltbl
 *
 * @author eshore
 * @date 2023-11-16
 */
@Data
@TableName("channeltbl")
public class Channeltbl extends BaseEntity implements Serializable {

    private static final long serialVersionUID=1L;

    /** 自增ID； */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 一级设备账号； */
    @Excel(name = "一级设备账号；")
    private String devpubid;

    /** 通道昵称； */
    @Excel(name = "通道昵称；")
    private String nickname;

    /** 二级设备类型； */
    @Excel(name = "二级设备类型；")
    private Long type;

    /** Alive */
    @Excel(name = "Alive")
    private Long alive;

    /** 子类型； */
    @Excel(name = "子类型；")
    private Long subtype;

    /** 通道数； */
    @Excel(name = "通道数；")
    private Long channel;

    /** 能力（弃用）； */
    @Excel(name = "能力", readConverterExp = "弃=用")
    private Long capability;

    /** ChanPubID */
    @Excel(name = "ChanPubID")
    private String chanpubid;

    /** DMarker */
    @Excel(name = "DMarker")
    private Long dmarker;

    /** StorageID */
    @Excel(name = "StorageID")
    private Long storageid;

    /** PlatID */
    @Excel(name = "PlatID")
    private Long platid;

}
