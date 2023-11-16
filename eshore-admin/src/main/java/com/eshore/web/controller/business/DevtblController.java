package com.eshore.web.controller.business;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.bean.BeanUtil;
import com.eshore.common.core.response.CommonResult;
import com.eshore.common.core.response.ResponseGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.eshore.common.annotation.Log;
import com.eshore.common.core.controller.BaseController;
import com.eshore.common.enums.BusinessType;
import com.eshore.business.domain.Devtbl;
import com.eshore.business.domain.form.DevtblForm;
import com.eshore.business.domain.vo.DevtblVO;
import com.eshore.business.service.IDevtblService;
import com.eshore.common.utils.poi.ExcelUtil;
import com.eshore.common.core.page.TableDataInfo;

/**
 * 创建设备Controller
 *
 * @author eshore
 * @date 2023-11-16
 */

@Api(tags = "创建设备模块")
@RestController
@RequestMapping("/business/devtbl")
public class DevtblController extends BaseController {
    @Autowired
    private IDevtblService devtblService;

    /**
     * 查询创建设备列表
     */
    @ApiOperation(value = "查询创建设备列表")
    @PreAuthorize("@ss.hasPermi('business:devtbl:list')")
    @GetMapping("/list")
    public TableDataInfo<DevtblVO> list(DevtblForm devtbl) {
        startPage();
        List<Devtbl> list = devtblService.selectDevtblList(devtbl);
        return getDataTable(list,DevtblVO.class);
    }

    /**
     * 导出创建设备列表
     */
    @ApiOperation(value = "导出创建设备列表")
    @PreAuthorize("@ss.hasPermi('business:devtbl:export')")
    @Log(title = "创建设备", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DevtblForm devtbl) {
        List<Devtbl> list = devtblService.selectDevtblList(devtbl);
        ExcelUtil<Devtbl> util = new ExcelUtil<Devtbl>(Devtbl. class);
        util.exportExcel(response, list, "创建设备数据");
    }

    /**
     * 获取创建设备详细信息
     */
    @ApiOperation(value = "获取创建设备详细信息")
    @PreAuthorize("@ss.hasPermi('business:devtbl:query')")
    @GetMapping(value = "/detail/{id}")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long")
    public CommonResult<DevtblVO> getDetail(@PathVariable("id") Long id) {
        return ResponseGenerator.genSuccessResult(BeanUtil.copyProperties(devtblService.getById(id),DevtblVO.class));
    }

    /**
     * 新增创建设备
     */
    @ApiOperation(value = "新增创建设备")
    @PreAuthorize("@ss.hasPermi('business:devtbl:add')")
    @Log(title = "创建设备", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Boolean> add(@RequestBody DevtblForm devtbl) {
        boolean result = devtblService.saveOrUpdate(BeanUtil.copyProperties(devtbl,Devtbl. class));
        return result ? ResponseGenerator.genSuccessResult() : ResponseGenerator.genFailResult("新增失败");
    }

    /**
     * 修改创建设备
     */
    @ApiOperation(value = "修改创建设备")
    @PreAuthorize("@ss.hasPermi('business:devtbl:edit')")
    @Log(title = "创建设备", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Boolean> edit(@RequestBody DevtblForm devtbl) {
        return ResponseGenerator.genSuccessResult(devtblService.saveOrUpdate(BeanUtil.copyProperties(devtbl,Devtbl. class)));
    }

    /**
     * 删除创建设备
     */
    @ApiOperation(value = "删除创建设备")
    @ApiImplicitParam(name = "ids", value = "ids", dataType = "Long[]")
    @PreAuthorize("@ss.hasPermi('business:devtbl:remove')")
    @Log(title = "创建设备", businessType = BusinessType.DELETE)
    @DeleteMapping("/del")
    public CommonResult<Integer> remove(@RequestBody Long[] ids) {
        return ResponseGenerator.genSuccessResult(devtblService.deleteDevtblByIds(ids));
    }
}
