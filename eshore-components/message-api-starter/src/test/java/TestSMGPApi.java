import com.eshore.ctmp.config.SMGPProperties;
import com.eshore.ctmp.handler.impl.SMGPApiServiceImpl;
import com.eshore.ctmp.protocol.smgp.SMGPLoginRespMessage;
import com.eshore.ctmp.protocol.smgp.SMGPSubmitMessage;
import com.eshore.ctmp.protocol.utils.SequenceGenerator;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class TestSMGPApi {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        SMGPProperties smgpProperties = new SMGPProperties();
        String host = "124.126.119.17";
        int port = 8890;
        String clientId = "10690805";
        String password = "w21048";
        smgpProperties.setHost(host);
        smgpProperties.setPort(port);
        smgpProperties.setClientId(clientId);
        smgpProperties.setPassword(password);
        SMGPApiServiceImpl mtApi = new SMGPApiServiceImpl();
        mtApi.setSmgpProperties(smgpProperties);

        //String serviceId="10690805";
        String srcTermId = "10690805";
        String[] mobiles = new String[] {"17764116956,18002559526,18923782770"};
        String msgContent = "魏亚锋测试";
        byte msgFmt = 8;
        byte needReport = 0;
        int count = 1;
        long sleep = 1000;

        try {
            SMGPLoginRespMessage resp = mtApi.connect();
            if (resp != null && resp.getStatus() == 0) {
                System.out.println("连接成功");
            } else {
                System.out.println("连接失败,status=" + resp.getStatus());
                return;
            }
        } catch (Exception e) {
            System.out.println("网络异常");
            e.printStackTrace();
            return;
        }

        //new Thread(new  SMGPListener(mtApi)).start();
        //new Thread(new ActiveTestThread(mtApi)).start();

        for (int i = 0; i < count; i++) {
            try {
                SMGPSubmitMessage submit = new SMGPSubmitMessage();
                submit.setSequenceNumber(SequenceGenerator.nextSequence());

                submit.setPriority((byte) 3);
                submit.setSrcTermId(srcTermId);
                //submit.setChargeTermId(srcTermId);
                //submit.setServiceId(serviceId);
                submit.setDestTermIdArray(mobiles);
                submit.setMsgFmt(msgFmt);
                submit.setMsgContent(msgContent);
                //submit.setBMsgContent(msgContent.getBytes("GB18030"));
                submit.setNeedReport(needReport);
                mtApi.sendMsg(submit);

                log.info("发送消息成功:" + submit);
                Thread.sleep(sleep);
            } catch (Exception e) {
                log.error("发送消息异常:" + e.getMessage(), e);
                e.printStackTrace();
                mtApi.close();
                Thread.sleep(100);
                int k = 0;
                while (true) {
                    try {
                        SMGPLoginRespMessage resp = mtApi.connect();
                        if (resp != null && resp.getStatus() == 0) {
                            System.out.println("连接成功");
                            new Thread(new SMGPListener(mtApi)).start();
                            new Thread(new ActiveTestThread(mtApi)).start();
                            break;
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }

                    k++;
                    Thread.sleep(30000);
                }
            }
        }


    }

}
