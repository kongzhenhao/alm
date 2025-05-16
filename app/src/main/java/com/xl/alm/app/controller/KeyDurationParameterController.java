package com.xl.alm.app.controller;

import com.alibaba.excel.EasyExcel;
import com.jd.lightning.common.annotation.Log;
import com.jd.lightning.common.core.controller.BaseController;
import com.jd.lightning.common.core.domain.Result;
import com.jd.lightning.common.core.page.TableDataInfo;
import com.jd.lightning.common.enums.BusinessType;
import com.xl.alm.app.dto.KeyDurationParameterDTO;
import com.xl.alm.app.query.KeyDurationParameterQuery;
import com.xl.alm.app.service.KeyDurationParameterService;
import com.xl.alm.app.util.ValueSetExcelExporter;
import com.xl.alm.app.util.ValueSetExcelImportListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 关键久期参数表 Controller
 *
 * @author AI Assistant
 */
@RestController
@RequestMapping("/dur/key/duration/parameter")
public class KeyDurationParameterController extends BaseController {

    @Autowired
    private KeyDurationParameterService keyDurationParameterService;

    /**
     * 查询关键久期参数列表
     */
    @PreAuthorize("@ss.hasPermi('dur:key:duration:parameter:list')")
    @GetMapping("/list")
    public TableDataInfo list(KeyDurationParameterQuery query) {
        startPage();
        List<KeyDurationParameterDTO> list = keyDurationParameterService.selectKeyDurationParameterDtoList(query);
        return getDataTable(list);
    }

    /**
     * 获取关键久期参数详细信息（不包含参数值集）
     */
    @PreAuthorize("@ss.hasPermi('dur:key:duration:parameter:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id) {
        return Result.success(keyDurationParameterService.selectKeyDurationParameterDtoById(id));
    }

    /**
     * 获取关键久期参数详细信息（包含参数值集）
     */
    @PreAuthorize("@ss.hasPermi('dur:key:duration:parameter:query')")
    @GetMapping(value = "/withValSet/{id}")
    public Result getInfoWithValSet(@PathVariable("id") Long id) {
        return Result.success(keyDurationParameterService.selectKeyDurationParameterDtoWithValSetById(id));
    }

    /**
     * 根据条件查询关键久期参数
     */
    @PreAuthorize("@ss.hasPermi('dur:key:duration:parameter:query')")
    @GetMapping("/condition")
    public Result getByCondition(
            @RequestParam("accountPeriod") String accountPeriod,
            @RequestParam("keyDuration") String keyDuration) {
        return Result.success(keyDurationParameterService.selectKeyDurationParameterDtoByCondition(accountPeriod, keyDuration));
    }

    /**
     * 新增关键久期参数
     */
    @PreAuthorize("@ss.hasPermi('dur:key:duration:parameter:add')")
    @Log(title = "关键久期参数", businessType = BusinessType.INSERT)
    @PostMapping
    public Result add(@RequestBody @Valid KeyDurationParameterDTO dto) {
        return toAjax(keyDurationParameterService.insertKeyDurationParameterDto(dto));
    }

    /**
     * 批量新增关键久期参数
     */
    @PreAuthorize("@ss.hasPermi('dur:key:duration:parameter:add')")
    @Log(title = "关键久期参数", businessType = BusinessType.INSERT)
    @PostMapping("/batchAdd")
    public Result batchAdd(@RequestBody List<KeyDurationParameterDTO> dtoList) {
        return toAjax(keyDurationParameterService.batchInsertKeyDurationParameterDto(dtoList));
    }

