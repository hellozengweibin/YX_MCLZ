package com.eshore.feign.yinzhu.resp;

import com.alibaba.fastjson.annotation.JSONField;
import com.eshore.feign.yinzhu.req.CreateGroupReq;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 *  参数   说明
 *  CurrentTime 当前歌曲的时长,ms为单位,用于计算进度
 *  MusicName  当前歌曲名称
 *  PlayStatus  当前状态,”play” 正在播放,”pause” 暂停,”stop”停止
 *  TaskID  任务ID,任务惟一标识
 *  PlayIndex 当前正在播放的歌曲序号,也就是第几首歌曲
 *  TotalTime 歌曲总时长,ms为单位
 */
@NoArgsConstructor
@Data
public class CreateGroupResp extends BaseResp{
    private DataDTO data;
    private String device_name;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        private String name;
        private String create_time;
        private String update_time;
        private String id;
        private Integer call_code;
        private Integer users_id;
        private Integer groups_users_id;
        private List<TerminalsDto> terminals;

        @NoArgsConstructor
        @Data
        public static class TerminalsDto{
            private Integer terminals_id;
        }
    }
}
