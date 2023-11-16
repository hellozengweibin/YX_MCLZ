package com.eshore.ctmp.protocol.smgp;

import com.eshore.ctmp.protocol.utils.ByteUtil;

public class SMGPBaseMessage {

    public static final int SZ_HEADER = 12;

    protected int commandLength = 0;

    protected int commandId = 0;

    protected int sequenceNumber = 0;

    public boolean fromBytes(byte[] bytes) throws Exception {
        if (bytes == null) {
            return false;
        }
        if (bytes.length < SZ_HEADER) {
            return false;
        }

        commandLength = ByteUtil.byte2int(bytes, 0);
        commandId = ByteUtil.byte2int(bytes, 4);
        sequenceNumber = ByteUtil.byte2int(bytes, 8);
        byte[] bodyBytes = new byte[commandLength - SZ_HEADER];
        System.arraycopy(bytes, SZ_HEADER, bodyBytes, 0, bodyBytes.length);
        setBody(bodyBytes);
        return true;
    }

    public byte[] toBytes() throws Exception {
        byte[] bodyBytes = getBody();
        commandLength = SZ_HEADER + bodyBytes.length;
        byte[] bytes = new byte[commandLength];

        ByteUtil.int2byte(commandLength, bytes, 0);
        ByteUtil.int2byte(commandId, bytes, 4);
        ByteUtil.int2byte(sequenceNumber, bytes, 8);

        System.arraycopy(bodyBytes, 0, bytes, SZ_HEADER, bodyBytes.length);

        return bytes;
    }

    protected void setBody(byte[] bodyBytes) throws Exception {

    }

    protected byte[] getBody() throws Exception {
        return new byte[0];
    }


    protected String plus86(String mobile) {
        if (mobile == null || mobile.trim().length() == 0) return "";
        if (mobile.startsWith("86")) return mobile;
        if (mobile.startsWith("+86")) return mobile.substring(1);
        return "86" + mobile;
    }

    protected String minus86(String mobile) {
        if (mobile == null || mobile.trim().length() == 0) return "";
        if (mobile.startsWith("86")) return mobile.substring(2);
        if (mobile.startsWith("+86")) return mobile.substring(3);
        return mobile;

    }


    public int getCommandId() {
        return commandId;
    }

    public void setCommandId(int commandId) {
        this.commandId = commandId;
    }

    public int getCommandLength() {
        return commandLength;
    }

    public void setCommandLength(int commandLength) {
        this.commandLength = commandLength;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }


    public String sequenceString() {
        StringBuffer buffer = new StringBuffer();
        int offset = 0;
        byte[] seqBytes = new byte[8];
        System.arraycopy(ByteUtil.int2byte(sequenceNumber), offset, seqBytes, 4, 4);
        buffer.append(ByteUtil.byte2long(seqBytes));
        return buffer.toString();

    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("SMGPBaseMessage:[sequenceNumber=").append(sequenceString()).append(",").append(
                "commandId=").append(commandId).append("]");

        return buffer.toString();
    }

}
