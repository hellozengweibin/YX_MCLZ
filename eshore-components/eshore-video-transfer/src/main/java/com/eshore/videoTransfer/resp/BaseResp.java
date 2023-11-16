package com.eshore.videoTransfer.resp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class BaseResp implements Serializable {
    @JsonProperty("resultCode")
    private Integer resultCode;
    @JsonProperty("resultMsg")
    private String resultMsg;
    public boolean isSuccess(){
        return resultCode != null && resultCode == 0;
    }

}
