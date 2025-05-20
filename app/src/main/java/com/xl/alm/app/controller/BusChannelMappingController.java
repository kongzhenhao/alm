package com.xl.alm.app.controller;

import com.jd.lightning.common.annotation.Log;
import com.jd.lightning.common.core.controller.BaseController;
import com.jd.lightning.common.core.domain.Result;
import com.jd.lightning.common.core.page.TableDataInfo;
import com.jd.lightning.common.enums.BusinessType;
import com.xl.alm.app.dto.BusChannelMappingDTO;
import com.xl.alm.app.query.BusChannelMappingQuery;
import com.xl.alm.app.service.BusChannelMappingService;
import com.xl.alm.app.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 渠道码映射配置 Controller
 *
 * @author AI Assistant
 */
@RestController
@RequestMapping("/insu/bus/channel/mapping")
public class BusChannelMappingController extends BaseController {

    @Autowired
    private BusChannelMappingService busChannelMappingService;

    /**
     * 查询渠道码映射配置列表
     */
    @PreAuthorize("@ss.hasPermi('insu:bus:channel:mapping:list')")
    @GetMapping("/list")
    public TableDataInfo list(BusChannelMappingQuery query) {
        startPage();
        List<BusChannelMappingDTO> list = busChannelMappingService.selectBusChannelMappingDtoList(query);
        return getDataTable(list);
    }

    /**
     * 导出渠道码映射配置列表
     */
    @PreAuthorize("@ss.hasPermi('insu:bus:channel:mapping:export')")
    @Log(title = "渠道码映射配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BusChannelMappingQuery query) {
        List<BusChannelMappingDTO> list = busChannelMappingService.selectBusChannelMappingDtoList(query);
        ExcelUtil<BusChannelMappingDTO> util = new ExcelUtil<>(BusChannelMappingDTO.class);
        util.exportExcel(list, "渠道码映射配置数据", response);
    }

    /**
     * 获取渠道码映射配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('insu:bus:channel:mapping:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id) {
        return Result.success(busChannelMappingService.selectBusChannelMappingDtoById(id));
    }

    /**
     * 新增渠道码映射配置
     */
    @PreAuthorize("@ss.hasPermi('insu:bus:channel:mapping:add')")
    @Log(title = "渠道码映射配置", businessType = BusinessType.INSERT)
    @PostMapping
    public Result add(@Valid @RequestBody BusChannelMappingDTO dto) {
        return toAjax(busChannelMappingService.insertBusChannelMappingDto(dto));
    }

    /**
     * 修改渠道码映射配置
     */
    @PreAuthorize("@ss.hasPermi('insu:bus:channel:mapping:edit')")
    @Log(title = "渠道码映射配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result edit(@Valid @RequestBody BusChannelMappingDTO dto) {
        return toAjax(busChannelMappingService.updateBusChannelMappingDto(dto));
    }

    /**
     * 删除渠道码映射配置
     */
    @PreAuthorize("@ss.hasPermi('insu:bus:channel:mapping:remove')")
    @Log(title = "渠道码映射配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids) {
        return toAjax(busChannelMappingService.deleteBusChannelMappingDtoByIds(ids));
    }

    /**
     * 导入渠道码映射配置
     */
    @PreAuthorize("@ss.hasPermi('insu:bus:channel:mapping:import')")
    @Log(title = "渠道码映射配置", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public Result importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<BusChannelMappingDTO> util = new ExcelUtil<>(BusChannelMappingDTO.class);
        List<BusChannelMappingDTO> dtoList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = busChannelMappingService.importBusChannelMappingDto(dtoList, updateSupport, operName);
        return Result.success(message);
    }

    /**
     * 获取渠道码映射配置导入模板
     */
    @PreAuthorize("@ss.hasPermi('insu:bus:channel:mapping:import')")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<BusChannelMappingDTO> util = new ExcelUtil<>(BusChannelMappingDTO.class);
        util.exportTemplateExcel(response, "渠道码映射配置");
    }
}
