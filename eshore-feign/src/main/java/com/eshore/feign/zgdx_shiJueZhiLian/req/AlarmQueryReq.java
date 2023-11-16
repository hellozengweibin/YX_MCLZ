package com.eshore.feign.zgdx_shiJueZhiLian.req;

import lombok.Data;

import java.util.List;

@Data
public class AlarmQueryReq {
    private String memberkey;
    private List<String> deviceids;
    private List<Integer> types;
    private String starttime;
    private String endtime;
    private Integer resulttype;
    private Integer pagesize;
    private Integer pagenum;
}
