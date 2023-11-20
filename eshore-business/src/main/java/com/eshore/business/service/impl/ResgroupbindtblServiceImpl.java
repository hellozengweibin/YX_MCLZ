package com.eshore.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eshore.business.mapper.ResgroupbindtblMapper;
import com.eshore.business.domain.Resgroupbindtbl;
import com.eshore.business.service.IResgroupbindtblService;
import com.eshore.business.domain.form.ResgroupbindtblForm;


/**
 * 创建资源组资源绑定Service业务层处理
 *
 * @author eshore
 * @date 2023-11-20
 */
@Service
public class ResgroupbindtblServiceImpl extends ServiceImpl<ResgroupbindtblMapper,Resgroupbindtbl> implements IResgroupbindtblService {
    @Autowired
    private ResgroupbindtblMapper resgroupbindtblMapper;

    /**
     * 查询创建资源组资源绑定
     *
     * @param id 创建资源组资源绑定主键
     * @return 创建资源组资源绑定
     */
    @Override
    public Resgroupbindtbl selectResgroupbindtblById(Long id) {
        return resgroupbindtblMapper.selectResgroupbindtblById(id);
    }

    /**
     * 查询创建资源组资源绑定列表
     *
     * @param resgroupbindtblForm 创建资源组资源绑定
     * @return 创建资源组资源绑定
     */
    @Override
    public List<Resgroupbindtbl> selectResgroupbindtblList(ResgroupbindtblForm resgroupbindtblForm) {
        return resgroupbindtblMapper.selectResgroupbindtblList(resgroupbindtblForm);
    }

    /**
     * 新增创建资源组资源绑定
     *
     * @param resgroupbindtbl 创建资源组资源绑定
     * @return 结果
     */
    @Override
    public int insertResgroupbindtbl(Resgroupbindtbl resgroupbindtbl) {
        return resgroupbindtblMapper.insertResgroupbindtbl(resgroupbindtbl);
    }

    /**
     * 修改创建资源组资源绑定
     *
     * @param resgroupbindtbl 创建资源组资源绑定
     * @return 结果
     */
    @Override
    public int updateResgroupbindtbl(Resgroupbindtbl resgroupbindtbl) {
        return resgroupbindtblMapper.updateResgroupbindtbl(resgroupbindtbl);
    }

    /**
     * 批量删除创建资源组资源绑定
     *
     * @param ids 需要删除的创建资源组资源绑定主键
     * @return 结果
     */
    @Override
    public int deleteResgroupbindtblByIds(Long[] ids) {
        return resgroupbindtblMapper.deleteResgroupbindtblByIds(ids);
    }

    /**
     * 删除创建资源组资源绑定信息
     *
     * @param id 创建资源组资源绑定主键
     * @return 结果
     */
    @Override
    public int deleteResgroupbindtblById(Long id) {
        return resgroupbindtblMapper.deleteResgroupbindtblById(id);
    }
}
