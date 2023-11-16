package com.eshore.feign.zgdx_shiJueZhiLian;

import com.eshore.feign.zgdx_shiJueZhiLian.req.AlarmQueryReq;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest
class ShijueZhiLianServiceTest {

    String memberKey = "oqcyjmJpaHZsBU2RIim0";

    @Autowired
    private ShijueZhiLianService shijueZhiLianService;

    @Test
    public void alarmQuery() {
        AlarmQueryReq req = new AlarmQueryReq();
        req.setMemberkey(memberKey);
        ArrayList<Integer> types = new ArrayList<>();
        types.add(2);
        req.setTypes(types);
        req.setStarttime("2022-08-14 17:33:14");
        req.setEndtime("2022-08-15 17:33:14");
        req.setResulttype(0);
        req.setPagesize(99);
        req.setPagenum(1);
        shijueZhiLianService.alarmQuery(req);
    }


}