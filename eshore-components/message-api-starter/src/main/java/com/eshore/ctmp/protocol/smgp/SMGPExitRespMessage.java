package com.eshore.ctmp.protocol.smgp;

import com.eshore.ctmp.protocol.constants.SMGPConstants;

public class SMGPExitRespMessage extends SMGPBaseMessage {

    public SMGPExitRespMessage() {
        this.commandId = SMGPConstants.SMGP_EXIT_RESP;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("SMGPExitRespMessage:[sequenceNumber=").append(
                sequenceString()).append(",");
        buffer.append("]");
        return buffer.toString();
    }
}