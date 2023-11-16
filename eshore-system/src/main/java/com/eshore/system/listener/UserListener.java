package com.eshore.system.listener;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.eshore.common.constant.Constants;
import com.eshore.common.core.domain.entity.SysUser;
import com.eshore.common.enums.PlatformType;
import com.eshore.common.enums.UserType;
import com.eshore.common.exception.ServiceException;
import com.eshore.common.utils.SecurityUtils;
import com.eshore.common.utils.StringUtils;
import com.eshore.domain.event.UserEvent;
import com.eshore.domain.event.enums.BusinessOptType;
import com.eshore.domain.model.vo.common.CallBackResult;
import com.eshore.system.mapper.SysUserMapper;
import com.eshore.system.service.ISysUserService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * @ClassName CreateUserListener
 * @Description 创建客户账号
 * @Author jianlin.liu
 * @Date 2023/5/12
 * @Version 1.0
 **/
@Slf4j
@Component
public class UserListener {

    @Resource
    private SysUserMapper userMapper;

    @Resource
    private ISysUserService userService;

    // @Async
    @SneakyThrows
    @EventListener(classes = UserEvent.class)
    public void onApplicationEvent(UserEvent event) {
        BusinessOptType optType = event.getOptType();
        UserEvent.UserVo userVo = event.getUserVo();
        if (ObjectUtil.isEmpty(userVo)) {
            log.warn("[addSysUser]==========> userVo is empty!");
            return;
        }
        UserType userType = UserType.findByCode(userVo.getUserType());
        if (ObjectUtil.isEmpty(userType)) {
            throw new ServiceException("创建账号的用户类型不能为空");
        }
        log.info("[UserListener]>>>>>>>>>>>开始[{}][{}]系统账号 => {}", optType.getDesc(), userType.getDesc(), event.getSource());
        CallBackResult<SysUser> result = null;
        switch (optType) {
            case INSERT:
                result = this.addSysUser(userVo, userType.getCode());
                break;
            case UPDATE:
                result = this.updateSysUser(userVo, userType.getCode());
                break;
            case DELETE:
                result = this.deleteUser(userVo);
                break;
            default:
                log.warn("[UserListener]============>不支持的操作类型:{}", optType);
        }
        if (ObjectUtil.isNotEmpty(userVo.getNotify())) {
            userVo.getNotify().call(result);
        }
        log.info("[UserListener]===========>[{}]账号({})[{}]完成 => 结果:{}",
                userType.getDesc(), userVo.getUserName(), optType.getDesc(), result != null ? result.isSuccess() : false);
    }

    /**
     * 新增用户账号
     *
     * @param userVo
     * @return
     */
    private CallBackResult<SysUser> addSysUser(UserEvent.UserVo userVo, String userType) {
        // 参数校验
        boolean valid = this.validateUserVo(BusinessOptType.INSERT, userVo);
        if (!valid) return new CallBackResult<SysUser>(false, null, null);

        int unique = userMapper.checkUserNameUnique(userVo.getUserName());
        if (unique > 0) {
            // 用户名已存在
            return new CallBackResult<SysUser>(false, null, String.format("用户名【%s】已存在", userVo.getUserName()));
        }
        // 平台类型
        String platformTypes = userVo.getPlatformType().stream()
                .filter(item -> PlatformType.findByCode(item) != null)
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        boolean flag = false;
        SysUser sysUser = new SysUser();
        sysUser.setDeptId(0L);
        sysUser.setRoleIds(new Long[] {userVo.getRoleId()});
        sysUser.setRoleId(userVo.getRoleId());
        sysUser.setUserName(userVo.getUserName());
        sysUser.setPassword(SecurityUtils.encryptPassword(userVo.getPassword()));
        sysUser.setNickName(userVo.getName());
        sysUser.setPhonenumber(userVo.getPhone());
        sysUser.setSex("2");
        sysUser.setUserType(userType);
        sysUser.setDelFlag(Constants.NOT_DELETE + "");
        sysUser.setAssociationDataId(userVo.getAssociationId());
        sysUser.setRemark(userVo.getRemark());
        sysUser.setPlatformType(platformTypes);
        sysUser.setCreateTime(new Date());
        sysUser.setCreateBy(SecurityUtils.getUsername());

        int result = userService.insertUser(sysUser);
        if (result > 0) {
            flag = true;
            log.debug("[addUser]=========>add user success!");
        }
        CallBackResult<SysUser> callBackResult = new CallBackResult<>();
        callBackResult.setSuccess(flag);
        callBackResult.setData(sysUser);
        return callBackResult;
    }

