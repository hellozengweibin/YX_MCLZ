package com.eshore.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

import com.eshore.common.annotation.DataSource;
import com.eshore.common.enums.DataSourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eshore.business.mapper.Resgroupbindtbl3Mapper;
import com.eshore.business.domain.Resgroupbindtbl3;
import com.eshore.business.service.IResgroupbindtbl3Service;
import com.eshore.business.domain.form.Resgroupbindtbl3Form;


/**
 * resgroupbindtbl3Service业务层处理
 *
 * @author eshore
 * @date 2023-11-20
 */
@Service
@DataSource(value = DataSourceType.SLAVE2)
public class Resgroupbindtbl3ServiceImpl extends ServiceImpl<Resgroupbindtbl3Mapper,Resgroupbindtbl3> implements IResgroupbindtbl3Service {
    @Autowired
    private Resgroupbindtbl3Mapper resgroupbindtbl3Mapper;

    /**
     * 查询resgroupbindtbl3
     *
     * @param id resgroupbindtbl3主键
     * @return resgroupbindtbl3
     */
    @Override
    public Resgroupbindtbl3 selectResgroupbindtbl3ById(Long id) {
        return resgroupbindtbl3Mapper.selectResgroupbindtbl3ById(id);
    }

    /**
     * 查询resgroupbindtbl3列表
     *
     * @param resgroupbindtbl3Form resgroupbindtbl3
     * @return resgroupbindtbl3
     */
    @Override
    public List<Resgroupbindtbl3> selectResgroupbindtbl3List(Resgroupbindtbl3Form resgroupbindtbl3Form) {
        return resgroupbindtbl3Mapper.selectResgroupbindtbl3List(resgroupbindtbl3Form);
    }

    /**
     * 新增resgroupbindtbl3
     *
     * @param resgroupbindtbl3 resgroupbindtbl3
     * @return 结果
     */
    @Override
    public int insertResgroupbindtbl3(Resgroupbindtbl3 resgroupbindtbl3) {
        return resgroupbindtbl3Mapper.insertResgroupbindtbl3(resgroupbindtbl3);
    }

    /**
     * 修改resgroupbindtbl3
     *
     * @param resgroupbindtbl3 resgroupbindtbl3
     * @return 结果
     */
    @Override
    public int updateResgroupbindtbl3(Resgroupbindtbl3 resgroupbindtbl3) {
        return resgroupbindtbl3Mapper.updateResgroupbindtbl3(resgroupbindtbl3);
    }

    /**
     * 批量删除resgroupbindtbl3
     *
     * @param ids 需要删除的resgroupbindtbl3主键
     * @return 结果
     */
    @Override
    public int deleteResgroupbindtbl3ByIds(Long[] ids) {
        return resgroupbindtbl3Mapper.deleteResgroupbindtbl3ByIds(ids);
    }

    /**
     * 删除resgroupbindtbl3信息
     *
     * @param id resgroupbindtbl3主键
     * @return 结果
     */
    @Override
    public int deleteResgroupbindtbl3ById(Long id) {
        return resgroupbindtbl3Mapper.deleteResgroupbindtbl3ById(id);
    }
}
