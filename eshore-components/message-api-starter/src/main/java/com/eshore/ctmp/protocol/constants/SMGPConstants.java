package com.eshore.ctmp.protocol.constants;

import com.eshore.ctmp.protocol.smgp.*;
import com.eshore.ctmp.protocol.utils.ByteUtil;

/**
 * 请求标识常量
 */
public class SMGPConstants {

    public static final int SMGP_LOGIN = 0x00000001;

    public static final int SMGP_LOGIN_RESP = 0x80000001;

    public static final int SMGP_SUBMIT = 0x00000002;

    public static final int SMGP_SUBMIT_RESP = 0x80000002;

    public static final int SMGP_DELIVER = 0x00000003;

    public static final int SMGP_DELIVER_RESP = 0x80000003;

    public static final int SMGP_ACTIVE_TEST = 0x00000004;

    public static final int SMGP_ACTIVE_TEST_RESP = 0x80000004;

    public static final int SMGP_EXIT_TEST = 0x00000006;

    public static final int SMGP_EXIT_RESP = 0x80000006;

    public static SMGPBaseMessage fromBytes(byte[] bytes) throws Exception {
        if (bytes == null) {
            return null;
        }
        if (bytes.length < 12) {
            return null;
        }

        int commandLength = ByteUtil.byte2int(bytes, 0);
        int commandId = ByteUtil.byte2int(bytes, 4);

        SMGPBaseMessage baseMsg = null;
        switch (commandId) {
            case SMGP_LOGIN:
                baseMsg = new SMGPLoginMessage();
                break;
            case SMGP_LOGIN_RESP:
                baseMsg = new SMGPLoginRespMessage();
                break;
            case SMGP_SUBMIT:
                baseMsg = new SMGPSubmitMessage();
                break;
            case SMGP_SUBMIT_RESP:
                baseMsg = new SMGPSubmitRespMessage();
                break;
            case SMGP_DELIVER:
                baseMsg = new SMGPDeliverMessage();
                break;
            case SMGP_DELIVER_RESP:
                baseMsg = new SMGPDeliverRespMessage();
                break;
            case SMGP_ACTIVE_TEST:
                baseMsg = new SMGPActiveTestMessage();
                break;
            case SMGP_ACTIVE_TEST_RESP:
                baseMsg = new SMGPActiveTestRespMessage();
                break;
            case SMGP_EXIT_TEST:
                baseMsg = new SMGPExitMessage();
                break;
            case SMGP_EXIT_RESP:
                baseMsg = new SMGPExitRespMessage();
                break;
            default:
                baseMsg = new SMGPBaseMessage();
                break;
        }
        baseMsg.fromBytes(bytes);
        return baseMsg;
    }

}