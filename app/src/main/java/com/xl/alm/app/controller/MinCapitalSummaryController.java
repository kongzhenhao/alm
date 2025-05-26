package com.xl.alm.app.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.jd.lightning.common.annotation.Log;
import com.jd.lightning.common.core.controller.BaseController;
import com.jd.lightning.common.core.domain.Result;
import com.jd.lightning.common.core.page.TableDataInfo;
import com.jd.lightning.common.enums.BusinessType;
import com.xl.alm.app.dto.ItemDefinitionDTO;
import com.xl.alm.app.dto.MinCapitalSummaryDTO;
import com.xl.alm.app.dto.MinCapitalSummaryExportDTO;
import com.xl.alm.app.query.ItemDefinitionQuery;
import com.xl.alm.app.query.MinCapitalSummaryQuery;
import com.xl.alm.app.service.ItemDefinitionService;
import com.xl.alm.app.service.MinCapitalSummaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 最低资本明细汇总表Controller
 *
 * @author AI Assistant
 */
@Slf4j
@RestController
@RequestMapping("/minc/min/capital/summary")
public class MinCapitalSummaryController extends BaseController {

    @Autowired
    private MinCapitalSummaryService minCapitalSummaryService;

    @Autowired
    private ItemDefinitionService itemDefinitionService;

    /**
     * 获取项目定义列表（用于下拉选择）
     */
    @PreAuthorize("@ss.hasPermi('minc:min:capital:summary:list')")
    @GetMapping("/itemDefinitions")
    public Result getItemDefinitions() {
        try {
            // 使用空查询条件，获取所有未删除的项目定义
            ItemDefinitionQuery query = new ItemDefinitionQuery();
            List<ItemDefinitionDTO> allList = itemDefinitionService.selectItemDefinitionDtoList(query);

            // 过滤掉capitalItem为空的记录
            List<ItemDefinitionDTO> filteredList = allList.stream()
                    .filter(item -> item.getCapitalItem() != null && !item.getCapitalItem().trim().isEmpty())
                    .collect(java.util.stream.Collectors.toList());

            log.info("获取项目定义列表，总数量: {}, 过滤后数量: {}", allList.size(), filteredList.size());
            return Result.success(filteredList);
        } catch (Exception e) {
            log.error("获取项目定义列表失败", e);
            return Result.error("获取项目定义列表失败：" + e.getMessage());
        }
    }



    /**
     * 查询最低资本明细汇总表列表
     */
    @PreAuthorize("@ss.hasPermi('minc:min:capital:summary:list')")
    @GetMapping("/list")
    public TableDataInfo list(MinCapitalSummaryQuery query) {
        startPage();
        List<MinCapitalSummaryDTO> list = minCapitalSummaryService.selectMinCapitalSummaryDtoList(query);
        return getDataTable(list);
    }

    /**
     * 导出最低资本明细汇总表列表
     */
    @PreAuthorize("@ss.hasPermi('minc:min:capital:summary:export')")
    @Log(title = "最低资本明细汇总表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MinCapitalSummaryQuery query) {
        try {
            List<MinCapitalSummaryDTO> list = minCapitalSummaryService.selectMinCapitalSummaryDtoList(query);

            // 转换为导出DTO，使用项目名称
            List<MinCapitalSummaryExportDTO> exportList = new ArrayList<>();
            for (MinCapitalSummaryDTO dto : list) {
                MinCapitalSummaryExportDTO exportDto = new MinCapitalSummaryExportDTO();
                exportDto.setAccountingPeriod(dto.getAccountingPeriod());
                exportDto.setItemName(dto.getItemName() != null ? dto.getItemName() : dto.getItemCode()); // 使用项目名称
                exportDto.setTraditionalAmount(dto.getTraditionalAmount());
                exportDto.setDividendAmount(dto.getDividendAmount());
                exportDto.setUniversalAmount(dto.getUniversalAmount());
                exportDto.setIndependentAmount(dto.getIndependentAmount());
                exportDto.setCompanyTotalAmount(dto.getCompanyTotalAmount());
                exportList.add(exportDto);
            }

            // 设置响应头
            String fileName = URLEncoder.encode("最低资本明细汇总表", "UTF-8").replaceAll("\\+", "%20");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            // 使用EasyExcel导出，自动调整列宽
            EasyExcel.write(response.getOutputStream(), MinCapitalSummaryExportDTO.class)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .sheet("最低资本明细汇总表")
                    .doWrite(exportList);
        } catch (Exception e) {
            log.error("导出Excel异常", e);
        }
    }

    /**
     * 获取最低资本明细汇总表详细信息
     */
    @PreAuthorize("@ss.hasPermi('minc:min:capital:summary:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id) {
        return Result.success(minCapitalSummaryService.selectMinCapitalSummaryDtoById(id));
    }

    /**
     * 新增最低资本明细汇总表
     */
    @PreAuthorize("@ss.hasPermi('minc:min:capital:summary:add')")
    @Log(title = "最低资本明细汇总表", businessType = BusinessType.INSERT)
    @PostMapping
    public Result add(@RequestBody MinCapitalSummaryDTO minCapitalSummaryDTO) {
        try {
            // 检查是否已存在相同的记录
            MinCapitalSummaryDTO existDto = minCapitalSummaryService.selectValidMinCapitalSummaryDtoByUniqueKey(
                    minCapitalSummaryDTO.getAccountingPeriod(),
                    minCapitalSummaryDTO.getItemCode(),
                    minCapitalSummaryDTO.getAccountCode());

            if (existDto != null) {
                return Result.error("该账期、项目和账户的记录已存在，不能重复添加");
            }

            return toAjax(minCapitalSummaryService.insertMinCapitalSummaryDto(minCapitalSummaryDTO));
        } catch (Exception e) {
            log.error("新增最低资本明细汇总表失败", e);
            return Result.error("新增失败：" + e.getMessage());
        }
    }

    /**
     * 修改最低资本明细汇总表
     */
    @PreAuthorize("@ss.hasPermi('minc:min:capital:summary:edit')")
    @Log(title = "最低资本明细汇总表", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result edit(@RequestBody MinCapitalSummaryDTO minCapitalSummaryDTO) {
        try {
            // 如果修改了唯一键字段，需要检查是否与其他记录冲突
            if (minCapitalSummaryDTO.getId() != null) {
                MinCapitalSummaryDTO existDto = minCapitalSummaryService.selectValidMinCapitalSummaryDtoByUniqueKey(
                        minCapitalSummaryDTO.getAccountingPeriod(),
                        minCapitalSummaryDTO.getItemCode(),
                        minCapitalSummaryDTO.getAccountCode());

                // 如果存在记录且不是当前记录，则说明有冲突
                if (existDto != null && !existDto.getId().equals(minCapitalSummaryDTO.getId())) {
                    return Result.error("该账期、项目和账户的记录已存在，不能重复");
                }
            }

            return toAjax(minCapitalSummaryService.updateMinCapitalSummaryDto(minCapitalSummaryDTO));
        } catch (Exception e) {
            log.error("修改最低资本明细汇总表失败", e);
            return Result.error("修改失败：" + e.getMessage());
        }
    }

    /**
     * 删除最低资本明细汇总表
     */
    @PreAuthorize("@ss.hasPermi('minc:min:capital:summary:remove')")
    @Log(title = "最低资本明细汇总表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids) {
        return toAjax(minCapitalSummaryService.deleteMinCapitalSummaryDtoByIds(ids));
    }
}
