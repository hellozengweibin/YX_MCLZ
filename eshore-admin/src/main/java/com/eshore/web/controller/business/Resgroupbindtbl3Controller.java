package com.eshore.web.controller.business;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.bean.BeanUtil;
import com.eshore.common.annotation.Encrypt;
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
import com.eshore.business.domain.Resgroupbindtbl3;
import com.eshore.business.domain.form.Resgroupbindtbl3Form;
import com.eshore.business.domain.vo.Resgroupbindtbl3VO;
import com.eshore.business.service.IResgroupbindtbl3Service;
import com.eshore.common.utils.poi.ExcelUtil;
import com.eshore.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * resgroupbindtbl3Controller
 *
 * @author eshore
 * @date 2023-11-20
 */

@Api(tags = "resgroupbindtbl3模块")
@RestController
@RequestMapping("/business/resgroupbindtbl3")
public class Resgroupbindtbl3Controller extends BaseController {
    @Autowired
    private IResgroupbindtbl3Service resgroupbindtbl3Service;

    /**
     * 查询resgroupbindtbl3列表
     */
    @ApiOperation(value = "查询resgroupbindtbl3列表")
    @PreAuthorize("@ss.hasPermi('business:resgroupbindtbl3:list')")
    @GetMapping("/list")
    public TableDataInfo<Resgroupbindtbl3VO> list(Resgroupbindtbl3Form resgroupbindtbl3) {
        startPage();
        List<Resgroupbindtbl3> list = resgroupbindtbl3Service.selectResgroupbindtbl3List(resgroupbindtbl3);
        return getDataTable(list,Resgroupbindtbl3VO.class);
    }

    /**
     * 导出resgroupbindtbl3列表
     */
    @ApiOperation(value = "导出resgroupbindtbl3列表")
    @PreAuthorize("@ss.hasPermi('business:resgroupbindtbl3:export')")
    @Log(title = "resgroupbindtbl3", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Resgroupbindtbl3Form resgroupbindtbl3) {
        List<Resgroupbindtbl3> list = resgroupbindtbl3Service.selectResgroupbindtbl3List(resgroupbindtbl3);
        ExcelUtil<Resgroupbindtbl3> util = new ExcelUtil<Resgroupbindtbl3>(Resgroupbindtbl3. class);
        util.exportExcel(response, list, "resgroupbindtbl3数据");
    }

    /**
     * 获取resgroupbindtbl3详细信息
     */
    @ApiOperation(value = "获取resgroupbindtbl3详细信息")
    @PreAuthorize("@ss.hasPermi('business:resgroupbindtbl3:query')")
    @GetMapping(value = "/detail/{id}")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long")
    public CommonResult<Resgroupbindtbl3VO> getDetail(@PathVariable("id") Long id) {
        return ResponseGenerator.genSuccessResult(BeanUtil.copyProperties(resgroupbindtbl3Service.getById(id),Resgroupbindtbl3VO.class));
    }

    /**
     * 新增resgroupbindtbl3
     */
    @ApiOperation(value = "新增resgroupbindtbl3")
    @PreAuthorize("@ss.hasPermi('business:resgroupbindtbl3:add')")
    @Log(title = "resgroupbindtbl3", businessType = BusinessType.INSERT)
    @PostMapping
    public int add(@RequestBody Resgroupbindtbl3 resgroupbindtbl3) {
//    public CommonResult<Boolean> add(@RequestBody Resgroupbindtbl3Form resgroupbindtbl3) {
        int i = resgroupbindtbl3Service.insertResgroupbindtbl3(resgroupbindtbl3);
        return i;
    }

    /**
     * 修改resgroupbindtbl3
     */
    @ApiOperation(value = "修改resgroupbindtbl3")
    @PreAuthorize("@ss.hasPermi('business:resgroupbindtbl3:edit')")
    @Log(title = "resgroupbindtbl3", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Boolean> edit(@RequestBody Resgroupbindtbl3Form resgroupbindtbl3) {
        return ResponseGenerator.genSuccessResult(resgroupbindtbl3Service.saveOrUpdate(BeanUtil.copyProperties(resgroupbindtbl3,Resgroupbindtbl3. class)));
    }

    /**
     * 删除resgroupbindtbl3
     */
    @ApiOperation(value = "删除resgroupbindtbl3")
    @ApiImplicitParam(name = "ids", value = "ids", dataType = "Long[]")
    @PreAuthorize("@ss.hasPermi('business:resgroupbindtbl3:remove')")
    @Log(title = "resgroupbindtbl3", businessType = BusinessType.DELETE)
    @DeleteMapping("/del")
    public CommonResult<Integer> remove(@RequestBody Long[] ids) {
        return ResponseGenerator.genSuccessResult(resgroupbindtbl3Service.deleteResgroupbindtbl3ByIds(ids));
    }


    @Encrypt(isEncypt = false)
    @Log(title = "工单导入", businessType = BusinessType.IMPORT)
    @PostMapping("/import")
    public CommonResult importData(MultipartFile file) throws Exception {
        return toResult(resgroupbindtbl3Service.importData(file));

    }
}
