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
import com.eshore.business.mapper.ResgroupbindtblMapper;
import com.eshore.business.domain.Resgroupbindtbl;
import com.eshore.business.service.IResgroupbindtblService;
import com.eshore.business.domain.form.ResgroupbindtblForm;
import org.springframework.web.multipart.MultipartFile;


/**
 * 创建资源组资源绑定Service业务层处理
 *
 * @author eshore
 * @date 2023-11-20
 */
@Service
@DataSource(value = DataSourceType.SLAVE1)
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
    /**
     * Excel批量导入资源组资源绑定信息
     */
    @Override
    public boolean importData(MultipartFile file) throws Exception {
        Date startDate = DateUtils.getNowDate();
        log.debug("工单导入开始：" + DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, startDate));
        try{
            //读取Excel中的内容
            ExcelUtil<Resgroupbindtbl> orderExcelUtil = new ExcelUtil<>(Resgroupbindtbl.class);
            List<Resgroupbindtbl> resgroupbindtbls = orderExcelUtil.importExcel(file.getInputStream());
            Assert.isCollEmpty(resgroupbindtbls,"导入内容为空");
            //校验
            validateImportData(resgroupbindtbls);
            //插入数据库并自动生成工单
            resgroupbindtbls.stream().forEach(item ->{
                //  当前设备在系统中是否已存在相同产品标识
                //  当前设备在系统中是否已存在相同产品标识
                //  判断

//                if (true){
//                    int i = checkAndCreateOrder(item);
//                    log.info("===========》 校验并自动生成工单，返回信息信息：{}",i);
//                }else {
                    resgroupbindtblMapper.insertResgroupbindtbl(item);
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
    private void validateImportData(List<Resgroupbindtbl> importDtoList){
        // 构建导入失败的信息
        int failureNum  = 0;
        StringBuilder failureMsg = new StringBuilder();

        // 校验是否为admin
        LoginUser loginUser = SecurityUtils.getLoginUser();
        for (Resgroupbindtbl resgroupbindtbl2 : importDtoList){
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




