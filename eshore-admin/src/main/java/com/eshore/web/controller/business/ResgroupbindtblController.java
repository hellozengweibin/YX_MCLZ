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
import com.eshore.business.domain.Resgroupbindtbl;
import com.eshore.business.domain.form.ResgroupbindtblForm;
import com.eshore.business.domain.vo.ResgroupbindtblVO;
import com.eshore.business.service.IResgroupbindtblService;
import com.eshore.common.utils.poi.ExcelUtil;
import com.eshore.common.core.page.TableDataInfo;

/**
 * 创建资源组资源绑定Controller
 *
 * @author eshore
 * @date 2023-11-20
 */

@Api(tags = "创建资源组资源绑定模块")
@RestController
@RequestMapping("/business/resgroupbindtbl")
public class ResgroupbindtblController extends BaseController {
    @Autowired
    private IResgroupbindtblService resgroupbindtblService;

    /**
     * 查询创建资源组资源绑定列表
     */
    @ApiOperation(value = "查询创建资源组资源绑定列表")
    @PreAuthorize("@ss.hasPermi('business:resgroupbindtbl:list')")
    @GetMapping("/list")
    public TableDataInfo<ResgroupbindtblVO> list(ResgroupbindtblForm resgroupbindtbl) {
        startPage();
        List<Resgroupbindtbl> list = resgroupbindtblService.selectResgroupbindtblList(resgroupbindtbl);
        return getDataTable(list,ResgroupbindtblVO.class);
    }

    /**
     * 导出创建资源组资源绑定列表
     */
    @ApiOperation(value = "导出创建资源组资源绑定列表")
    @PreAuthorize("@ss.hasPermi('business:resgroupbindtbl:export')")
    @Log(title = "创建资源组资源绑定", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ResgroupbindtblForm resgroupbindtbl) {
        List<Resgroupbindtbl> list = resgroupbindtblService.selectResgroupbindtblList(resgroupbindtbl);
        ExcelUtil<Resgroupbindtbl> util = new ExcelUtil<Resgroupbindtbl>(Resgroupbindtbl. class);
        util.exportExcel(response, list, "创建资源组资源绑定数据");
    }

    /**
     * 获取创建资源组资源绑定详细信息
     */
    @ApiOperation(value = "获取创建资源组资源绑定详细信息")
    @PreAuthorize("@ss.hasPermi('business:resgroupbindtbl:query')")
    @GetMapping(value = "/detail/{id}")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long")
    public CommonResult<ResgroupbindtblVO> getDetail(@PathVariable("id") Long id) {
        return ResponseGenerator.genSuccessResult(BeanUtil.copyProperties(resgroupbindtblService.getById(id),ResgroupbindtblVO.class));
    }

    /**
     * 新增创建资源组资源绑定
     */
    @ApiOperation(value = "新增创建资源组资源绑定")
    @PreAuthorize("@ss.hasPermi('business:resgroupbindtbl:add')")
    @Log(title = "创建资源组资源绑定", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Boolean> add(@RequestBody ResgroupbindtblForm resgroupbindtbl) {
        boolean result = resgroupbindtblService.saveOrUpdate(BeanUtil.copyProperties(resgroupbindtbl,Resgroupbindtbl. class));
        return result ? ResponseGenerator.genSuccessResult() : ResponseGenerator.genFailResult("新增失败");
    }

    /**
     * 修改创建资源组资源绑定
     */
    @ApiOperation(value = "修改创建资源组资源绑定")
    @PreAuthorize("@ss.hasPermi('business:resgroupbindtbl:edit')")
    @Log(title = "创建资源组资源绑定", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Boolean> edit(@RequestBody ResgroupbindtblForm resgroupbindtbl) {
        return ResponseGenerator.genSuccessResult(resgroupbindtblService.saveOrUpdate(BeanUtil.copyProperties(resgroupbindtbl,Resgroupbindtbl. class)));
    }

    /**
     * 删除创建资源组资源绑定
     */
    @ApiOperation(value = "删除创建资源组资源绑定")
    @ApiImplicitParam(name = "ids", value = "ids", dataType = "Long[]")
    @PreAuthorize("@ss.hasPermi('business:resgroupbindtbl:remove')")
    @Log(title = "创建资源组资源绑定", businessType = BusinessType.DELETE)
    @DeleteMapping("/del")
    public CommonResult<Integer> remove(@RequestBody Long[] ids) {
        return ResponseGenerator.genSuccessResult(resgroupbindtblService.deleteResgroupbindtblByIds(ids));
    }
}
