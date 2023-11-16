package com.eshore.feign.yinzhu.resp;

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
public class GetTaskInfoResp extends BaseResp{
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        private Float CurrentTime;
        private String MusicName;
        private String PlayStatus;
        private String TaskID;
        private Integer PlayIndex;
        private Integer TotalTime;
    }
}
