package com.xl.alm.app.controller;

import com.jd.lightning.common.annotation.Log;
import com.jd.lightning.common.core.controller.BaseController;
import com.jd.lightning.common.core.domain.Result;
import com.jd.lightning.common.core.page.TableDataInfo;
import com.jd.lightning.common.enums.BusinessType;
import com.xl.alm.app.dto.SampleDTO;
import com.xl.alm.app.query.SampleQuery;
import com.xl.alm.app.service.SampleService;
import com.xl.alm.app.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 折现因子表 Controller
 *
 * @author AI Assistant
 */
@RestController
// 这里xxx是模块名
@RequestMapping("/xxx/sample")
public class SampleController extends BaseController {

    @Autowired
    private SampleService sampleService;

    /**
     * 查询折现因子列表
     */
    @PreAuthorize("@ss.hasPermi('xxx:sample:list')")
    @GetMapping("/list")
    public TableDataInfo list(SampleQuery sampleQuery) {
        startPage();
        List<SampleDTO> list = sampleService.selectSampleDtoList(sampleQuery);
        return getDataTable(list);
    }

    /**
     * 获取折现因子详细信息
     */
    @PreAuthorize("@ss.hasPermi('xxx:sample:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id) {
        return Result.success(sampleService.selectSampleDtoById(id));
    }

    /**
     * 根据条件查询折现因子
     */
    @PreAuthorize("@ss.hasPermi('xxx:sample:query')")
    @GetMapping("/condition")
    public Result getByCondition(
            @RequestParam("accountPeriod") String accountPeriod,
            @RequestParam("factorType") String factorType,
            @RequestParam("durationType") String durationType,
            @RequestParam(value = "designType", required = false) String designType,
            @RequestParam(value = "isShortTerm", required = false) String isShortTerm) {
        return Result.success(sampleService.selectSampleDtoByCondition(
                accountPeriod, factorType, durationType, designType, isShortTerm));
    }

    /**
     * 批量新增折现因子
     */
    @PreAuthorize("@ss.hasPermi('xxx:sample:add')")
    @Log(title = "折现因子", businessType = BusinessType.INSERT)
    @PostMapping("/batchAdd")
    public Result batchAdd(@RequestBody List<SampleDTO> sampleEntityList) {
        return toAjax(sampleService.batchInsertSampleDto(sampleEntityList));
    }

    /**
     * 删除指定账期的折现因子
     */
    @PreAuthorize("@ss.hasPermi('xxx:sample:remove')")
    @Log(title = "折现因子", businessType = BusinessType.DELETE)
    @DeleteMapping("/period/{accountPeriod}")
    public Result removeByPeriod(@PathVariable String accountPeriod) {
        return toAjax(sampleService.deleteSampleDtoByPeriod(accountPeriod));
    }

    /**
     * 删除折现因子
     */
    @PreAuthorize("@ss.hasPermi('xxx:sample:remove')")
    @Log(title = "折现因子", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public Result remove(@PathVariable Long id) {
        return toAjax(sampleService.deleteSampleDtoById(id));
    }

    /**
     * 导入折现因子
     */
    @PreAuthorize("@ss.hasPermi('xxx:sample:import')")
    @Log(title = "折现因子", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public Result importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<SampleDTO> util = new ExcelUtil(SampleDTO.class);
        List<SampleDTO> dtoList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        try {
            String message = sampleService.importSampleDto(dtoList, updateSupport, operName);
            return Result.success(message);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除导出因子
     */
    @PreAuthorize("@ss.hasPermi('dur:liability:export')")
    @Log(title = "折现因子", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public void export(HttpServletResponse response, SampleQuery query) {
        ExcelUtil<SampleDTO> util = new ExcelUtil<>(SampleDTO.class);
        List<SampleDTO> list = sampleService.selectSampleDtoList(query);
        util.exportExcel(list, "折现因子数据", response);
    }

    /**
     * 获取折现因子模板Excel
     */
    @PreAuthorize("@ss.hasPermi('xxx:sample:template:export')")
    @GetMapping("/exportTemplate")
    public void exportTemplate(HttpServletResponse response) {
        ExcelUtil<SampleDTO> util = new ExcelUtil<>(SampleDTO.class);
        util.exportTemplateExcel(response, "折现因子");
    }
}
