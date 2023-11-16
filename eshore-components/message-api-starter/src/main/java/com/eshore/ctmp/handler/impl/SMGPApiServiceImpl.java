package com.eshore.ctmp.handler.impl;


import com.eshore.ctmp.config.SMGPProperties;
import com.eshore.ctmp.handler.AbstractMessageService;
import com.eshore.ctmp.handler.MessageApi;
import com.eshore.ctmp.model.MessageVo;
import com.eshore.ctmp.protocol.constants.SMGPConstants;
import com.eshore.ctmp.protocol.smgp.*;
import com.eshore.ctmp.protocol.utils.ByteUtil;
import com.eshore.ctmp.protocol.utils.MD5;
import com.eshore.ctmp.protocol.utils.SequenceGenerator;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Slf4j
public class SMGPApiServiceImpl extends AbstractMessageService<SMGPProperties> implements MessageApi {

    public static final byte MT = 0;
    public static final byte MO = 1;
    public static final byte MT_MO = 2;

    private Socket socket;
    private boolean connected;

    private SMGPProperties smgpProperties;

    private Object readLock = new Object();
    private Object writeLock = new Object();

    public SMGPApiServiceImpl() {
    }

    public SMGPApiServiceImpl(SMGPProperties smgpProperties) {
        this.buildConfig(smgpProperties);
    }


    //作为服务端使用
    public SMGPApiServiceImpl(Socket socket) {
        this.socket = socket;
        this.connected = true;
    }

    @Override
    public int connect_() throws IOException {
        try {
            SMGPLoginRespMessage loginRespMessage = this.connect();
            return loginRespMessage.getStatus();
        } finally {
            this.disconnect();
        }
    }

    /**
     * 连接
     *
     * @return
     * @throws IOException
     */
    public synchronized SMGPLoginRespMessage connect() throws IOException {
        disconnect();
        socket = new Socket(smgpProperties.getHost(), smgpProperties.getPort());
        SMGPLoginMessage loginMsg = new SMGPLoginMessage();
        loginMsg.setClientId(smgpProperties.getClientId());
        loginMsg.setLoginMode(smgpProperties.getLoginMode());
        loginMsg.setVersion(smgpProperties.getVersion());

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmss");
        String tmp = dateFormat.format(calendar.getTime());
        loginMsg.setTimestamp(Integer.parseInt(tmp));

        loginMsg.setClientAuth(MD5.md5(smgpProperties.getClientId(), smgpProperties.getPassword(), tmp));

        loginMsg.setSequenceNumber(SequenceGenerator.nextSequence());

        try {
            writeBytes(loginMsg.toBytes());
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }

        SMGPBaseMessage baseMsg = null;

        byte[] header = new byte[SMGPBaseMessage.SZ_HEADER];
        byte[] cmdBytes = null;

        //begin process read data
        try {
            readBytes(header, 0, SMGPBaseMessage.SZ_HEADER);
            int cmdLen = ByteUtil.byte2int(header, 0);

            if (cmdLen > 8096 || cmdLen < SMGPBaseMessage.SZ_HEADER) {
                close();
                throw new IOException("read stream error,cmdLen=" + cmdLen + ",close connection");
            }
            cmdBytes = new byte[cmdLen];
            System.arraycopy(header, 0, cmdBytes, 0, SMGPBaseMessage.SZ_HEADER);
            readBytes(cmdBytes, SMGPBaseMessage.SZ_HEADER, cmdLen - SMGPBaseMessage.SZ_HEADER);
        } catch (IOException readError) {
            close();
            throw readError;
        }

        try {
            baseMsg = SMGPConstants.fromBytes(cmdBytes);

        } catch (Exception e) {
            close();
            e.printStackTrace();
            throw new IOException("build SMGPBaseMessage error:" + e.getMessage());
        }
        if (baseMsg == null) return null;
        if (baseMsg instanceof SMGPLoginRespMessage) {
            SMGPLoginRespMessage resp = (SMGPLoginRespMessage) baseMsg;
            if (resp.getStatus() == 0) {
                connected = true;
                return resp;
            } else {
                log.error("=======[SMGP]=>bind fail,status:{} => result:" + resp, resp.getStatus());
                // return resp;
                throw new RuntimeException("[SMGP] => properties:[" + smgpProperties.toString() + "] bind fail,result:" + resp.getStatus());
            }
        } else {
            close();
            throw new IOException("the first packet was not SMGPBindRespMessage:" + baseMsg);
        }
    }

    /**
     * 断开
     */
    public synchronized void disconnect() {
        if (!connected) return;
        SMGPExitMessage term = new SMGPExitMessage();
        try {
            sendMsg(term);
            SMGPBaseMessage baseMsg = null;
            while ((baseMsg = receiveMsg()) != null) {
                if (baseMsg instanceof SMGPExitRespMessage) {
                    break;
                }
            }
            close();
            connected = false;
        } catch (IOException e) {
            //ignore
        }

    }

