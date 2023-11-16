package com.eshore.feign.yinzhu.resp;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MP3PlayResp extends BaseResp{
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        private String taskID;
        private String taskStatus;
        private Integer remoteID;
        private String remoteType;
    }

    /**
    * "TaskID" : "{098ADF3F20E54AFAAE23AA5DB509176D}",//任务ID
     *       "TaskStatus" : "task_process_work",//任务当前状态
     *       "RemoteID" : 0,//远程任务ID标识,由服务器建立的
     *       "RemoteType" : "",//远程任务类型,不是远程任务为空
    */
}
