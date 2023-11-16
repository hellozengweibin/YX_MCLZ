package com.eshore.domain.model.vo.esRoute;

//添加mybatisplus的三个包引用

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.eshore.domain.annotation.Excel;
import com.eshore.domain.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 通道对象 es_route
 *
 * @author eshore
 * @date 2022-09-13
 */
@ApiModel(value = "通道对象")
@Data
@TableName("es_route")
public class EsRouteVO extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "落点地图right")
    private Integer id;

    /**
     * 关联车间id
     */
    @Excel(name = "关联车间id")
    @ApiModelProperty(value = "关联车间id")
    private Integer placeId;

    /**
     * 通道编号
     */
    @Excel(name = "通道编号")
    @ApiModelProperty(value = "通道编号")
    private String code;

    /**
     * 名称/别名
     */
    @Excel(name = "名称/别名")
    @ApiModelProperty(value = "名称/别名")
    private String name;

    /**
     * ip
     */
    @Excel(name = "ip")
    @ApiModelProperty(value = "ip")
    private String ip;

    /**
     * 流水线起始端
     */
    @Excel(name = "流水线起始端")
    @ApiModelProperty(value = "流水线起始端")
    private Integer piplineStart;

    /**
     * 流水线结束端
     */
    @Excel(name = "流水线结束端")
    @ApiModelProperty(value = "流水线结束端")
    private Integer piplineEnd;

    /**
     * 在线状态
     */
    @Excel(name = "在线状态")
    @ApiModelProperty(value = "在线状态")
    private Integer online;

    /**
     * 落点地图left
     */
    @Excel(name = "落点地图left")
    @ApiModelProperty(value = "落点地图left")
    private String positionLeft;

    /**
     * 落点地图right
     */
    @Excel(name = "落点地图right")
    @ApiModelProperty(value = "落点地图right")
    private String positionTop;

}
