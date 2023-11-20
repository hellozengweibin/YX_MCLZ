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
import com.eshore.business.domain.Resgroupbindtbl2;
import com.eshore.business.domain.form.Resgroupbindtbl2Form;
import com.eshore.business.domain.vo.Resgroupbindtbl2VO;
import com.eshore.business.service.IResgroupbindtbl2Service;
import com.eshore.common.utils.poi.ExcelUtil;
import com.eshore.common.core.page.TableDataInfo;

/**
 * resgroupbindtbl2Controller
 *
 * @author eshore
 * @date 2023-11-20
 */

@Api(tags = "resgroupbindtbl2模块")
@RestController
@RequestMapping("/business/resgroupbindtbl2")
public class Resgroupbindtbl2Controller extends BaseController {
    @Autowired
    private IResgroupbindtbl2Service resgroupbindtbl2Service;

    /**
     * 查询resgroupbindtbl2列表
     */
    @ApiOperation(value = "查询resgroupbindtbl2列表")
    @PreAuthorize("@ss.hasPermi('business:resgroupbindtbl2:list')")
    @GetMapping("/list")
    public TableDataInfo<Resgroupbindtbl2VO> list(Resgroupbindtbl2Form resgroupbindtbl2) {
        startPage();
        List<Resgroupbindtbl2> list = resgroupbindtbl2Service.selectResgroupbindtbl2List(resgroupbindtbl2);
        return getDataTable(list,Resgroupbindtbl2VO.class);
    }

    /**
     * 导出resgroupbindtbl2列表
     */
    @ApiOperation(value = "导出resgroupbindtbl2列表")
    @PreAuthorize("@ss.hasPermi('business:resgroupbindtbl2:export')")
    @Log(title = "resgroupbindtbl2", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Resgroupbindtbl2Form resgroupbindtbl2) {
        List<Resgroupbindtbl2> list = resgroupbindtbl2Service.selectResgroupbindtbl2List(resgroupbindtbl2);
        ExcelUtil<Resgroupbindtbl2> util = new ExcelUtil<Resgroupbindtbl2>(Resgroupbindtbl2. class);
        util.exportExcel(response, list, "resgroupbindtbl2数据");
    }

    /**
     * 获取resgroupbindtbl2详细信息
     */
    @ApiOperation(value = "获取resgroupbindtbl2详细信息")
    @PreAuthorize("@ss.hasPermi('business:resgroupbindtbl2:query')")
    @GetMapping(value = "/detail/{id}")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long")
    public CommonResult<Resgroupbindtbl2VO> getDetail(@PathVariable("id") Long id) {
        return ResponseGenerator.genSuccessResult(BeanUtil.copyProperties(resgroupbindtbl2Service.getById(id),Resgroupbindtbl2VO.class));
    }

    /**
     * 新增resgroupbindtbl2
     */
    @ApiOperation(value = "新增resgroupbindtbl2")
    @PreAuthorize("@ss.hasPermi('business:resgroupbindtbl2:add')")
    @Log(title = "resgroupbindtbl2", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Boolean> add(@RequestBody Resgroupbindtbl2Form resgroupbindtbl2) {
        boolean result = resgroupbindtbl2Service.saveOrUpdate(BeanUtil.copyProperties(resgroupbindtbl2,Resgroupbindtbl2. class));
        return result ? ResponseGenerator.genSuccessResult() : ResponseGenerator.genFailResult("新增失败");
    }

    /**
     * 修改resgroupbindtbl2
     */
    @ApiOperation(value = "修改resgroupbindtbl2")
    @PreAuthorize("@ss.hasPermi('business:resgroupbindtbl2:edit')")
    @Log(title = "resgroupbindtbl2", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Boolean> edit(@RequestBody Resgroupbindtbl2Form resgroupbindtbl2) {
        return ResponseGenerator.genSuccessResult(resgroupbindtbl2Service.saveOrUpdate(BeanUtil.copyProperties(resgroupbindtbl2,Resgroupbindtbl2. class)));
    }

    /**
     * 删除resgroupbindtbl2
     */
    @ApiOperation(value = "删除resgroupbindtbl2")
    @ApiImplicitParam(name = "ids", value = "ids", dataType = "Long[]")
    @PreAuthorize("@ss.hasPermi('business:resgroupbindtbl2:remove')")
    @Log(title = "resgroupbindtbl2", businessType = BusinessType.DELETE)
    @DeleteMapping("/del")
    public CommonResult<Integer> remove(@RequestBody Long[] ids) {
        return ResponseGenerator.genSuccessResult(resgroupbindtbl2Service.deleteResgroupbindtbl2ByIds(ids));
    }
}
