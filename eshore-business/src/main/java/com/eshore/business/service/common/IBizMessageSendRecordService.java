package com.eshore.business.service.common;


import com.eshore.domain.entity.common.BizMessageSendRecord;

import java.util.List;

/**
 * 短信发送记录Service接口
 *
 * @author eshore
 * @date 2022-09-09
 */
public interface IBizMessageSendRecordService {
    /**
     * 查询短信发送记录
     *
     * @param id 短信发送记录主键
     * @return 短信发送记录
     */
    public BizMessageSendRecord selectBizMessageSendRecordById(Long id);

    /**
     * 查询短信发送记录列表
     *
     * @param bizMessageSendRecord 短信发送记录
     * @return 短信发送记录集合
     */
    public List<BizMessageSendRecord> selectBizMessageSendRecordList(BizMessageSendRecord bizMessageSendRecord);

    /**
     * 新增短信发送记录
     *
     * @param bizMessageSendRecord 短信发送记录
     * @return 结果
     */
    public int insertBizMessageSendRecord(BizMessageSendRecord bizMessageSendRecord);

    /**
     * 修改短信发送记录
     *
     * @param bizMessageSendRecord 短信发送记录
     * @return 结果
     */
    public int updateBizMessageSendRecord(BizMessageSendRecord bizMessageSendRecord);

    /**
     * 批量删除短信发送记录
     *
     * @param ids 需要删除的短信发送记录主键集合
     * @return 结果
     */
    public int deleteBizMessageSendRecordByIds(Long[] ids);

    /**
     * 删除短信发送记录信息
     *
     * @param id 短信发送记录主键
     * @return 结果
     */
    public int deleteBizMessageSendRecordById(Long id);
}
