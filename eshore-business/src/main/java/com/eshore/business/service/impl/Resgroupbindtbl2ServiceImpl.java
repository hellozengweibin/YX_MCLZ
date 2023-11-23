package com.eshore.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Date;
import java.util.List;

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
import com.eshore.business.mapper.Resgroupbindtbl2Mapper;
import com.eshore.business.domain.Resgroupbindtbl2;
import com.eshore.business.service.IResgroupbindtbl2Service;
import com.eshore.business.domain.form.Resgroupbindtbl2Form;
import org.springframework.web.multipart.MultipartFile;


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
    /**
     * Excel批量导入资源组资源绑定信息
     */
    @Override
    public boolean importData(MultipartFile file) throws Exception {
        Date startDate = DateUtils.getNowDate();
        log.debug("工单导入开始：" + DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, startDate));
        try{
            //读取Excel中的内容
            ExcelUtil<Resgroupbindtbl2> orderExcelUtil = new ExcelUtil<>(Resgroupbindtbl2.class);
            List<Resgroupbindtbl2> resgroupbindtbls2 = orderExcelUtil.importExcel(file.getInputStream());
            Assert.isCollEmpty(resgroupbindtbls2,"导入内容为空");
            //校验
            validateImportData(resgroupbindtbls2);
            //插入数据库并自动生成工单
            resgroupbindtbls2.stream().forEach(item ->{
                //  当前设备在系统中是否已存在相同产品标识
                //  当前设备在系统中是否已存在相同产品标识
                //  判断

//                if (true){
//                    int i = checkAndCreateOrder(item);
//                    log.info("===========》 校验并自动生成工单，返回信息信息：{}",i);
//                }else {
                resgroupbindtbl2Mapper.insertResgroupbindtbl2(item);
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
    private void validateImportData(List<Resgroupbindtbl2> importDtoList){
        // 构建导入失败的信息
        int failureNum  = 0;
        StringBuilder failureMsg = new StringBuilder();

        // 校验是否为admin
        LoginUser loginUser = SecurityUtils.getLoginUser();
        for (Resgroupbindtbl2 resgroupbindtbl2 : importDtoList){
            if (resgroupbindtbl2.getBindresid() == null){
                failureNum ++;
                failureMsg.append("\r\n").append("Bindresid").append("是否为空 \r\n");
                continue;
            }
            if (resgroupbindtbl2.getBindrestype() == null){
                failureNum ++;
                failureMsg.append("\r\n").append("Bindrestype").append("是否为空 \r\n");
                continue;
            }
            if (resgroupbindtbl2.getResgroupid() == null){
                failureNum ++;
                failureMsg.append("\r\n").append("Resgroupid").append("是否为空 \r\n");
                continue;
            }
        }
    }
}

