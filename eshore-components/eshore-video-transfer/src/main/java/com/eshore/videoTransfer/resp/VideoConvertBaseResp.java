package com.eshore.videoTransfer.resp;

import lombok.Data;

@Data
public class VideoConvertBaseResp {
    /**
    * 成功为1，非1为失败
    */
    private Integer code;
    private String msg;

    public boolean isSuccess(){
        return code != null && code == 1;
    }

}