    /**
     * 修改客户管理员账号
     *
     * @param userVo
     * @return
     */
    private CallBackResult<SysUser> updateSysUser(UserEvent.UserVo userVo, String userType) {
        // 参数校验
        boolean valid = this.validateUserVo(BusinessOptType.UPDATE, userVo);
        if (!valid) return new CallBackResult<SysUser>(false, null, "关联人员id为空");

        SysUser user = userMapper.selectOne(Wrappers.lambdaQuery(SysUser.class)
                .eq(SysUser::getAssociationDataId, userVo.getAssociationId())
                .eq(SysUser::getDelFlag, "0"));
        if (ObjectUtil.isEmpty(user)) {
            return this.addSysUser(userVo, userType);
        }


        boolean flag = false;
        SysUser sysUser = new SysUser();
        sysUser.setUserId(user.getUserId());
        // 手机号
        if (StringUtils.isNotEmpty(userVo.getPhone())) {
            sysUser.setPhonenumber(userVo.getPhone());
        }
        // 密码
        if (StringUtils.isNotEmpty(userVo.getPassword())) {
            sysUser.setPassword(SecurityUtils.encryptPassword(userVo.getPassword()));
        }
        // 客户名称
        if (StringUtils.isNotEmpty(userVo.getName())) {
            sysUser.setNickName(userVo.getName());
        }
        sysUser.setUpdateTime(new Date());
        sysUser.setUpdateBy(SecurityUtils.getUsername());
        int ok = userService.updateUser(sysUser, false, false);
        if (ok > 0) {
            flag = true;
            log.debug("[updateSysUser]============>update customer admin user success!");
        }
        CallBackResult<SysUser> callBackResult = new CallBackResult<>();
        callBackResult.setSuccess(flag);
        callBackResult.setData(sysUser);
        return callBackResult;
    }

    /**
     * 删除用户
     *
     * @param userVo
     * @return
     */
    private CallBackResult<SysUser> deleteUser(UserEvent.UserVo userVo) {
        // 参数校验
        boolean valid = this.validateUserVo(BusinessOptType.DELETE, userVo);
        if (!valid) return new CallBackResult<SysUser>(false, null, null);

        SysUser user = userMapper.selectOne(Wrappers.lambdaQuery(SysUser.class)
                .select(SysUser::getUserId)
                .eq(SysUser::getAssociationDataId, userVo.getAssociationId())
                .eq(SysUser::getDelFlag, "0"));
        if (ObjectUtil.isEmpty(user)) return new CallBackResult<SysUser>(false, null, null);

        boolean flag = false;
        int i = userService.deleteUserById(user.getUserId());
        if (i > 0) {
            flag = true;
            log.debug("[DeleteUser]==========> delete associationId(id={}) user success!", userVo.getAssociationId());
        }
        CallBackResult<SysUser> callBackResult = new CallBackResult<>();
        callBackResult.setSuccess(flag);
        // callBackResult.setData(sysUser);
        return callBackResult;
    }

    /**
     * 校验参数
     *
     * @param optType
     * @param userVo
     * @return
     */
    private boolean validateUserVo(BusinessOptType optType, UserEvent.UserVo userVo) {
        if (ObjectUtil.isEmpty(userVo.getAssociationId())) {
            log.warn("[validateUserVo]==========> associationId is empty!");
            return false;
        }
        switch (optType) {
            case INSERT:
                if (ObjectUtil.isEmpty(userVo.getPlatformType())) {
                    log.warn("[validateUserVo]==========> platformType is empty!");
                    return false;
                }
                break;
            case UPDATE:
            case DELETE:

                break;
        }
        return true;
    }
}
