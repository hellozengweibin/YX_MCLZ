package com.eshore.feign.yinzhu.req;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
* data	true	string	空字符串
 * company	true	string	公司名取值: BL
 * return_message	true	string	硬性规定 无需理会 直接空即可
 * result	true	int	硬性规定 无需理会 默认填0
 * actioncode	true	string	指令,取值: c2ls_get_server_terminals_status
 * token	true	string	token
 * sign	true	string	随机字符串 也可空
*/
@NoArgsConstructor
@Data
public class GetTerminalInfoReq {
    private String data;
    private String company="BL";
    private String returnMessage;
    private Integer result;
    private String actioncode;
    private String token;
    private String sign;
}
