package com.eshore.videoTransfer.req;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
* ip	是	是	String	Nvr设备ip
 * port	是	是	String	Nvr端口
 * user	是	是	String	Nvr账户
 * passwd	是	是	String	Nvr密码
 * channelNum	是	是	String	待下载通道
 * storagePath	是	是	String	存储路径
 * fileName	是	是	String	文件名
 * beginTime	是	是	String	开始时间
 * endTime	是	是	String	结束时间
*/
@NoArgsConstructor
@Data
public class DownloadVideoReq {
    private String ip;
    private String port;
    private String user;
    private String passwd;
    private String channelNum;
    private String storagePath;
    private String fileName;
    private String beginTime;
    private String endTime;
}