    /**
     * 修改关键久期参数
     */
    @PreAuthorize("@ss.hasPermi('dur:key:duration:parameter:edit')")
    @Log(title = "关键久期参数", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result edit(@RequestBody @Valid KeyDurationParameterDTO dto) {
        return toAjax(keyDurationParameterService.updateKeyDurationParameterDto(dto));
    }

    /**
     * 删除关键久期参数
     */
    @PreAuthorize("@ss.hasPermi('dur:key:duration:parameter:remove')")
    @Log(title = "关键久期参数", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids) {
        return toAjax(keyDurationParameterService.deleteKeyDurationParameterDtoByIds(ids));
    }

    /**
     * 删除指定账期的关键久期参数
     */
    @PreAuthorize("@ss.hasPermi('dur:key:duration:parameter:remove')")
    @Log(title = "关键久期参数", businessType = BusinessType.DELETE)
    @DeleteMapping("/period/{accountPeriod}")
    public Result removeByPeriod(@PathVariable String accountPeriod) {
        return toAjax(keyDurationParameterService.deleteKeyDurationParameterDtoByAccountPeriod(accountPeriod));
    }

    /**
     * 导入关键久期参数
     */
    @PreAuthorize("@ss.hasPermi('dur:key:duration:parameter:import')")
    @Log(title = "关键久期参数", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public Result importData(MultipartFile file, boolean updateSupport) throws Exception {
        try {
            // 检查文件是否为空
            if (file == null) {
                return Result.error("请选择要导入的文件");
            }

            // 检查文件类型
            String fileName = file.getOriginalFilename();
            if (fileName == null || !(fileName.endsWith(".xls") || fileName.endsWith(".xlsx"))) {
                return Result.error("文件格式不正确，请上传Excel文件(.xls或.xlsx)");
            }

            // 使用自定义的ValueSetExcelImportListener处理Excel导入
            // 将表头列名从0开始至1272的列转成JSON串赋值到parameterValSet字段中
            ValueSetExcelImportListener<KeyDurationParameterDTO> listener = new ValueSetExcelImportListener<>(KeyDurationParameterDTO.class, "parameterValSet");

            // 读取Excel文件
            EasyExcel.read(file.getInputStream())
                    .registerReadListener(listener)
                    .sheet()
                    .headRowNumber(2) // 设置表头行数为2，确保不跳过表头行
                    .doRead();

            // 获取处理后的数据列表
            List<KeyDurationParameterDTO> dtoList = listener.getResultList();

            // 检查数据列表是否为空
            if (dtoList == null || dtoList.isEmpty()) {
                return Result.error("导入数据为空，请检查Excel文件内容");
            }

            // 检查必填字段和格式
            for (int i = 0; i < dtoList.size(); i++) {
                KeyDurationParameterDTO dto = dtoList.get(i);
                if (dto.getAccountPeriod() == null || dto.getAccountPeriod().trim().isEmpty()) {
                    return Result.error("第" + (i + 1) + "行账期不能为空");
                }
                if (dto.getKeyDuration() == null || dto.getKeyDuration().trim().isEmpty()) {
                    return Result.error("第" + (i + 1) + "行关键期限点不能为空");
                }

                // 检查关键期限点格式
                String keyDuration = dto.getKeyDuration().trim();
                // 如果是数字格式，保留原样；如果不是数字格式，也保留原样，由业务逻辑决定如何处理
                dto.setKeyDuration(keyDuration);
            }

            // 导入数据
            String operName = getUsername();
            String message = keyDurationParameterService.importKeyDurationParameterDto(dtoList, updateSupport, operName);
            return Result.success(message);
        } catch (Exception e) {
            logger.error("导入关键久期参数失败", e);
            return Result.error("导入失败：" + e.getMessage());
        }
    }

    /**
     * 导出关键久期参数
     */
    @PreAuthorize("@ss.hasPermi('dur:key:duration:parameter:export')")
    @Log(title = "关键久期参数", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, KeyDurationParameterQuery query) {
        List<KeyDurationParameterDTO> list = keyDurationParameterService.selectKeyDurationParameterDtoList(query);
        // 使用自定义的ValueSetExcelExporter导出，处理parameterValSet字段
        // 表头全部用中文，编码值通过字典转为中文
        ValueSetExcelExporter.exportExcel(list, "关键久期参数数据", response, "parameterValSet");
    }

    /**
     * 获取关键久期参数模板Excel
     */
    @PreAuthorize("@ss.hasPermi('dur:key:duration:parameter:export')")
    @PostMapping("/exportTemplate")
    public void exportTemplate(HttpServletResponse response) {
        // 使用自定义的ValueSetExcelExporter导出模板，处理parameterValSet字段
        // 序号从0开始，共有1273项，序号为0的列日期显示为上一月最后一天的日期，随序号递增加一个自然月显示日期
        ValueSetExcelExporter.exportTemplateExcel(KeyDurationParameterDTO.class, "关键久期参数模板", response, "parameterValSet");
    }
}
