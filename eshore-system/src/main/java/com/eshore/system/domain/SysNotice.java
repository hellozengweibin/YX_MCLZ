package com.eshore.system.domain;

//添加mybatisplus的三个包引用

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.eshore.domain.annotation.Excel;
import com.eshore.domain.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 公告管理对象 sys_notice
 *
 * @author eshore
 * @date 2023-07-03
 */
@Data
@TableName("sys_notice")
public class SysNotice extends BaseEntity implements Serializable
        {
private static final long serialVersionUID=1L;

    /** 公告id */
        @TableId(type = IdType.AUTO)
    private Integer noticeId;

    /** 公告标题 */
            @Excel(name = "公告标题")
    private String noticeTitle;

    /** 公告内容 */
            @Excel(name = "公告内容")
    private String noticeContent;

    /** 公告状态（0:未审核1:审核通过2:审核拒绝3:已作废） */
            @Excel(name = "公告状态", readConverterExp = "0=:未审核1:审核通过2:审核拒绝3:已作废")
    private String status;

    /** 附件地址 */
            @Excel(name = "附件地址")
    private String accessory;

    /** 推送时间 */
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @Excel(name = "推送时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date pushTime;

    /** 用户id */
            @Excel(name = "用户id")
    private Long userId;

    /** 机构id */
            @Excel(name = "机构id")
    private Long deptId;

    /** 机构名称 */
            @Excel(name = "机构名称")
    private String deptName;

    /** 审核不通过原因 */
            @Excel(name = "审核不通过原因")
    private String reason;

        }
