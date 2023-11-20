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
 * 创建资源组资源绑定对象 resgroupbindtbl
 *
 * @author eshore
 * @date 2023-11-20
 */
@ApiModel(value = "创建资源组资源绑定对象")
@Data
@TableName("resgroupbindtbl")
public class ResgroupbindtblForm extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 自增ID */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "绑定类型")
    private Long id;

    /** 资源组帐号 */
    @Excel(name = "资源组帐号")
    @ApiModelProperty(value = "资源组帐号")
    private String resgroupid;

    /** 资源帐号 */
    @Excel(name = "资源帐号")
    @ApiModelProperty(value = "资源帐号")
    private String bindresid;

    /** 绑定类型 */
    @Excel(name = "绑定类型")
    @ApiModelProperty(value = "绑定类型")
    private Long bindrestype;

}
