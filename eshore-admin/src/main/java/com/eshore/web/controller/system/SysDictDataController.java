package com.eshore.web.controller.system;

import com.eshore.common.annotation.Log;
import com.eshore.common.core.controller.BaseController;
import com.eshore.common.core.domain.entity.SysDictData;
import com.eshore.common.core.page.TableDataInfo;
import com.eshore.common.core.response.CommonResult;
import com.eshore.common.enums.BusinessType;
import com.eshore.common.utils.StringUtils;
import com.eshore.common.utils.poi.ExcelUtil;
import com.eshore.system.service.ISysDictDataService;
import com.eshore.system.service.ISysDictTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据字典信息
 *
 * @author eshore
 */
@RestController
@RequestMapping("/system/dict/data")
@Api(tags = "数据字典信息")
public class SysDictDataController extends BaseController {
    @Autowired
    private ISysDictDataService dictDataService;

    @Autowired
    private ISysDictTypeService dictTypeService;

    @PreAuthorize("@ss.hasPermi('system:dict:list')")
    @GetMapping("/list")
    @ApiOperation("获取数据字典列表")
    public TableDataInfo list(SysDictData dictData) {
        startPage();
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        return getDataTable(list);
    }

    @Log(title = "字典数据", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:dict:export')")
    @PostMapping("/export")
    @ApiOperation("导出数据字典信息")
    public void export(HttpServletResponse response, SysDictData dictData) {
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        ExcelUtil<SysDictData> util = new ExcelUtil<SysDictData>(SysDictData.class);
        util.exportExcel(response, list, "字典数据");
    }

    /**
     * 查询字典数据详细
     */
    @PreAuthorize("@ss.hasPermi('system:dict:query')")
    @GetMapping(value = "/{dictCode}")
    @ApiOperation("查询字典数据详细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dictCode", value = "字典编码", dataType = "Long", dataTypeClass = Long.class, required = true)
    })
    public CommonResult<?> getInfo(@PathVariable Long dictCode) {
        return success(dictDataService.selectDictDataById(dictCode));
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @GetMapping(value = "/type/{dictType}")
    @ApiOperation("根据字典类型查询字典数据信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dictType", value = "字典编码", dataType = "String", dataTypeClass = String.class, required = true)
    })
    public CommonResult<?> dictType(@PathVariable String dictType) {
        List<SysDictData> data = dictTypeService.selectDictDataByType(dictType);
        if (StringUtils.isNull(data)) {
            data = new ArrayList<SysDictData>();
        }
        return success(data);
    }

    /**
     * 新增字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:add')")
    @Log(title = "字典数据", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增字典类型")
    public CommonResult<?> add(@Validated @RequestBody SysDictData dict) {
        dict.setCreateBy(getUsername());
        return toResult(dictDataService.insertDictData(dict));
    }

    /**
     * 修改保存字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:edit')")
    @Log(title = "字典数据", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改保存字典类型")
    public CommonResult<?> edit(@Validated @RequestBody SysDictData dict) {
        dict.setUpdateBy(getUsername());
        return toResult(dictDataService.updateDictData(dict));
    }

    /**
     * 删除字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dictCodes}")
    @ApiOperation("根据字典类型查询字典数据信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dictCodes", value = "字典编码数组", required = true)
    })
    public CommonResult<?> remove(@PathVariable Long[] dictCodes) {
        dictDataService.deleteDictDataByIds(dictCodes);
        return success();
    }
}
