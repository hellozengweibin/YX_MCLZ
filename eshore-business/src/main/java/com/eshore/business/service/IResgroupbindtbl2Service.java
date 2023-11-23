package com.eshore.business.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.eshore.business.domain.Resgroupbindtbl2;
import com.eshore.business.domain.form.Resgroupbindtbl2Form;
import org.springframework.web.multipart.MultipartFile;


/**
 * resgroupbindtbl2Service接口
 *
 * @author eshore
 * @date 2023-11-20
 */
public interface IResgroupbindtbl2Service extends IService<Resgroupbindtbl2> {
    /**
     * 查询resgroupbindtbl2
     *
     * @param id resgroupbindtbl2主键
     * @return resgroupbindtbl2
     */
     Resgroupbindtbl2 selectResgroupbindtbl2ById(Long id);

    /**
     * 查询resgroupbindtbl2列表
     *
     * @param resgroupbindtbl2Form resgroupbindtbl2
     * @return resgroupbindtbl2集合
     */
     List<Resgroupbindtbl2> selectResgroupbindtbl2List(Resgroupbindtbl2Form resgroupbindtbl2Form);

    /**
     * 新增resgroupbindtbl2
     *
     * @param resgroupbindtbl2 resgroupbindtbl2
     * @return 结果
     */
     int insertResgroupbindtbl2(Resgroupbindtbl2 resgroupbindtbl2);

    /**
     * 修改resgroupbindtbl2
     *
     * @param resgroupbindtbl2 resgroupbindtbl2
     * @return 结果
     */
     int updateResgroupbindtbl2(Resgroupbindtbl2 resgroupbindtbl2);

    /**
     * 批量删除resgroupbindtbl2
     *
     * @param ids 需要删除的resgroupbindtbl2主键集合
     * @return 结果
     */
     int deleteResgroupbindtbl2ByIds(Long[] ids);

    /**
     * 删除resgroupbindtbl2信息
     *
     * @param id resgroupbindtbl2主键
     * @return 结果
     */
     int deleteResgroupbindtbl2ById(Long id);
    /**
     * 批量导入
     */
    public boolean importData(MultipartFile file) throws Exception;
}
