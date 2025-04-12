package com.xl.alm.app.controller;

import com.jd.lightning.common.annotation.Log;
import com.jd.lightning.common.core.controller.BaseController;
import com.jd.lightning.common.core.domain.Result;
import com.jd.lightning.common.core.page.TableDataInfo;
import com.jd.lightning.common.enums.BusinessType;
import com.xl.alm.app.entity.SampleEntity;
import com.xl.alm.app.service.SampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 折现因子表 Controller
 *
 * @author AI Assistant
 */
@RestController
@RequestMapping("/sample")
public class SampleController extends BaseController {

    @Autowired
    private SampleService sampleService;

    /**
     * 查询折现因子列表
     */
    @PreAuthorize("@ss.hasPermi('alm:sample:list')")
    @GetMapping("/list")
    public TableDataInfo list(SampleEntity sampleEntity) {
        startPage();
        List<SampleEntity> list = sampleService.selectSampleEntityList(sampleEntity);
        return getDataTable(list);
    }

    /**
     * 获取折现因子详细信息
     */
    @PreAuthorize("@ss.hasPermi('alm:sample:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id) {
        return Result.success(sampleService.selectSampleEntityById(id));
    }

    /**
     * 根据条件查询折现因子
     */
    @PreAuthorize("@ss.hasPermi('alm:sample:query')")
    @GetMapping("/condition")
    public Result getByCondition(
            @RequestParam("accountPeriod") String accountPeriod,
            @RequestParam("factorType") String factorType,
            @RequestParam("durationType") String durationType,
            @RequestParam(value = "designType", required = false) String designType,
            @RequestParam(value = "isShortTerm", required = false) String isShortTerm) {
        return Result.success(sampleService.selectSampleEntityByCondition(
                accountPeriod, factorType, durationType, designType, isShortTerm));
    }

    /**
     * 批量新增折现因子
     */
    @PreAuthorize("@ss.hasPermi('alm:sample:add')")
    @Log(title = "折现因子", businessType = BusinessType.INSERT)
    @PostMapping("/batchAdd")
    public Result batchAdd(@RequestBody List<SampleEntity> sampleEntityList) {
        return toAjax(sampleService.batchInsertSampleEntity(sampleEntityList));
    }

    /**
     * 删除指定账期的折现因子
     */
    @PreAuthorize("@ss.hasPermi('alm:sample:remove')")
    @Log(title = "折现因子", businessType = BusinessType.DELETE)
    @DeleteMapping("/period/{accountPeriod}")
    public Result removeByPeriod(@PathVariable String accountPeriod) {
        return toAjax(sampleService.deleteSampleEntityByPeriod(accountPeriod));
    }

    /**
     * 删除折现因子
     */
    @PreAuthorize("@ss.hasPermi('alm:sample:remove')")
    @Log(title = "折现因子", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public Result remove(@PathVariable Long id) {
        return toAjax(sampleService.deleteSampleEntityById(id));
    }
}
