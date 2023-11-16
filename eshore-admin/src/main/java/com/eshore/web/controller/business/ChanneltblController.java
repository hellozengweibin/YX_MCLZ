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
import com.eshore.business.domain.Channeltbl;
import com.eshore.business.domain.form.ChanneltblForm;
import com.eshore.business.domain.vo.ChanneltblVO;
import com.eshore.business.service.IChanneltblService;
import com.eshore.common.utils.poi.ExcelUtil;
import com.eshore.common.core.page.TableDataInfo;

/**
 * 创建二级设备Controller
 *
 * @author eshore
 * @date 2023-11-16
 */

@Api(tags = "创建二级设备模块")
@RestController
@RequestMapping("/business/channeltbl")
public class ChanneltblController extends BaseController {
    @Autowired
    private IChanneltblService channeltblService;

    /**
     * 查询创建二级设备列表
     */
    @ApiOperation(value = "查询创建二级设备列表")
    @PreAuthorize("@ss.hasPermi('business:channeltbl:list')")
    @GetMapping("/list")
    public TableDataInfo<ChanneltblVO> list(ChanneltblForm channeltbl) {
        startPage();
        List<Channeltbl> list = channeltblService.selectChanneltblList(channeltbl);
        return getDataTable(list,ChanneltblVO.class);
    }

    /**
     * 导出创建二级设备列表
     */
    @ApiOperation(value = "导出创建二级设备列表")
    @PreAuthorize("@ss.hasPermi('business:channeltbl:export')")
    @Log(title = "创建二级设备", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ChanneltblForm channeltbl) {
        List<Channeltbl> list = channeltblService.selectChanneltblList(channeltbl);
        ExcelUtil<Channeltbl> util = new ExcelUtil<Channeltbl>(Channeltbl. class);
        util.exportExcel(response, list, "创建二级设备数据");
    }

    /**
     * 获取创建二级设备详细信息
     */
    @ApiOperation(value = "获取创建二级设备详细信息")
    @PreAuthorize("@ss.hasPermi('business:channeltbl:query')")
    @GetMapping(value = "/detail/{id}")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long")
    public CommonResult<ChanneltblVO> getDetail(@PathVariable("id") Long id) {
        return ResponseGenerator.genSuccessResult(BeanUtil.copyProperties(channeltblService.getById(id),ChanneltblVO.class));
    }

    /**
     * 新增创建二级设备
     */
    @ApiOperation(value = "新增创建二级设备")
    @PreAuthorize("@ss.hasPermi('business:channeltbl:add')")
    @Log(title = "创建二级设备", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Boolean> add(@RequestBody ChanneltblForm channeltbl) {
        boolean result = channeltblService.saveOrUpdate(BeanUtil.copyProperties(channeltbl,Channeltbl. class));
        return result ? ResponseGenerator.genSuccessResult() : ResponseGenerator.genFailResult("新增失败");
    }

    /**
     * 修改创建二级设备
     */
    @ApiOperation(value = "修改创建二级设备")
    @PreAuthorize("@ss.hasPermi('business:channeltbl:edit')")
    @Log(title = "创建二级设备", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Boolean> edit(@RequestBody ChanneltblForm channeltbl) {
        return ResponseGenerator.genSuccessResult(channeltblService.saveOrUpdate(BeanUtil.copyProperties(channeltbl,Channeltbl. class)));
    }

    /**
     * 删除创建二级设备
     */
    @ApiOperation(value = "删除创建二级设备")
    @ApiImplicitParam(name = "ids", value = "ids", dataType = "Long[]")
    @PreAuthorize("@ss.hasPermi('business:channeltbl:remove')")
    @Log(title = "创建二级设备", businessType = BusinessType.DELETE)
    @DeleteMapping("/del")
    public CommonResult<Integer> remove(@RequestBody Long[] ids) {
        return ResponseGenerator.genSuccessResult(channeltblService.deleteChanneltblByIds(ids));
    }
}
