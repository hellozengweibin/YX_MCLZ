package com.eshore.business.service.common.impl;

import com.eshore.business.mapper.biz.BizMessageSendRecordMapper;
import com.eshore.business.service.common.IBizMessageSendRecordService;
import com.eshore.common.utils.DateUtils;
import com.eshore.domain.entity.common.BizMessageSendRecord;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 短信发送记录Service业务层处理
 *
 * @author eshore
 * @date 2022-09-09
 */
@Service
public class BizMessageSendRecordServiceImpl implements IBizMessageSendRecordService {
    @Resource
    private BizMessageSendRecordMapper bizMessageSendRecordMapper;

    /**
     * 查询短信发送记录
     *
     * @param id 短信发送记录主键
     * @return 短信发送记录
     */
    @Override
    public BizMessageSendRecord selectBizMessageSendRecordById(Long id) {
        return bizMessageSendRecordMapper.selectById(id);
    }

    /**
     * 查询短信发送记录列表
     *
     * @param bizMessageSendRecord 短信发送记录
     * @return 短信发送记录
     */
    @Override
    public List<BizMessageSendRecord> selectBizMessageSendRecordList(BizMessageSendRecord bizMessageSendRecord) {
        return bizMessageSendRecordMapper.selectBizMessageSendRecordList(bizMessageSendRecord);
    }

    /**
     * 新增短信发送记录
     *
     * @param bizMessageSendRecord 短信发送记录
     * @return 结果
     */
    @Override
    public int insertBizMessageSendRecord(BizMessageSendRecord bizMessageSendRecord) {
        bizMessageSendRecord.setCreateTime(DateUtils.getNowDate());
        return bizMessageSendRecordMapper.insert(bizMessageSendRecord);
    }

    /**
     * 修改短信发送记录
     *
     * @param bizMessageSendRecord 短信发送记录
     * @return 结果
     */
    @Override
    public int updateBizMessageSendRecord(BizMessageSendRecord bizMessageSendRecord) {
        bizMessageSendRecord.setUpdateTime(DateUtils.getNowDate());
        return bizMessageSendRecordMapper.update(bizMessageSendRecord, null);
    }

    /**
     * 批量删除短信发送记录
     *
     * @param ids 需要删除的短信发送记录主键
     * @return 结果
     */
    @Override
    public int deleteBizMessageSendRecordByIds(Long[] ids) {
        return bizMessageSendRecordMapper.deleteBatchIds(Arrays.asList(ids.clone()));
    }

    /**
     * 删除短信发送记录信息
     *
     * @param id 短信发送记录主键
     * @return 结果
     */
    @Override
    public int deleteBizMessageSendRecordById(Long id) {
        return bizMessageSendRecordMapper.deleteById(id);
    }
}
