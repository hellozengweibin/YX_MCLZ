package com.eshore.business.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.eshore.business.domain.Channeltbl;
import com.eshore.business.domain.form.ChanneltblForm;


/**
 * 创建二级设备Service接口
 *
 * @author eshore
 * @date 2023-11-16
 */
public interface IChanneltblService extends IService<Channeltbl> {
    /**
     * 查询创建二级设备
     *
     * @param id 创建二级设备主键
     * @return 创建二级设备
     */
     Channeltbl selectChanneltblById(Long id);

    /**
     * 查询创建二级设备列表
     *
     * @param channeltblForm 创建二级设备
     * @return 创建二级设备集合
     */
     List<Channeltbl> selectChanneltblList(ChanneltblForm channeltblForm);

    /**
     * 新增创建二级设备
     *
     * @param channeltbl 创建二级设备
     * @return 结果
     */
     int insertChanneltbl(Channeltbl channeltbl);

    /**
     * 修改创建二级设备
     *
     * @param channeltbl 创建二级设备
     * @return 结果
     */
     int updateChanneltbl(Channeltbl channeltbl);

    /**
     * 批量删除创建二级设备
     *
     * @param ids 需要删除的创建二级设备主键集合
     * @return 结果
     */
     int deleteChanneltblByIds(Long[] ids);

    /**
     * 删除创建二级设备信息
     *
     * @param id 创建二级设备主键
     * @return 结果
     */
     int deleteChanneltblById(Long id);
}
