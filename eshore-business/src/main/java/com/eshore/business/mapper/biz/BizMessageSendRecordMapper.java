package com.eshore.business.mapper.biz;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eshore.domain.entity.common.BizMessageSendRecord;

import java.util.List;

/**
 * 短信发送记录Mapper接口
 *
 * @author eshore
 * @date 2022-09-09
 */
public interface BizMessageSendRecordMapper extends BaseMapper<BizMessageSendRecord> {

    List<BizMessageSendRecord> selectBizMessageSendRecordList(BizMessageSendRecord bizMessageSendRecord);
}
