package com.eshore.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

import com.eshore.common.annotation.DataSource;
import com.eshore.common.enums.DataSourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eshore.business.mapper.DevtblMapper;
import com.eshore.business.domain.Devtbl;
import com.eshore.business.service.IDevtblService;
import com.eshore.business.domain.form.DevtblForm;


/**
 * 1创建设备Service业务层处理
 *
 * @author eshore
 * @date 2023-11-16
 */
@Service
@DataSource(value = DataSourceType.SLAVE1)
public class DevtblServiceImpl extends ServiceImpl<DevtblMapper,Devtbl> implements IDevtblService {
    @Autowired
    private DevtblMapper devtblMapper;

    /**
     * 查询创建设备
     *
     * @param id 创建设备主键
     * @return 创建设备
     */
    @Override
    public Devtbl selectDevtblById(Long id) {
        return devtblMapper.selectDevtblById(id);
    }

    /**
     * 查询创建设备列表
     *
     * @param devtblForm 创建设备
     * @return 创建设备
     */
    @Override
    public List<Devtbl> selectDevtblList(DevtblForm devtblForm) {
        return devtblMapper.selectDevtblList(devtblForm);
    }

    /**
     * 新增创建设备
     *
     * @param devtbl 创建设备
     * @return 结果
     */
    @Override
    public int insertDevtbl(Devtbl devtbl) {
        return devtblMapper.insertDevtbl(devtbl);
    }

    /**
     * 修改创建设备
     *
     * @param devtbl 创建设备
     * @return 结果
     */
    @Override
    public int updateDevtbl(Devtbl devtbl) {
        return devtblMapper.updateDevtbl(devtbl);
    }

    /**
     * 批量删除创建设备
     *
     * @param ids 需要删除的创建设备主键
     * @return 结果
     */
    @Override
    public int deleteDevtblByIds(Long[] ids) {
        return devtblMapper.deleteDevtblByIds(ids);
    }

    /**
     * 删除创建设备信息
     *
     * @param id 创建设备主键
     * @return 结果
     */
    @Override
    public int deleteDevtblById(Long id) {
        return devtblMapper.deleteDevtblById(id);
    }
}
