package com.eshore.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eshore.domain.model.form.SysNoticeForm;
import com.eshore.system.domain.SysNotice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 公告管理Mapper接口
 *
 * @author eshore
 * @date 2023-07-03
 */
@Mapper
public interface SysNoticeMapper extends BaseMapper<SysNotice> {
    /**
     * 查询公告管理
     *
     * @param noticeId 公告管理主键
     * @return 公告管理
     */
    public SysNotice selectSysNoticeByNoticeId(Integer noticeId);

    /**
     * 查询公告管理列表
     *
     * @param sysNoticeForm 公告管理
     * @return 公告管理集合
     */
    public List<SysNotice> selectSysNoticeList(SysNoticeForm sysNoticeForm);

    /**
     * 新增公告管理
     *
     * @param sysNotice 公告管理
     * @return 结果
     */
    public int insertSysNotice(SysNotice sysNotice);

    /**
     * 修改公告管理
     *
     * @param sysNotice 公告管理
     * @return 结果
     */
    public int updateSysNotice(SysNotice sysNotice);

    /**
     * 删除公告管理
     *
     * @param noticeId 公告管理主键
     * @return 结果
     */
    public int deleteSysNoticeByNoticeId(Integer noticeId);

    /**
     * 批量删除公告管理
     *
     * @param noticeIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysNoticeByNoticeIds(Integer[] noticeIds);
}
