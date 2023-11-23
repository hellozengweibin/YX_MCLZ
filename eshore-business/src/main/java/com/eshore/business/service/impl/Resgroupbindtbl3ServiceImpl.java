package com.eshore.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Date;
import java.util.List;

import com.eshore.business.domain.Resgroupbindtbl2;
import com.eshore.common.annotation.DataSource;
import com.eshore.common.core.domain.model.LoginUser;
import com.eshore.common.enums.DataSourceType;
import com.eshore.common.exception.BizException;
import com.eshore.common.utils.Assert;
import com.eshore.common.utils.DateUtils;
import com.eshore.common.utils.SecurityUtils;
import com.eshore.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eshore.business.mapper.Resgroupbindtbl3Mapper;
import com.eshore.business.domain.Resgroupbindtbl3;
import com.eshore.business.service.IResgroupbindtbl3Service;
import com.eshore.business.domain.form.Resgroupbindtbl3Form;
import org.springframework.web.multipart.MultipartFile;


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
    /**
     * Excel批量导入资源组资源绑定信息
     */
    @Override
    public boolean importData(MultipartFile file) throws Exception {
        Date startDate = DateUtils.getNowDate();
        log.debug("工单导入开始：" + DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, startDate));
        try{
            //读取Excel中的内容
            ExcelUtil<Resgroupbindtbl3> orderExcelUtil = new ExcelUtil<>(Resgroupbindtbl3.class);
            List<Resgroupbindtbl3> resgroupbindtbls3 = orderExcelUtil.importExcel(file.getInputStream());
            Assert.isCollEmpty(resgroupbindtbls3,"导入内容为空");
            //校验
            validateImportData(resgroupbindtbls3);
            //插入数据库并自动生成工单
            resgroupbindtbls3.stream().forEach(item ->{
                //  当前设备在系统中是否已存在相同产品标识
                //  当前设备在系统中是否已存在相同产品标识
                //  判断

//                if (true){
//                    int i = checkAndCreateOrder(item);
//                    log.info("===========》 校验并自动生成工单，返回信息信息：{}",i);
//                }else {
                resgroupbindtbl3Mapper.insertResgroupbindtbl3(item);
//                }
            });
            return true;
        }catch (Exception e) {
            if (e instanceof BizException) {
                throw (BizException) e;
            }
            log.error("设备导入异常：" + e.getMessage());
            e.printStackTrace();
        }
        log.debug("布控导入结束，共耗时：" + DateUtils.getDatePoor(DateUtils.getNowDate(), startDate));
        return false;
    }
    private void validateImportData(List<Resgroupbindtbl3> importDtoList){
        // 构建导入失败的信息
        int failureNum  = 0;
        StringBuilder failureMsg = new StringBuilder();

        // 校验是否为admin
        LoginUser loginUser = SecurityUtils.getLoginUser();
        for (Resgroupbindtbl3 resgroupbindtbl3 : importDtoList){
            if (resgroupbindtbl3.getBindresid() == null){
                failureNum ++;
                failureMsg.append("\r\n").append("Bindresid").append("是否为空 \r\n");
                continue;
            }
            if (resgroupbindtbl3.getBindrestype() == null){
                failureNum ++;
                failureMsg.append("\r\n").append("Bindrestype").append("是否为空 \r\n");
                continue;
            }
            if (resgroupbindtbl3.getResgroupid() == null){
                failureNum ++;
                failureMsg.append("\r\n").append("Resgroupid").append("是否为空 \r\n");
                continue;
            }
        }
    }
}
