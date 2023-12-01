package com.eshore.business.domain;

//添加mybatisplus的三个包引用
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

//如果自己的包名修改了，则需要改成对应的包名
import com.eshore.common.annotation.DataSource;
import com.eshore.common.enums.DataSourceType;
import com.eshore.domain.base.BaseEntity;
import com.eshore.domain.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

/**
 * resgroupbindtbl3对象 resgroupbindtbl
 *
 * @author eshore
 * @date 2023-11-20
 */
@Data
@TableName("resgroupbindtbl")
public class Resgroupbindtbl3 extends BaseEntity implements Serializable {

    private static final long serialVersionUID=1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** ResGroupID */
    @Excel(name = "资源组帐号")
    private String resgroupid;

    /** BindResId */
    @Excel(name = "资源帐号")
    private String bindresid;

    /** BindResType */
    @Excel(name = "绑定类型")
    private Long bindrestype;

}
