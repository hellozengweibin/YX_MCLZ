package com.eshore.ctmp.consts;

/**
 * @ClassName CommonConsts
 * @Description 公共常量
 * @Author jianlin.liu
 * @Date 2023/4/23
 * @Version 1.0
 **/
public class CommonConsts {

    // 消息类型
    interface MsgType {
        int TYPE_0 = 0;  // 0＝MO消息（终端发给SP）；
        int TYPE_6 = 0;  //6＝MT消息（SP发给终端，包括WEB上发送的点对点短消息）；
        int TYPE_7 = 0;  //7＝点对点短消息；
    }

    // 是否要求返回状态报告:0＝不要求返回状态报告；1＝要求返回状态报告；
    interface NeedReport {
        int NEED = 0;
        int NOT_NEED = 1;
    }

    // 优先级：0＝低优先级；1＝普通优先级；2＝较高优先级；3＝高优先级；
    interface Priority {
        int P_0 = 0;
        int P_1 = 1;
        int P_2 = 2;
        int P_3 = 3;
    }

    // 短消息内容体的编码格式
    // 对于文字短消息，要求MsgFormat＝15。对于回执消息，要求MsgFormat＝0
    interface MsgFormat {
        int M_0 = 0; // ASCII编码；
        int M_3 = 3; // 短消息写卡操作
        int M_4 = 4; // 二进制短消息
        int M_8 = 8; // UCS2编码
        int M_15 = 15; // GB18030编码
        int M_246 = 246; // (U)SIM相关消息；
    }
}
