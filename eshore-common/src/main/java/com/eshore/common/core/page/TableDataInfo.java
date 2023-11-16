package com.eshore.common.core.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 表格分页数据对象
 *
 * @author eshore
 */
@ApiModel(value = "TableDataInfo", description = "表格分页数据对象实体类")
@Data
public class TableDataInfo<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 总记录数
     */
    @ApiModelProperty(name = "总记录数")
    private long total;

    /**
     * 列表数据
     */
    @ApiModelProperty(name = "列表数据")
    private List<T> rows;

    /**
     * 消息状态码
     */
    @ApiModelProperty(name = "消息状态码")
    private int code;

    /**
     * 消息内容
     */
    @ApiModelProperty(name = "消息内容")
    private String msg;

    /**
     * 表格数据对象
     */
    public TableDataInfo() {
    }

    /**
     * 分页
     *
     * @param list  列表数据
     * @param total 总记录数
     */
    public TableDataInfo(List<T> list, int total) {
        this.rows = list;
        this.total = total;
    }
}
