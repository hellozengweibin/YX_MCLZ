package com.eshore.feign.yinzhu.resp;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class ExecutingTaskResp extends BaseResp{
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        private List<TaskInfoArrayDTO> TaskInfoArrayDTO;

        @NoArgsConstructor
        @Data
        public static class TaskInfoArrayDTO {
            private List<EndpointIpListDTO> EndpointIpListDTO;
            @NoArgsConstructor
            @Data
            public static class EndpointIpListDTO {
                private Integer EndPointID;
                private String EndPointPrimaryKey;
            }
            private Integer IsMonitor;
            private String MonitorTaskID;
            private String TaskBeginTime;
            private String TaskID;
            private String TaskIniator;
            private Integer TaskIniatorID;
            private String TaskName;
            private String TaskShowInfo;
            private String TaskStatus;
            private Integer TaskType;
            private Integer TaskVolume;
            private String TaskTypeName;
            private String TaskUserName;
        }
    }

    /**
     * "TaskInfoArray": [
     *  {
     *   "EndpointIpList": [//任务中终端列表
     *   {
     *   "EndPointID": 1,//终端ID
     *   "EndPointPrimaryKey": "话筒喇叭B"
     *   },
     *   {
     *   "EndPointID": 9,
     *   "EndPointPrimaryKey": "音箱2"
     *   }
     *   ],
     *   "IsMonitor": 0,//是否监听,0-否,1-是
     *   "MonitorTaskID": "",
     *   "TaskBeginTime": "2020-10-24 16:07:48",//任务开始时间
     *   "TaskID": "{ BD6F122D8BD84176AA5AD9B0AC3B0B8E }",
     *   "TaskIniator": "admin",//任务发起方
     *   "TaskIniatorID": 0,
     *   "TaskName": "test",//任务名
     *   "TaskShowInfo": "光头华夏 - 无期.mp3",//当前播放名称
     *   "TaskStatus": "task_process_work",//任务状态
     *   "TaskType": 5,//任务类型值
     *   "TaskTypeName":"client_mp3_play_type",//任务类型名称
     *   "TaskUserName": "admin",
     *   "TaskVolume": 70
     *   }
     *   ]
     */

}
