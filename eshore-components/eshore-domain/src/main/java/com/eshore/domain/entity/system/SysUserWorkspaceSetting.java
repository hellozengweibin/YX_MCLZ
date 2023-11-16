package com.eshore.domain.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.eshore.domain.base.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @TableName sys_user_workspace_setting
 */
@Data
public class SysUserWorkspaceSetting extends BaseEntity implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 是否接收告警;0:打开；1:关闭
     */
    @NotNull(message = "是否接收告警不能为空")
    private Integer receiveAlarm;

    /**
     * 告警弹窗;0:打开；1:关闭
     */
    @NotNull(message = "是否告警弹窗不能为空")
    private Integer alarmAlert;

    /**
     * 声音提醒;0:打开；1:关闭
     */
    @NotNull(message = "是否声音提醒不能为空")
    private Integer voiceRemind;

    /**
     * 预留字段
     */
    private String extra;

    private Integer platformType;

    /**
     * 算法编码，多个以";"隔开
     */
    private String algorithmCode;

    private static final long serialVersionUID = 1L;

}
