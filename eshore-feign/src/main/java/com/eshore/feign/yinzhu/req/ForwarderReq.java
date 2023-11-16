package com.eshore.feign.yinzhu.req;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 参数	必选	类型	说明
 * data	true	string	取值 空字符串
 * company	true	string	公司名取值: BL
 * return_message	true	string	硬性规定 无需理会 直接空即可
 * result	true	int	硬性规定 无需理会 默认填0
 * actioncode	true	string	指令,取值: c2ls_get_server_music_list
 * token	true	string	token
 * sign	true	string	随机字符串 也可空
*/
@NoArgsConstructor
@Data
public class ForwarderReq {
    private String company="BL";
    private String actioncode;
    private String token;
    private Object data;
    private Integer result;
    private String sign;
    private String returnMessage;
}
