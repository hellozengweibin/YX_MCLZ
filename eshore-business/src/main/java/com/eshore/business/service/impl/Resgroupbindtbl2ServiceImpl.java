package com.eshore.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

import com.eshore.common.annotation.DataSource;
import com.eshore.common.enums.DataSourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eshore.business.mapper.Resgroupbindtbl2Mapper;
import com.eshore.business.domain.Resgroupbindtbl2;
import com.eshore.business.service.IResgroupbindtbl2Service;
import com.eshore.business.domain.form.Resgroupbindtbl2Form;


/**
 * resgroupbindtbl2Service业务层处理
 *
 * @author eshore
 * @date 2023-11-20
 */
@Service
@DataSource(value = DataSourceType.SLAVE)
public class Resgroupbindtbl2ServiceImpl extends ServiceImpl<Resgroupbindtbl2Mapper,Resgroupbindtbl2> implements IResgroupbindtbl2Service {
    @Autowired
    private Resgroupbindtbl2Mapper resgroupbindtbl2Mapper;

    /**
     * 查询resgroupbindtbl2
     *
     * @param id resgroupbindtbl2主键
     * @return resgroupbindtbl2
     */
    @Override
    public Resgroupbindtbl2 selectResgroupbindtbl2ById(Long id) {
        return resgroupbindtbl2Mapper.selectResgroupbindtbl2ById(id);
    }

    /**
     * 查询resgroupbindtbl2列表
     *
     * @param resgroupbindtbl2Form resgroupbindtbl2
     * @return resgroupbindtbl2
     */
    @Override
    public List<Resgroupbindtbl2> selectResgroupbindtbl2List(Resgroupbindtbl2Form resgroupbindtbl2Form) {
        return resgroupbindtbl2Mapper.selectResgroupbindtbl2List(resgroupbindtbl2Form);
    }

    /**
     * 新增resgroupbindtbl2
     *
     * @param resgroupbindtbl2 resgroupbindtbl2
     * @return 结果
     */
    @Override
    public int insertResgroupbindtbl2(Resgroupbindtbl2 resgroupbindtbl2) {
        return resgroupbindtbl2Mapper.insertResgroupbindtbl2(resgroupbindtbl2);
    }

    /**
     * 修改resgroupbindtbl2
     *
     * @param resgroupbindtbl2 resgroupbindtbl2
     * @return 结果
     */
    @Override
    public int updateResgroupbindtbl2(Resgroupbindtbl2 resgroupbindtbl2) {
        return resgroupbindtbl2Mapper.updateResgroupbindtbl2(resgroupbindtbl2);
    }

    /**
     * 批量删除resgroupbindtbl2
     *
     * @param ids 需要删除的resgroupbindtbl2主键
     * @return 结果
     */
    @Override
    public int deleteResgroupbindtbl2ByIds(Long[] ids) {
        return resgroupbindtbl2Mapper.deleteResgroupbindtbl2ByIds(ids);
    }

    /**
     * 删除resgroupbindtbl2信息
     *
     * @param id resgroupbindtbl2主键
     * @return 结果
     */
    @Override
    public int deleteResgroupbindtbl2ById(Long id) {
        return resgroupbindtbl2Mapper.deleteResgroupbindtbl2ById(id);
    }
}
