package com.eshore.domain.entity.biz;

//添加mybatisplus的三个包引用

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.eshore.domain.base.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 通道对象 es_route
 *
 * @author eshore
 * @date 2022-09-13
 */
@Data
@TableName("es_route")
public class EsRoute extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 关联车间id
     */
    private Integer placeId;

    /**
     * 通道编号
     */
    private String code;

    /**
     * 名称/别名
     */
    private String name;

    /**
     * ip
     */
    private String ip;

    /**
     * 流水线起始端
     */
    private Integer piplineStart;

    /**
     * 流水线结束端
     */
    private Integer piplineEnd;

    /**
     * 在线状态
     */
    private Integer online;

    /**
     * 落点地图left
     */
    private String positionLeft;

    /**
     * 落点地图right
     */
    private String positionTop;

}
