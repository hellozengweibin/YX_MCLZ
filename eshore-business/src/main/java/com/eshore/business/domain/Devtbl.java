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
import java.sql.Timestamp;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 创建设备对象 devtbl
 *
 * @author eshore
 * @date 2023-11-16
 */
@Data
@TableName("devtbl")
public class Devtbl extends BaseEntity implements Serializable {

    private static final long serialVersionUID=1L;

    /** 自增ID； */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 产品SN； */
    @Excel(name = "产品SN；")
    private String sn;

    /** 昵称； */
    @Excel(name = "昵称；")
    private String devnickname;

    /** 一级设备账号； */
    @Excel(name = "一级设备账号；")
    private String devpubid;

    /** 一级设备账号，专为电话（弃用）； */
    @Excel(name = "一级设备账号，专为电话", readConverterExp = "弃=用")
    private String devpubidfortel;

    /** 一级设备账号，专为其它（弃用）； */
    @Excel(name = "一级设备账号，专为其它", readConverterExp = "弃=用")
    private String devpubid3;

    /** 密码（弃用）； */
    @Excel(name = "密码", readConverterExp = "弃=用")
    private String passwd;

    /** 注册状态； */
    @Excel(name = "注册状态；")
    private Integer alive;

    /** 注册状态； */
    @Excel(name = "注册状态；")
    private Date updatetime;

    /** 注册主域(服务器名+服务器IP+服务器SipPort)； */
    @Excel(name = "注册主域(服务器名+服务器IP+服务器SipPort)；")
    private String homename;

    /** 域名； */
    @Excel(name = "域名；")
    private String domain;

    /** 端口； */
    @Excel(name = "端口；")
    private Integer port;

    /** IP地址 ； */
    @Excel(name = "IP地址 ；")
    private String ip;

    /** 穿网IP地址； */
    @Excel(name = "穿网IP地址；")
    private String natip;

    /** 网页入口； */
    @Excel(name = "网页入口；")
    private String webportal;

    /** 网页入口登录用户； */
    @Excel(name = "网页入口登录用户；")
    private String webloginuser;

    /** 网页入口登录密码； */
    @Excel(name = "网页入口登录密码；")
    private String webloginpasswd;

    /** 录象总空间； */
    @Excel(name = "录象总空间；")
    private Long totaldiskroom;

    /** 录像剩余空间； */
    @Excel(name = "录像剩余空间；")
    private Long diskroom;

    /** 所属区域(DevAreaTbl的ID,设备区域)； */
    @Excel(name = "所属区域(DevAreaTbl的ID,设备区域)；")
    private Long areaid;

    /** 设备型号(DevModelTbl的Model2) */
    @Excel(name = "设备型号(DevModelTbl的Model2)")
    private String model;

    /** 所属平台版本  （弃用）； */
    @Excel(name = "所属平台版本  ", readConverterExp = "弃=用")
    private String sysver;

    /** 归属分发服务器（弃用）； */
    @Excel(name = "归属分发服务器", readConverterExp = "弃=用")
    private String mrspubid;

    /** 归属存储服务器（弃用）； */
    @Excel(name = "归属存储服务器", readConverterExp = "弃=用")
    private String msspubid;

    /** 归属点播服务器（弃用）； */
    @Excel(name = "归属点播服务器", readConverterExp = "弃=用")
    private String vodpubid;

    /** 归属网关服务器； */
    @Excel(name = "归属网关服务器；")
    private String sipgwpubid;

    /** 归属报警服务器（弃用）； */
    @Excel(name = "归属报警服务器", readConverterExp = "弃=用")
    private String acspubid;

    /** 点对点最大连接数（弃用）； */
    @Excel(name = "点对点最大连接数", readConverterExp = "弃=用")
    private Long p2pmaxconns;

    /** 分发连接方式(0-UDP单播；1-UDP组播)（弃用）； */
    @Excel(name = "分发连接方式(0-UDP单播；1-UDP组播)", readConverterExp = "弃=用")
    private Long relaymode;

    /** 安装日期； */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "安装日期；", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date opendate;

    /** 安装详细地点； */
    @Excel(name = "安装详细地点；")
    private String installlocation;

    /** 电话； */
    @Excel(name = "电话；")
    private String linktelphone;

    /** 证件名称(身份证-1；驾驶证-2；军人证-3)； */
    @Excel(name = "证件名称(身份证-1；驾驶证-2；军人证-3)；")
    private Long cert;

    /** 证件编号； */
    @Excel(name = "证件编号；")
    private String certnum;

    /** 开户日期； */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "开户日期；", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date opentime;

    /** 销户日期； */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "销户日期；", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date closetime;

    /** 有效状态； */
    @Excel(name = "有效状态；")
    private Integer status;

    /** 经度； */
    @Excel(name = "经度；")
    private Long longitude;

    /** 纬度； */
    @Excel(name = "纬度；")
    private Long latitude;

    /** 设备备忘录； */
    @Excel(name = "设备备忘录；")
    private String memo;

    /** ； */
    @Excel(name = "；")
    private String onvifuri;

    /** 0-未变化；1-增加；2-删除；3-修改；4-已同步，可删除； */
    @Excel(name = "0-未变化；1-增加；2-删除；3-修改；4-已同步，可删除；")
    private Long dmarker;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long storageid;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long platid;

}
