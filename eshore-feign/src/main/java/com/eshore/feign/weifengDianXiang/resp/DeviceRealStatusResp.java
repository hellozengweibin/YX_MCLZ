package com.eshore.feign.weifengDianXiang.resp;

import lombok.Data;
import lombok.NoArgsConstructor;
/**
* 参数名	解释
 * deviceid	设备ID
 * voltage	电压，V*1000
 * electricCurrent	电流，A*1000
 * power	功率，W
 * temperature	温度，摄氏度
 * doorstatus	柜门状态，1打开，0关闭
 * filllightstatus	补光灯状态（同上）
 * boxarmingstatus	箱体布防状态（同上）
 * flashlightstatus	照明灯01状态（同上）
 * heaterstatus	加热器状态（同上）
 * camerastatus	摄像机状态（同上）
 * switchstatus	交换机状态（同上）
 * fanstatus	风扇状态（同上）
 * buzzer2status	蜂鸣器02状态（同上）
 * fwarning	F告警（同上）
 * boxopennum	箱体打开次数
 * power12v01	12VO1，1打开，0关闭
 * power12v02	12VO2，1打开，0关闭
 * sysHumidity	湿度，xx%
 * windSpeed	风速，m/s
 * windDirect	风向
 * temperatureAir	气温
 * shockX	震动加速度x
 * shockY	震动加速度Y
 * shockZ	震动加速度Z
 * bumpX	撞击加速度X
 * bumpY	撞击加速度Y
 * bumpZ	撞击加速度Z
 * angleX	倾斜角度X
 * angleY	倾斜角度Y
 * angleZ	倾斜角度Z
 * electricQuantity	当前电表读数，kwh
 * lastElectricQuantity	上一次电表读数，kwh
 * fanSpeed	风扇转速，hz
 * fanState	风扇状态，1异常，0正常
*/
@NoArgsConstructor
@Data
public class DeviceRealStatusResp {
    private Integer code;
    private String message;
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        private String code;
        private DeviceStatusDTO deviceStatus;

        @NoArgsConstructor
        @Data
        public static class DeviceStatusDTO {
            private Integer id;
            private Integer deviceid;
            private Integer sysStatusPower;
            private Integer sysStatusSystemrun;
            private Integer sysStatusTemperatureover;
            private Integer sysStatusMagnetic;
            private Integer sysStatusFilllight;
            private Integer sysStatusSwitch;
            private Integer sysLightningRod;
            private Integer sysLeakage;
            private Integer sysShortCircuit;
            private Integer sysOverpressure;
            private Integer sysUndervoltage;
            private Integer sysViolenceOpen;
            private Integer voltage;
            private Integer electricCurrent;
            private Integer temperature;
            private Integer doorstatus;
            private Integer gin1status;
            private Integer filllightstatus;
            private Integer boxarmingstatus;
            private Integer flashlightstatus;
            private Integer heaterstatus;
            private Integer camerastatus;
            private Integer switchstatus;
            private Integer fanstatus;
            private Integer buzzer2status;
            private Integer fwarning;
            private Long createTime;
            private Integer boxopennum;
            private Integer power12v01;
            private Integer power12v02;
            private Integer fiber1;
            private Integer fiber2;
            private Integer fiber3;
            private Integer fiber4;
            private Integer fiber5;
            private Integer fiber6;
            private Integer fiber7;
            private Integer fiber8;
            private Integer ethernet1;
            private Integer ethernet2;
            private Integer ethernet3;
            private Integer ethernet4;
            private Integer ethernet5;
            private Integer ethernet6;
            private Integer ethernet7;
            private Integer ethernet8;
            private Integer sysHumidity;
            private Integer poweroff1;
            private Integer poweroff2;
            private Integer poweroff3;
            private Integer poweroff4;
            private Integer poweroff5;
            private Integer poweroff6;
            private Integer poweroff7;
            private Integer poweroff8;
            private Integer poweroff9;
            private Integer poweroff10;
            private Integer poweroff11;
            private Integer poweroff12;
            private Double windSpeed;
            private String windDirect;
            private Integer temperatureAir;
            private Double shockX;
            private Double shockY;
            private Double shockZ;
            private Double bumpX;
            private Double bumpY;
            private Double bumpZ;
            private Double angleX;
            private Double angleY;
            private Double angleZ;
            private Double electricQuantity;
            private Double lastElectricQuantity;
            private Integer fanSpeed;
            private Integer fanState;
        }
    }
}
