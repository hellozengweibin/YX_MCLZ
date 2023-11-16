package com.eshore.ctmp.protocol.smgp;


import com.eshore.ctmp.protocol.constants.SMGPConstants;

public class SMGPActiveTestRespMessage extends SMGPBaseMessage {

    public SMGPActiveTestRespMessage() {
        this.commandId = SMGPConstants.SMGP_ACTIVE_TEST_RESP;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("SMGPActiveTestRespMessage:[sequenceNumber=").append(
                sequenceString()).append("]");

        return buffer.toString();
    }
}