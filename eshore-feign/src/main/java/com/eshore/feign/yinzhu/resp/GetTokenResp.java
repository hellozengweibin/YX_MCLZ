package com.eshore.feign.yinzhu.resp;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
* data	int	正常返回为空, 返回logged表示账号已登录
 * company	string	公司名 无需理会
 * device_name	string	设备名 无需理会
 * return_message	string	返回说明 默认空
 * result	int	返回码 200为正常
 * actioncode	string	操作码 无需理会
 * sign	string	签名 无需理会
 * token	string	token
*/
@NoArgsConstructor
@Data
public class GetTokenResp {
    private Integer data;
    private String company;
    private String deviceName;
    private String returnMessage;
    private Integer result;
    private String actioncode;
    private String sign;
    private String token;
}
