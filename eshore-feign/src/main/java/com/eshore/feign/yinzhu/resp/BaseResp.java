package com.eshore.feign.yinzhu.resp;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * company	string	公司名 无需理会
 * return_message	string	返回说明 默认空
 * result	int	返回码 200为正常
 * actioncode	string	操作码 无需理会
 * sign	string	签名 无需理会
 * token	string	token
*/
@NoArgsConstructor
@Data
public class BaseResp {
    private String company;
    private String actioncode;
    private String token;
    private Integer result;
    private String returnMessage;
    private String sign;
}
