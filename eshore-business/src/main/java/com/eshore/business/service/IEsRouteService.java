package com.eshore.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.eshore.domain.entity.biz.EsRoute;
import com.eshore.domain.model.form.EsRouteForm;

import java.util.List;


/**
 * 通道Service接口
 *
 * @author eshore
 * @date 2022-09-13
 */
public interface IEsRouteService extends IService<EsRoute> {
    /**
     * 查询通道
     *
     * @param id 通道主键
     * @return 通道
     */
    public EsRoute selectEsRouteById(Integer id);

    /**
     * 查询通道列表
     *
     * @param esRouteForm 通道
     * @return 通道集合
     */
    public List<EsRoute> selectEsRouteList(EsRouteForm esRouteForm);

    /**
     * 新增通道
     *
     * @param esRoute 通道
     * @return 结果
     */
    public int insertEsRoute(EsRoute esRoute);

    /**
     * 修改通道
     *
     * @param esRoute 通道
     * @return 结果
     */
    public int updateEsRoute(EsRoute esRoute);

    /**
     * 批量删除通道
     *
     * @param ids 需要删除的通道主键集合
     * @return 结果
     */
    public int deleteEsRouteByIds(Integer[] ids);

    /**
     * 删除通道信息
     *
     * @param id 通道主键
     * @return 结果
     */
    public int deleteEsRouteById(Integer id);
}
