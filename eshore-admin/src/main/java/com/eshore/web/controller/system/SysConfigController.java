package com.eshore.web.controller.system;

import com.eshore.common.annotation.Log;
import com.eshore.common.constant.UserConstants;
import com.eshore.common.core.controller.BaseController;
import com.eshore.common.core.page.TableDataInfo;
import com.eshore.common.core.response.CommonResult;
import com.eshore.common.core.response.ResponseGenerator;
import com.eshore.common.enums.BusinessType;
import com.eshore.common.utils.poi.ExcelUtil;
import com.eshore.system.domain.SysConfig;
import com.eshore.system.service.ISysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 参数配置 信息操作处理
 *
 * @author eshore
 */
@RestController
@RequestMapping("/system/config")
@Api(tags = "参数配置模块")
public class SysConfigController extends BaseController {
    @Autowired
    private ISysConfigService configService;

    /**
     * 获取参数配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    @GetMapping("/list")
    @ApiOperation("获取参数配置列表")
    public TableDataInfo list(SysConfig config) {
        startPage();
        List<SysConfig> list = configService.selectConfigList(config);
        return getDataTable(list);
    }

    @Log(title = "参数管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:config:export')")
    @PostMapping("/export")
    @ApiOperation("参数配置列表导出")
    public void export(HttpServletResponse response, SysConfig config) {
        List<SysConfig> list = configService.selectConfigList(config);
        ExcelUtil<SysConfig> util = new ExcelUtil<SysConfig>(SysConfig.class);
        util.exportExcel(response, list, "参数数据");
    }

    /**
     * 根据参数编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:config:query')")
    @GetMapping(value = "/{configId}")
    @ApiOperation("根据参数编号获取详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "configId", value = "参数编号", dataType = "Long", dataTypeClass = Long.class, required = true)
    })
    public CommonResult<SysConfig> getInfo(@PathVariable Long configId) {
        return ResponseGenerator.genSuccessResult(configService.selectConfigById(configId));
    }

    /**
     * 根据参数键名查询参数值
     */
    @GetMapping(value = "/configKey/{configKey}")
    @ApiOperation("根据参数键名查询参数值")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "configKey", value = "参数键名", dataType = "String", dataTypeClass = String.class, required = true)
    })
    public CommonResult<String> getConfigKey(@PathVariable String configKey) {
        return ResponseGenerator.genSuccessResult(configService.selectConfigByKey(configKey));
    }

    /**
     * 新增参数配置
     */
    @PreAuthorize("@ss.hasPermi('system:config:add')")
    @Log(title = "参数管理", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增参数配置")
    public CommonResult<? extends Object> add(@Validated @RequestBody SysConfig config) {
        if (UserConstants.NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config))) {
            return ResponseGenerator.genFailResult("新增参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        config.setCreateBy(getUsername());
        return ResponseGenerator.genSuccessResult(configService.insertConfig(config));
    }

    /**
     * 修改参数配置
     */
    @PreAuthorize("@ss.hasPermi('system:config:edit')")
    @Log(title = "参数管理", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改参数配置")
    public CommonResult<? extends Object> edit(@Validated @RequestBody SysConfig config) {
        if (UserConstants.NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config))) {
            return ResponseGenerator.genFailResult("修改参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        config.setUpdateBy(getUsername());
        return ResponseGenerator.genSuccessResult(configService.updateConfig(config));
    }

    /**
     * 删除参数配置
     */
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @Log(title = "参数管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{configIds}")
    @ApiOperation("删除参数配置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "configIds", value = "主键id数组", required = true)
    })
    public CommonResult remove(@PathVariable Long[] configIds) {
        configService.deleteConfigByIds(configIds);
        return success();
    }

    /**
     * 刷新参数缓存
     */
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @Log(title = "参数管理", businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    @ApiOperation("刷新参数缓存")
    public CommonResult refreshCache() {
        configService.resetConfigCache();
        return success();
    }
}
