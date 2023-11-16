package com.eshore.framework.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.eshore.common.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author woo
 * @date 2021/3/11
 */
@Slf4j
@Component
public class CreateAndUpdateMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        Object createBy = this.getFieldValByName("createBy", metaObject);
        try {
            if (createBy == null) {
                createBy = SecurityUtils.getUsername();
                this.setFieldValByName("createBy", createBy, metaObject);
            }
        } catch (Exception e) {
            this.setFieldValByName("createBy", "", metaObject);
        }
        Object updateBy = this.getFieldValByName("updateBy", metaObject);
        if (updateBy == null) {
            updateBy = createBy;
            this.setFieldValByName("updateBy", updateBy, metaObject);
        }
        Date date = new Date();
        this.strictInsertFill(metaObject, "createTime", Date.class, date); // 起始版本 3.3.0(推荐使用)
        this.strictInsertFill(metaObject, "updateTime", Date.class, date); // 起始版本 3.3.0(推荐使用)

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Object updateBy = this.getFieldValByName("updateBy", metaObject);
        if (updateBy == null) {
            String createBy = null;
            try {
                createBy = SecurityUtils.getUsername();
            } catch (Exception e) {
                log.info("获取用户账号异常");
                return;
            }
            updateBy = createBy;
            this.setFieldValByName("updateBy", updateBy, metaObject);
        }
        this.strictInsertFill(metaObject, "updateTime", Date.class, new Date()); // 起始版本 3.3.0(推荐使用)
    }


}

