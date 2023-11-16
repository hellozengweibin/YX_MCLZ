package com.eshore.domain.event;

import com.eshore.domain.event.enums.BusinessOptType;
import com.eshore.domain.notify.INotify;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * @ClassName BindDeviceEvent
 * @Description
 * @Author jianlin.liu
 * @Date 2023/5/12
 * @Version 1.0
 **/
@Getter
public class UserEvent extends ApplicationEvent {

    /**
     * 业务类型，只支持新增、修改、删除
     */
    private BusinessOptType optType;

    private UserVo userVo;


    public UserEvent(UserVo source, BusinessOptType optType) {
        super(source);
        this.userVo = source;
        this.optType = optType;
    }

    @Data
    @Builder
    public static class UserVo {
        /**
         * 用户id，在编辑，修改时传
         */
        private Long userId;

        /**
         * 用户名
         */
        private String userName;

        /**
         * 姓名
         */
        private String name;

        private String phone;

        /**
         * 密码，不需要加密
         */
        private String password;

        private Long roleId;

        /**
         * 水域id集合
         */
        private Long[] waterAreaIds;

        /**
         * 关联id，如：安全员id
         */
        private Long associationId;

        private String remark;

        private String userType;

        private List<Integer> platformType;

        private String optUser;

        private INotify notify;
    }
}
