package com.eshore.business.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eshore.business.domain.Channeltbl;
import com.eshore.business.domain.form.ChanneltblForm;

/**
 * 创建二级设备Mapper接口
 *
 * @author eshore
 * @date 2023-11-16
 */
@Mapper
public interface ChanneltblMapper extends BaseMapper<Channeltbl> {
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
     * @param channeltbl 创建二级设备
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
     * 删除创建二级设备
     *
     * @param id 创建二级设备主键
     * @return 结果
     */
    int deleteChanneltblById(Long id);

    /**
     * 批量删除创建二级设备
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteChanneltblByIds(Long[] ids);
}
