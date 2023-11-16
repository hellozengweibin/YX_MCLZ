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
public class GetGroupResp extends BaseResp{
    private List<DataDTO> data;
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
            private Integer users_id;
            private Integer groups_users_id;
            private Integer terminals_groups_id;
            private Integer id;
            private Integer type;
            private Integer call_code;
            private Integer relay_servers_id;
            private List amplifier;
            private List power;
            private List alarm;
            private String name;
            private String ip_address;
            private String other_config;
        }
    }


//                "id": 1001, //分组id
//                "name": "fgmnmny", //分组名
//                "users_id": 2, //创建的用户id
//                "groups_users_id": 0, //用户组id
//                "call_code": 2583, //分组呼叫编码
//                "create_time": "2020-01-02 15:28:08", //创建时间
//                "update_time": "2020-01-02 15:28:08", //最后更新时间
//
//
//                "terminals": [ //分组终端
//                "terminals_id": 7, //终端id
//                 "users_id": 2, //创建的用户id
//                 "groups_users_id": 0, //用户组id
//                 "terminals_groups_id": 1001, //分组id
//                  "amplifier": [], //八分区配置
//                  "power": [], //电源配置
//                  "alarm": [], //报警配置
//                  "id": 7, // 记录索引
//                  "name": "172.16.21.109:3",//终端名
//                  "type": 1, //终端类型
//                  "ip_address": "172.16.21.109", //ip地址
//                   "call_code": 0, // 终端呼叫编码
//                   "relay_servers_id": 3, //中继id
//                   "other_config": "" //其他配置
}
