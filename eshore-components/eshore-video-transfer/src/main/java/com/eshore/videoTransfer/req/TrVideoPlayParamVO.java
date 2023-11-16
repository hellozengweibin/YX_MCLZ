package com.eshore.videoTransfer.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName VideoPlayParamVO
 * @Description
 * @Author jianlin.liu
 * @Date 2023/6/26
 * @Version 1.0
 **/
@Data
public class TrVideoPlayParamVO {
   @ApiModelProperty("摄像头编码")
   private String cameraCode;
   private MediaURLParamVO mediaURLParam;
}
