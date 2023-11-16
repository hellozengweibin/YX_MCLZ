import com.eshore.ctmp.handler.impl.SMGPApiServiceImpl;
import com.eshore.ctmp.protocol.smgp.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SMGPListener implements Runnable {

    private SMGPApiServiceImpl api;


    public SMGPListener(SMGPApiServiceImpl api) {
        this.api = api;

    }

    public void run() {

        while (true) {
            try {
                SMGPBaseMessage baseMsg = api.receiveMsg();
                if (baseMsg == null) {
                    api.close();
                    return;
                }

                log.info("收到消息:" + baseMsg);

                if (baseMsg instanceof SMGPActiveTestMessage) {
                    SMGPActiveTestRespMessage resp = new SMGPActiveTestRespMessage();
                    resp.setSequenceNumber(baseMsg.getSequenceNumber());
                    api.sendMsg(resp);
                } else if (baseMsg instanceof SMGPActiveTestRespMessage) {
                    //do nothing;
                }

                if (baseMsg instanceof SMGPSubmitRespMessage) {
                    SMGPSubmitRespMessage resp = (SMGPSubmitRespMessage) baseMsg;
                }

                if (baseMsg instanceof SMGPDeliverMessage) {
                    SMGPDeliverMessage deliver = (SMGPDeliverMessage) baseMsg;
                    SMGPDeliverRespMessage deliverResp = new SMGPDeliverRespMessage();
                    deliverResp.setMsgId(deliver.getMsgId());
                    deliverResp.setSequenceNumber(deliver.getSequenceNumber());
                    deliverResp.setStatus(0);
                    api.sendMsg(deliverResp);

                    if (deliver.getIsReport() == 0) {//上行短信
                        log.info("=============", deliver.getSrcTermId());
                        log.info("=============", deliver.getDestTermId());
                        log.info("=============", deliver.getMsgContent());
                    } else {//状态报告
                        SMGPReportData report = deliver.getReport();
                        //report.getMsgId();  //对应submitResp.getMsgId()
                        log.info("=============", report.getStat()); //状态报告值                 
                    }
                }

            } catch (Exception e) {
                log.error("接收消息异常:" + e.getMessage(), e);
                e.printStackTrace();
                api.close();
                return;
            }
        }

    }

}
