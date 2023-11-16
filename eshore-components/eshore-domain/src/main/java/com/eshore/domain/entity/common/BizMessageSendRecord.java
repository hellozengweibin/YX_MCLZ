package com.eshore.domain.entity.common;

import com.eshore.domain.annotation.Excel;
import com.eshore.domain.base.BaseEntity;
import lombok.Data;

/**
 * 短信发送记录对象 biz_message_send_record
 *
 * @author eshore
 * @date 2022-09-09
 */
@Data
public class BizMessageSendRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private Long id;

    /**
     * 状态（0成功 1失败）
     */
    @Excel(name = "状态", readConverterExp = "0=成功,1=失败")
    private String status;

    /**
     * 手机号码
     */
    @Excel(name = "手机号码")
    private String phone;

    /**
     * 类型(0：登录 | 1：修改密码 | 2：重置密码 | 3：短信预警)
     */
    @Excel(name = "类型", readConverterExp = "0=登录,1=修改密码,2=重置密码,3=短信预警")
    private Integer type;

    /**
     * 端口
     */
    @Excel(name = "端口")
    private Integer port;

    /**
     * 发送内容
     */
    @Excel(name = "发送内容")
    private String content;

    /**
     *
     */
    private String appCode;

}
