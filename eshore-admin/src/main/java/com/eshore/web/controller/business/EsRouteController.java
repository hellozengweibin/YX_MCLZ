package com.eshore.web.controller.business;

import cn.hutool.core.bean.BeanUtil;
import com.eshore.business.service.IEsRouteService;
import com.eshore.common.annotation.Log;
import com.eshore.common.core.controller.BaseController;
import com.eshore.common.core.page.TableDataInfo;
import com.eshore.common.core.response.CommonResult;
import com.eshore.common.core.response.ResponseGenerator;
import com.eshore.common.enums.BusinessType;
import com.eshore.common.utils.poi.ExcelUtil;
import com.eshore.domain.entity.biz.EsRoute;
import com.eshore.domain.model.form.EsRouteForm;
import com.eshore.domain.model.vo.esRoute.EsRouteVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 通道Controller
 *
 * @author eshore
 * @date 2022-09-13
 */

@Api(tags = "通道模块")
@RestController
@RequestMapping("/business/route")
public class EsRouteController extends BaseController {
    @Resource
    private IEsRouteService esRouteService;

    /**
     * 查询通道列表
     */
    @ApiOperation(value = "查询通道列表")
    @PreAuthorize("@ss.hasPermi('business:route:list')")
    @GetMapping("/list")
    public TableDataInfo<EsRouteVO> list(EsRouteForm esRoute) {
        startPage();
        List<EsRoute> list = esRouteService.selectEsRouteList(esRoute);
        return getDataTable(list, EsRouteVO.class);
    }

    /**
     * 导出通道列表
     */
    @ApiOperation(value = "导出通道列表")
    @PreAuthorize("@ss.hasPermi('business:route:export')")
    @Log(title = "通道", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EsRouteForm esRoute) {
        List<EsRoute> list = esRouteService.selectEsRouteList(esRoute);
        ExcelUtil<EsRoute> util = new ExcelUtil<EsRoute>(EsRoute.class);
        util.exportExcel(response, list, "通道数据");
    }

    /**
     * 获取通道详细信息
     */
    @ApiOperation(value = "获取通道详细信息")
    @PreAuthorize("@ss.hasPermi('business:route:query')")
    @GetMapping(value = "/{id}")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Integer")
    public CommonResult<EsRouteVO> getInfo(@PathVariable("id") Integer id) {
        return ResponseGenerator.genSuccessResult(BeanUtil.copyProperties(esRouteService.getById(id), EsRouteVO.class));
    }

    /**
     * 新增通道
     */
    @ApiOperation(value = "新增通道")
    @PreAuthorize("@ss.hasPermi('business:route:add')")
    @Log(title = "通道", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult add(@RequestBody EsRouteForm esRoute) {
        esRouteService.saveOrUpdate(BeanUtil.copyProperties(esRoute, EsRoute.class));
        return ResponseGenerator.genSuccessResult();
    }

    /**
     * 修改通道
     */
    @ApiOperation(value = "修改通道")
    @PreAuthorize("@ss.hasPermi('business:route:edit')")
    @Log(title = "通道", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult edit(@RequestBody EsRouteForm esRoute) {
        esRouteService.saveOrUpdate(BeanUtil.copyProperties(esRoute, EsRoute.class));
        return ResponseGenerator.genSuccessResult();
    }

    /**
     * 删除通道
     */
    @ApiOperation(value = "删除通道")
    @ApiImplicitParam(name = "ids", value = "ids", dataType = "Integer[]")
    @PreAuthorize("@ss.hasPermi('business:route:remove')")
    @Log(title = "通道", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult remove(@PathVariable Integer[] ids) {
        esRouteService.deleteEsRouteByIds(ids);
        return ResponseGenerator.genSuccessResult();
    }
}
