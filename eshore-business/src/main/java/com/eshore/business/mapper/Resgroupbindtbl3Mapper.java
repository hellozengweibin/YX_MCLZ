package com.eshore.business.mapper;

import com.eshore.common.annotation.DataSource;
import com.eshore.common.enums.DataSourceType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eshore.business.domain.Resgroupbindtbl3;
import com.eshore.business.domain.form.Resgroupbindtbl3Form;

/**
 * resgroupbindtbl3Mapper接口
 *
 * @author eshore
 * @date 2023-11-20
 */
@Mapper
public interface Resgroupbindtbl3Mapper extends BaseMapper<Resgroupbindtbl3> {
    /**
     * 查询resgroupbindtbl3
     *
     * @param id resgroupbindtbl3主键
     * @return resgroupbindtbl3
     */
    Resgroupbindtbl3 selectResgroupbindtbl3ById(Long id);

    /**
     * 查询resgroupbindtbl3列表
     *
     * @param resgroupbindtbl3 resgroupbindtbl3
     * @return resgroupbindtbl3集合
     */
    List<Resgroupbindtbl3> selectResgroupbindtbl3List(Resgroupbindtbl3Form resgroupbindtbl3Form);

    /**
     * 新增resgroupbindtbl3
     *
     * @param resgroupbindtbl3 resgroupbindtbl3
     * @return 结果
     */
    int insertResgroupbindtbl3(Resgroupbindtbl3 resgroupbindtbl3);

    /**
     * 修改resgroupbindtbl3
     *
     * @param resgroupbindtbl3 resgroupbindtbl3
     * @return 结果
     */
    int updateResgroupbindtbl3(Resgroupbindtbl3 resgroupbindtbl3);

    /**
     * 删除resgroupbindtbl3
     *
     * @param id resgroupbindtbl3主键
     * @return 结果
     */
    int deleteResgroupbindtbl3ById(Long id);

    /**
     * 批量删除resgroupbindtbl3
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteResgroupbindtbl3ByIds(Long[] ids);
}
