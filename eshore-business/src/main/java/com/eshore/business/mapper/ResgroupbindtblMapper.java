package com.eshore.business.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eshore.business.domain.Resgroupbindtbl;
import com.eshore.business.domain.form.ResgroupbindtblForm;

/**
 * 创建资源组资源绑定Mapper接口
 *
 * @author eshore
 * @date 2023-11-20
 */
@Mapper
public interface ResgroupbindtblMapper extends BaseMapper<Resgroupbindtbl> {
    /**
     * 查询创建资源组资源绑定
     *
     * @param id 创建资源组资源绑定主键
     * @return 创建资源组资源绑定
     */
    Resgroupbindtbl selectResgroupbindtblById(Long id);

    /**
     * 查询创建资源组资源绑定列表
     *
     * @param resgroupbindtbl 创建资源组资源绑定
     * @return 创建资源组资源绑定集合
     */
    List<Resgroupbindtbl> selectResgroupbindtblList(ResgroupbindtblForm resgroupbindtblForm);

    /**
     * 新增创建资源组资源绑定
     *
     * @param resgroupbindtbl 创建资源组资源绑定
     * @return 结果
     */
    int insertResgroupbindtbl(Resgroupbindtbl resgroupbindtbl);

    /**
     * 修改创建资源组资源绑定
     *
     * @param resgroupbindtbl 创建资源组资源绑定
     * @return 结果
     */
    int updateResgroupbindtbl(Resgroupbindtbl resgroupbindtbl);

    /**
     * 删除创建资源组资源绑定
     *
     * @param id 创建资源组资源绑定主键
     * @return 结果
     */
    int deleteResgroupbindtblById(Long id);

    /**
     * 批量删除创建资源组资源绑定
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteResgroupbindtblByIds(Long[] ids);
}
