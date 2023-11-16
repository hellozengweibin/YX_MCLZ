import com.eshore.ctmp.handler.impl.SMGPApiServiceImpl;
import com.eshore.ctmp.protocol.smgp.SMGPActiveTestMessage;

import java.io.IOException;

public class ActiveTestThread implements Runnable {

    private SMGPApiServiceImpl api;

    public ActiveTestThread(SMGPApiServiceImpl api) {
        this.api = api;
    }

    public void run() {
        while (true) {
            SMGPActiveTestMessage active = new SMGPActiveTestMessage();
            try {
                api.sendMsg(active);
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

}
