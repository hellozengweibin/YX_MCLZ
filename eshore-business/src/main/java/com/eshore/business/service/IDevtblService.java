package com.eshore.business.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.eshore.business.domain.Devtbl;
import com.eshore.business.domain.form.DevtblForm;


/**
 * 创建设备Service接口
 *
 * @author eshore
 * @date 2023-11-16
 */
public interface IDevtblService extends IService<Devtbl> {
    /**
     * 查询创建设备
     *
     * @param id 创建设备主键
     * @return 创建设备
     */
     Devtbl selectDevtblById(Long id);

    /**
     * 查询创建设备列表
     *
     * @param devtblForm 创建设备
     * @return 创建设备集合
     */
     List<Devtbl> selectDevtblList(DevtblForm devtblForm);

    /**
     * 新增创建设备
     *
     * @param devtbl 创建设备
     * @return 结果
     */
     int insertDevtbl(Devtbl devtbl);

    /**
     * 修改创建设备
     *
     * @param devtbl 创建设备
     * @return 结果
     */
     int updateDevtbl(Devtbl devtbl);

    /**
     * 批量删除创建设备
     *
     * @param ids 需要删除的创建设备主键集合
     * @return 结果
     */
     int deleteDevtblByIds(Long[] ids);

    /**
     * 删除创建设备信息
     *
     * @param id 创建设备主键
     * @return 结果
     */
     int deleteDevtblById(Long id);
}
