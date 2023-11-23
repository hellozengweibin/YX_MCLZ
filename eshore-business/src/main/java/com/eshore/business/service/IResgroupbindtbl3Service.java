package com.eshore.business.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.eshore.business.domain.Resgroupbindtbl3;
import com.eshore.business.domain.form.Resgroupbindtbl3Form;
import org.springframework.web.multipart.MultipartFile;


/**
 * resgroupbindtbl3Service接口
 *
 * @author eshore
 * @date 2023-11-20
 */
public interface IResgroupbindtbl3Service extends IService<Resgroupbindtbl3> {
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
     * @param resgroupbindtbl3Form resgroupbindtbl3
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
     * 批量删除resgroupbindtbl3
     *
     * @param ids 需要删除的resgroupbindtbl3主键集合
     * @return 结果
     */
     int deleteResgroupbindtbl3ByIds(Long[] ids);

    /**
     * 删除resgroupbindtbl3信息
     *
     * @param id resgroupbindtbl3主键
     * @return 结果
     */
     int deleteResgroupbindtbl3ById(Long id);

    /**
     * 批量导入
     */
    public boolean importData(MultipartFile file) throws Exception;
}
