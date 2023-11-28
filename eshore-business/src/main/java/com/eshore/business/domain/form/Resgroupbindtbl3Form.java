package com.eshore.business.domain.form;

//添加mybatisplus的三个包引用
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

//如果自己的包名修改了，则需要改成对应的包名
import com.eshore.common.annotation.DataSource;
import com.eshore.common.enums.DataSourceType;
import com.eshore.domain.base.BaseEntity;
import com.eshore.domain.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

/**
 * resgroupbindtbl3对象 resgroupbindtbl
 *
 * @author eshore
 * @date 2023-11-20
 */
@ApiModel(value = "resgroupbindtbl3对象")
@Data
@TableName("resgroupbindtbl")
public class Resgroupbindtbl3Form extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "BindResType")
    private Long id;

    /** ResGroupID */
    @Excel(name = "ResGroupID")
    @ApiModelProperty(value = "ResGroupID")
    private String resgroupid;

    /** BindResId */
    @Excel(name = "BindResId")
    @ApiModelProperty(value = "BindResId")
    private String bindresid;

    /** BindResType */
    @Excel(name = "BindResType")
    @ApiModelProperty(value = "BindResType")
    private Long bindrestype;

}
