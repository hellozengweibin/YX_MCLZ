package com.eshore.web.controller.system;

import cn.hutool.core.util.ObjectUtil;
import com.eshore.business.utils.MinIOUtil;
import com.eshore.common.config.AesConfig;
import com.eshore.common.core.domain.entity.SysMenu;
import com.eshore.common.core.domain.entity.SysUser;
import com.eshore.common.core.domain.model.LoginBody;
import com.eshore.common.core.domain.model.LoginResponseBody;
import com.eshore.common.core.domain.model.LoginUser;
import com.eshore.common.core.response.CommonResult;
import com.eshore.common.core.response.ResponseGenerator;
import com.eshore.common.enums.PlatformType;
import com.eshore.common.exception.ServiceException;
import com.eshore.common.utils.SecurityUtils;
import com.eshore.common.utils.StringUtils;
import com.eshore.framework.web.service.*;
import com.eshore.system.domain.vo.RouterVo;
import com.eshore.system.service.ISysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 登录验证
 *
 * @author eshore
 */
@RestController
@Api(tags = "用户登录模块")
public class SysLoginController {
    @Autowired
    private AesConfig aesConfig;

    @Value("${login.checkMessageCode:true}")
    private boolean checkMessageCode;

    // @Autowired
    // private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private TokenService tokenService;

    @Resource
    private MinIOUtil minIOUtil;

    public final static HashMap<Integer, ILogin> MAP = new HashMap<>();
    @Resource
    ApplicationContext applicationContext;
    @Value("${shoutServer.url:}")
    private String shoutServerUrl;

    @Value("${shoutServer.salt:13579ab}")
    private String salt;
    @PostConstruct
    public void init() {
        MAP.put(1, applicationContext.getBean(UserNamePasswordLogin.class));
        MAP.put(2, applicationContext.getBean(PhoneCodeLogin.class));
    }

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    @ApiOperation("登录")
    public CommonResult<LoginResponseBody> login(@RequestBody LoginBody loginBody) {
        ILogin iLogin = MAP.get(loginBody.getLoginWay());
        if (ObjectUtil.isEmpty(iLogin)) {
            throw new ServiceException("不支持的登录方式");
        }
        return ResponseGenerator.genSuccessResult(iLogin.login(loginBody));
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    @ApiOperation("获取用户信息")
    public CommonResult<LoginUserInfoVo> getInfo() {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        if (StringUtils.isNotEmpty(user.getAvatar())) {
            user.setAvatar(minIOUtil.getObjectUrl(user.getAvatar()));
        }
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);

        PlatformType currentPlatform = SecurityUtils.getLoginUserCurrentPlatform();
        LoginUserInfoVo loginUserInfoVo = new LoginUserInfoVo(currentPlatform.getCode(), currentPlatform.getName(), user, roles, permissions,shoutServerUrl,salt);

        return ResponseGenerator.genSuccessResult(loginUserInfoVo);
    }

    @GetMapping("/user/getPlatform")
    @ApiOperation("获取用户平台信息")
    public CommonResult<List<Map<String, Object>>> getUserPlatform() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        String platformType = loginUser.getUser().getPlatformType();
        List<Map<String, Object>> list = Arrays.stream(PlatformType.values())
                .filter(r -> {
                    if (SecurityUtils.isAdmin()) return true;
                    if (platformType.contains(String.valueOf(r.getCode()))) return true;
                    return false;
                }).map(item -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("value", item.getCode());
                    map.put("label", item.getName());
                    map.put("isCurrentLoginPlatform", false);
                    if (loginUser.getPlatform() == item.getCode()) {
                        map.put("isCurrentLoginPlatform", true);
                    }
                    map.put("supportSwitch", item.isSupportSwitch());
                    return map;
                }).collect(Collectors.toList());
        return ResponseGenerator.genSuccessResult(list);
    }

    @GetMapping("/switchPlatform/{platformType}")
    @ApiOperation(value = "平台切换")
    public CommonResult<Boolean> switchPlatform(@PathVariable("platformType") Integer platformType) {
        PlatformType type = PlatformType.findByCode(platformType);
        if (ObjectUtil.isEmpty(type)) return ResponseGenerator.genFailResult("平台类型错误");
        PlatformType currentPlatform = SecurityUtils.getLoginUserCurrentPlatform();
        if (currentPlatform.getCode() == platformType) {
            return ResponseGenerator.genFailResult("切换失败，目标平台与当前登录平台一致");
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();
        if (StringUtils.isNotEmpty(user.getPlatformType()) && !user.getPlatformType().contains(String.valueOf(platformType))) {
            return ResponseGenerator.genFailResult("切换失败，目标平台无权限");
        }
        if (StringUtils.isNotNull(loginUser)) {
            // loginUser.setPermissions(permissionService.getMenuPermission(loginUser.getUser()));
            loginUser.setPlatform(platformType);
            user.setLoginPlatformType(platformType);
            tokenService.setLoginUser(loginUser);
        }
        return ResponseGenerator.genSuccessResult();
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    @ApiOperation("获取路由信息")
    public CommonResult<List<RouterVo>> getRouters() {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return ResponseGenerator.genSuccessResult(menuService.buildMenus(menus));
    }


    @ApiModel(value = "LoginRespVo")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginRespVo {
        @ApiModelProperty(value = "token")
        private String token;
    }

    @ApiModel(value = "LoginUserInfoVo")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginUserInfoVo {
        @ApiModelProperty(value = "当前登录的平台类型id")
        private Integer currentPlatform;

        @ApiModelProperty(value = "当前登录的平台类型名称")
        private String platformName;

        @ApiModelProperty(value = "用户信息")
        private SysUser user;

        @ApiModelProperty(value = "角色信息")
        private Set<String> roles;

        @ApiModelProperty(value = "权限信息")
        private Set<String> permissions;

        @ApiModelProperty(value = "喊话服务对接服务连接信息")
        private String shoutServerUrl;
        @ApiModelProperty(value = "喊话服务对接服务加盐值")
        private String salt;

    }
}
