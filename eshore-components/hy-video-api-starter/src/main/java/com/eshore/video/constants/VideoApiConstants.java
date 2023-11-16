package com.eshore.video.constants;

/**
 * @Author cjy
 * @Date 2022/7/26 20:33
 * @Version 1.0
 */
public class VideoApiConstants {
    /**
     * 实时推流接口
     */
//    public static String REAL_PLAY = "api/v1/getRealPlayUrl";
    public static String REAL_PLAY = "api/v1/getRealPlayUrlV1";

    /**
     * 回放推流接口
     */
//    public static String PLAY_BACK = "api/v1/getPlaybackUrl";
    public static String PLAY_BACK = "api/v2/getPlaybackUrlV1";

    /**
     * 回看播放控制
     */
    public static String PLAY_CONTROLLER = "playback_ctrl";

    /**
     * 刷新tokenURL
     */
//    public static String REFRESH_TOEKN = "flushToken";
    public static String REFRESH_TOEKN = "flushTokenV1";

    /**
     * 检索路径
     */
//    public static String RECORDQUERY ="recordqueryV1";
    public static String RECORDQUERY = "api/v1/recordqueryV1";
    /**
     * 下载录像地址 recordquery
     */
    public static String DOWNLOAD_URL = "recorddownload";

    /**
     * 获取视频token（通过视频流模块） 2023-03-06 ymh add
     */
    public static String VIDEO_TOKEN = "getCmsToken";

    /**
     * 获取视频账号（通过视频流模块） 2023-03-07 ymh add
     */
    public static String VIDEO_ACCOUNT = "getLoginUser";

    /**
     * 获取实时/回放视频流地址（通过视频流模块） 2023-03-07 ymh add
     */
    public static String VIDEO_STREAM_URL = "getStreamingUrl";

    /**
     * 获取截图（通过视频流模块） 2023-03-06 ymh add
     */
    public static String VIDEO_STREAM_IMG = "getVideoStreamImgByChnlId";
}
