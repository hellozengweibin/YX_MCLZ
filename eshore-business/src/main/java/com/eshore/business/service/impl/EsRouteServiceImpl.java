package com.eshore.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eshore.common.annotation.DataSource;
import com.eshore.common.enums.DataSourceType;
import com.eshore.domain.entity.biz.EsRoute;
import com.eshore.domain.model.form.EsRouteForm;
import com.eshore.business.mapper.EsRouteMapper;
import com.eshore.business.service.IEsRouteService;
import com.eshore.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 通道Service业务层处理
 *
 * @author eshore
 * @date 2022-09-13
 */
@Service
@DataSource(value = DataSourceType.SLAVE1)
public class EsRouteServiceImpl extends ServiceImpl<EsRouteMapper, EsRoute> implements IEsRouteService {
    @Autowired
    private EsRouteMapper esRouteMapper;

    /**
     * 查询通道
     *
     * @param id 通道主键
     * @return 通道
     */
    @Override
    public EsRoute selectEsRouteById(Integer id) {
        return esRouteMapper.selectEsRouteById(id);
    }

    /**
     * 查询通道列表
     *
     * @param esRouteForm 通道
     * @return 通道
     */
    @Override
    public List<EsRoute> selectEsRouteList(EsRouteForm esRouteForm) {
        return esRouteMapper.selectEsRouteList(esRouteForm);
    }

    /**
     * 新增通道
     *
     * @param esRoute 通道
     * @return 结果
     */
    @Override
    public int insertEsRoute(EsRoute esRoute) {
        esRoute.setCreateTime(DateUtils.getNowDate());
        return esRouteMapper.insertEsRoute(esRoute);
    }

    /**
     * 修改通道
     *
     * @param esRoute 通道
     * @return 结果
     */
    @Override
    public int updateEsRoute(EsRoute esRoute) {
        esRoute.setUpdateTime(DateUtils.getNowDate());
        return esRouteMapper.updateEsRoute(esRoute);
    }

    /**
     * 批量删除通道
     *
     * @param ids 需要删除的通道主键
     * @return 结果
     */
    @Override
    public int deleteEsRouteByIds(Integer[] ids) {
        return esRouteMapper.deleteEsRouteByIds(ids);
    }

    /**
     * 删除通道信息
     *
     * @param id 通道主键
     * @return 结果
     */
    @Override
    public int deleteEsRouteById(Integer id) {
        return esRouteMapper.deleteEsRouteById(id);
    }
}
