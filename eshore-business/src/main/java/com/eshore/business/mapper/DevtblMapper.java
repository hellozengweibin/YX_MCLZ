package com.eshore.business.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eshore.business.domain.Devtbl;
import com.eshore.business.domain.form.DevtblForm;

/**
 * 创建设备Mapper接口
 *
 * @author eshore
 * @date 2023-11-16
 */
@Mapper
public interface DevtblMapper extends BaseMapper<Devtbl> {
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
     * @param devtbl 创建设备
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
     * 删除创建设备
     *
     * @param id 创建设备主键
     * @return 结果
     */
    int deleteDevtblById(Long id);

    /**
     * 批量删除创建设备
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteDevtblByIds(Long[] ids);
}