    /**
     * 发送短信
     *
     * @param messageVo baseMsg消息
     * @throws IOException
     */
    @Override
    public int sendMsg(MessageVo messageVo) {
        //String serviceId="10690805";
        String srcTermId = "10690805";
        String[] mobiles = new String[] {messageVo.getPhone()};
        String msgContent = messageVo.getContent();
        byte msgFmt = messageVo.getMsgFmt();
        byte priority = messageVo.getPriority();
        byte needReport = 1;
        int count = 1;
        long sleep = 1000;

        String hostStr = smgpProperties.getHost() + ":" + smgpProperties.getPort();
        try {
            SMGPLoginRespMessage resp = connect();
            if (resp != null && resp.getStatus() == 0) {
                log.info(">>>>>>>>>短信服务 [{}] 连接成功", hostStr);
            } else {
                log.error(">>>>>>>>>>短信服务[{}]连接失败,status=" + resp.getStatus(), hostStr);
                return 0;
            }
        } catch (Exception e) {
            log.error(">>>>>>>>>>>>连接短信网关[{}]网络异常", hostStr);
            return 0;
        }
        try {
            SMGPSubmitMessage submit = new SMGPSubmitMessage();
            // submit.setSequenceNumber(SequenceGenerator.nextSequence());
            if (submit.getSequenceNumber() == 0) {
                submit.setSequenceNumber(SequenceGenerator.nextSequence());
            }

            submit.setPriority(priority);
            submit.setSrcTermId(srcTermId);
            //submit.setChargeTermId(srcTermId);
            //submit.setServiceId(serviceId);
            submit.setDestTermIdArray(mobiles);
            submit.setMsgFmt(msgFmt);
            submit.setMsgContent(msgContent);
            //submit.setBMsgContent(msgContent.getBytes("GB18030"));
            submit.setNeedReport(needReport);

            try {
                writeBytes(submit.toBytes());
                SMGPBaseMessage baseMsg = null;
                while ((baseMsg = receiveMsg()) != null) {
                    if (baseMsg instanceof SMGPSubmitRespMessage) {
                        break;
                    }
                }
                SMGPSubmitRespMessage respMessage = (SMGPSubmitRespMessage) baseMsg;
                if (respMessage.getStatus() == 0) {
                    log.info("发送消息成功:{} => resp:{}", submit, respMessage);
                } else {
                    log.error("发送消息失败:{} => resp:{}", submit, respMessage);
                }
                return respMessage.getStatus() == 0 ? 1 : 0;
            } catch (Exception e) {
                log.error(">>>>>>>>>>短信发送失败：", e);
                throw new IOException(e.getMessage());
            }
        } catch (Exception e) {
            log.error("发送消息异常:" + e.getMessage(), e);
            e.printStackTrace();
        } finally {
            this.close();
        }
        return 0;
    }

    public void sendMsg(SMGPBaseMessage baseMsg) throws IOException {
        if (baseMsg == null) return;
        if (!connected) {
            throw new IOException("socket has not established");
        }
        if (baseMsg.getSequenceNumber() == 0) {
            baseMsg.setSequenceNumber(SequenceGenerator.nextSequence());
        }
        try {
            writeBytes(baseMsg.toBytes());
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }

    }

    /**
     * 接收消息
     *
     * @return
     * @throws IOException
     */
    public SMGPBaseMessage receiveMsg() throws IOException {

        if (!connected) {
            throw new IOException("socket has not established");
        }
        byte[] header = new byte[SMGPBaseMessage.SZ_HEADER];
        byte[] cmdBytes = null;

        //begin process read data
        try {
            readBytes(header, 0, SMGPBaseMessage.SZ_HEADER);
            int cmdLen = ByteUtil.byte2int(header, 0);

            if (cmdLen > 8096 || cmdLen < SMGPBaseMessage.SZ_HEADER) {
                close();
                throw new IOException("read stream error,cmdLen=" + cmdLen + ",close connection");
            }
            cmdBytes = new byte[cmdLen];
            System.arraycopy(header, 0, cmdBytes, 0, SMGPBaseMessage.SZ_HEADER);
            readBytes(cmdBytes, SMGPBaseMessage.SZ_HEADER, cmdLen - SMGPBaseMessage.SZ_HEADER);
        } catch (IOException readError) {
            close();
            throw readError;
        }

        try {
            SMGPBaseMessage baseMsg = SMGPConstants.fromBytes(cmdBytes);
            return baseMsg;
        } catch (Exception e) {
            e.printStackTrace();
            close();
            throw new IOException("build SMGPBaseMessage error:" + e.getMessage());
        }
    }


    private void readBytes(byte[] bytes, int offset, int len) throws IOException {
        if (socket.isClosed())
            throw new IOException("Socket is closed");
        socket.setSoTimeout(smgpProperties.getSoTimeout());
        InputStream in = socket.getInputStream();
        int count = 0;
        synchronized (readLock) {
            while (len > 0 && (count = in.read(bytes, offset, len)) > -1) {
                offset += count;
                len -= count;
            }
        }

    }

    private void writeBytes(byte[] bytes) throws IOException {
        if (socket.isClosed())
            throw new IOException("Socket is closed");
        synchronized (writeLock) {
            OutputStream out = socket.getOutputStream();
            out.write(bytes);
            out.flush();
        }
    }

    /**
     * 关闭socket连接
     */
    public void close() {
        try {
            socket.close();
            connected = false;

        } catch (Exception e) { //ignore
        }

        synchronized (readLock) {
            readLock.notifyAll();
        }

        synchronized (writeLock) {
            writeLock.notifyAll();
        }

    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }


    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public SMGPProperties getSmgpProperties() {
        return smgpProperties;
    }

    public void setSmgpProperties(SMGPProperties smgpProperties) {
        this.smgpProperties = smgpProperties;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("SMGPApi:[clientId=").append(smgpProperties.getClientId()).append(",")
                .append("host=").append(smgpProperties.getHost()).append(",")
                .append("port=").append(smgpProperties.getPort()).append(",")
                .append("password=").append(smgpProperties.getPassword()).append(",")
                .append("loginMode=").append(smgpProperties.getLoginMode()).append("]");
        return buffer.toString();
    }

    @Override
    protected SMGPProperties buildConfig(SMGPProperties properties) {
        if (properties == null) {
            throw new RuntimeException("短信配置项信息不能为空");
        }
        log.info(">>>>>>>>>>SMGP配置属性:{}", properties);
        this.smgpProperties = properties;
        return this.smgpProperties;
    }
}