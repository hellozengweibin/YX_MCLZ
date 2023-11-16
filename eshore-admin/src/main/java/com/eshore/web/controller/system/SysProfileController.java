package com.eshore.web.controller.system;

import cn.hutool.core.bean.BeanUtil;
import com.eshore.business.service.common.IMessageService;
import com.eshore.business.utils.MinIOUtil;
import com.eshore.common.annotation.Log;
import com.eshore.common.constant.UserConstants;
import com.eshore.common.core.controller.BaseController;
import com.eshore.common.core.domain.entity.SysUser;
import com.eshore.common.core.domain.model.LoginUser;
import com.eshore.common.core.response.CommonResult;
import com.eshore.common.core.response.ResponseGenerator;
import com.eshore.common.enums.BusinessType;
import com.eshore.common.utils.SecurityUtils;
import com.eshore.common.utils.StringUtils;
import com.eshore.common.utils.minio.demo.service.FileStorageService;
import com.eshore.common.utils.uuid.IdGenerateUtil;
import com.eshore.domain.entity.system.SysUserWorkspaceSetting;
import com.eshore.domain.model.dto.UpdatePasswordDto;
import com.eshore.domain.model.vo.common.MessageSendVo;
import com.eshore.framework.web.service.SysProfileService;
import com.eshore.framework.web.service.TokenService;
import com.eshore.system.service.ISysUserService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 个人信息 业务处理
 *
 * @author eshore
 */
@RestController
@RequestMapping("/system/user/profile")
public class SysProfileController extends BaseController {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private TokenService tokenService;

    @Resource
    private FileStorageService fileStorageService;

    @Autowired
    private IMessageService messageService;

    @Resource
    private MinIOUtil minIOUtil;

    @Resource
    private SysProfileService sysProfileService;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfileResult {
        private SysUser user;
        private String roleGroup;
        private SysUserWorkspaceSetting sysUserWorkspaceSetting;
    }

    /**
     * 个人信息
     */
    @ApiOperation(value = "获取个人信息", notes = "获取个人信息", httpMethod = "GET")
    @GetMapping
    public CommonResult<ProfileResult> profile() {
        LoginUser loginUser = getLoginUser();
        SysUser user = loginUser.getUser();
        String roleGroup = userService.selectUserRoleGroup(loginUser.getUsername());

        if (StringUtils.isNotEmpty(user.getAvatar())) {
            user.setAvatar(minIOUtil.getObjectUrl(user.getAvatar()));
        }

        ProfileResult profileResult = new ProfileResult(user, roleGroup, null);
        return success(profileResult);
    }

    /**
     * 修改用户
     */
    @ApiOperation(value = "修改个人信息", notes = "修改个人信息", httpMethod = "PUT")
    @Log(title = "个人中心-个人信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<?> updateProfile(@RequestBody UpdateUserProfileVo bodyVo) {
        SysUser user = new SysUser();
        BeanUtil.copyProperties(bodyVo, user);
        LoginUser loginUser = getLoginUser();
        SysUser sysUser = loginUser.getUser();
        if (StringUtils.isNotEmpty(user.getPhonenumber())) {
            if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
                return error("保存失败，手机号码已存在");
            }
            if (StringUtils.isNotEmpty(bodyVo.getCode())) {
                // 校验手机验证码
                MessageSendVo messageSendDto = new MessageSendVo();
                messageSendDto.setAccount(sysUser.getUserName());
                messageSendDto.setMessageCode(bodyVo.getCode());
                messageSendDto.setPhoneNum(bodyVo.getPhonenumber());
                messageSendDto.setType(3);
                messageService.validMessageCode(messageSendDto, true);
            }
        }
        if (StringUtils.isNotEmpty(user.getEmail())
                && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            return error("保存失败，邮箱账号已存在");
        }
        user.setUserId(sysUser.getUserId());
        user.setPassword(null);
        if (userService.updateUserProfile(user) > 0) {
            // 更新缓存用户信息
            if (StringUtils.isNotEmpty(user.getNickName())) sysUser.setNickName(user.getNickName());

            if (StringUtils.isNotEmpty(user.getPhonenumber())) sysUser.setPhonenumber(user.getPhonenumber());

            if (StringUtils.isNotEmpty(user.getEmail())) sysUser.setEmail(user.getEmail());

            if (StringUtils.isNotEmpty(user.getSex())) sysUser.setSex(user.getSex());

            tokenService.setLoginUser(loginUser);
            return success();
        }
        return error("修改个人信息异常，请联系管理员");
    }

    /**
     * 重置密码
     */
    @ApiOperation(value = "个人中心-修改密码", notes = "个人中心-修改密码", httpMethod = "POST")
    @Log(title = "个人中心-修改密码", businessType = BusinessType.UPDATE)
    @PutMapping("/updatePwd")
    public CommonResult updatePwd(@RequestBody @Validated UpdatePasswordDto updatePasswordDto) {
        return sysProfileService.updatePwd(updatePasswordDto);
    }

    /**
     * 头像上传
     */
    @ApiOperation(value = "头像上传", notes = "头像上传", httpMethod = "POST")
    @Log(title = "用户头像", businessType = BusinessType.UPDATE)
    @PostMapping("/avatar")
    public CommonResult avatar(@RequestParam("avatarfile") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            LoginUser loginUser = getLoginUser();
            String fileName = IdGenerateUtil.getInstance().nextId(loginUser.getUserId() + "_" + loginUser.getUsername() + "_") + ".jpg";
            String avatarUrl = fileStorageService.uploadImgFile(null, fileName, file.getInputStream());
            logger.info(">>>>>>>>>>>>>user:{} update avatar => fileName:{},uploadUrl:{}", loginUser.getUsername(), fileName, avatarUrl);
            if (userService.updateUserAvatar(loginUser.getUsername(), avatarUrl)) {
                Map<String, Object> ajax = new HashMap<>();
                ajax.put("imgUrl", minIOUtil.getObjectUrl(avatarUrl));
                // 更新缓存用户头像
                loginUser.getUser().setAvatar(avatarUrl);
                tokenService.setLoginUser(loginUser);
                return success(ajax);
            }
        }
        return error("头像上传失败，请联系管理员");
    }

    @ApiOperation(value = "个人中心-告警通知设置", notes = "个人中心-告警通知设置", httpMethod = "POST")
    @Log(title = "个人中心-告警通知设置", businessType = BusinessType.UPDATE)
    @PostMapping("/alarmSetting")
    public CommonResult<SysUserWorkspaceSetting> alarmSetting(@RequestBody @Valid SysUserWorkspaceSetting sysUserWorkspaceSetting) {
        sysUserWorkspaceSetting.setUserId(SecurityUtils.getUserId());
        return ResponseGenerator.genSuccessResult(sysUserWorkspaceSetting);
    }

    @Data
    public static class UpdateUserProfileVo {
        private String nickName;
        private String phonenumber;
        private String email;
        private String code;
        private String sex;
    }
}
