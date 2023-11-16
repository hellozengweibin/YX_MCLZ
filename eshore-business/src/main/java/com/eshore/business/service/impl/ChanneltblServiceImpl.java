package com.eshore.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eshore.business.mapper.ChanneltblMapper;
import com.eshore.business.domain.Channeltbl;
import com.eshore.business.service.IChanneltblService;
import com.eshore.business.domain.form.ChanneltblForm;


/**
 * 创建二级设备Service业务层处理
 *
 * @author eshore
 * @date 2023-11-16
 */
@Service
public class ChanneltblServiceImpl extends ServiceImpl<ChanneltblMapper,Channeltbl> implements IChanneltblService {
    @Autowired
    private ChanneltblMapper channeltblMapper;

    /**
     * 查询创建二级设备
     *
     * @param id 创建二级设备主键
     * @return 创建二级设备
     */
    @Override
    public Channeltbl selectChanneltblById(Long id) {
        return channeltblMapper.selectChanneltblById(id);
    }

    /**
     * 查询创建二级设备列表
     *
     * @param channeltblForm 创建二级设备
     * @return 创建二级设备
     */
    @Override
    public List<Channeltbl> selectChanneltblList(ChanneltblForm channeltblForm) {
        return channeltblMapper.selectChanneltblList(channeltblForm);
    }

    /**
     * 新增创建二级设备
     *
     * @param channeltbl 创建二级设备
     * @return 结果
     */
    @Override
    public int insertChanneltbl(Channeltbl channeltbl) {
        return channeltblMapper.insertChanneltbl(channeltbl);
    }

    /**
     * 修改创建二级设备
     *
     * @param channeltbl 创建二级设备
     * @return 结果
     */
    @Override
    public int updateChanneltbl(Channeltbl channeltbl) {
        return channeltblMapper.updateChanneltbl(channeltbl);
    }

    /**
     * 批量删除创建二级设备
     *
     * @param ids 需要删除的创建二级设备主键
     * @return 结果
     */
    @Override
    public int deleteChanneltblByIds(Long[] ids) {
        return channeltblMapper.deleteChanneltblByIds(ids);
    }

    /**
     * 删除创建二级设备信息
     *
     * @param id 创建二级设备主键
     * @return 结果
     */
    @Override
    public int deleteChanneltblById(Long id) {
        return channeltblMapper.deleteChanneltblById(id);
    }
}
