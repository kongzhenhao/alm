package com.xl.alm.app.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.jd.lightning.common.annotation.Log;
import com.jd.lightning.common.core.controller.BaseController;
import com.jd.lightning.common.core.domain.Result;
import com.jd.lightning.common.core.page.TableDataInfo;
import com.jd.lightning.common.enums.BusinessType;
import com.xl.alm.app.dto.ItemDefinitionDTO;
import com.xl.alm.app.dto.ItemDefinitionExportDTO;
import com.xl.alm.app.query.ItemDefinitionQuery;
import com.xl.alm.app.service.ItemDefinitionService;
import com.xl.alm.app.util.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目定义表 Controller
 *
 * @author AI Assistant
 */
@Slf4j
@RestController
@RequestMapping("/minc/item/definition")
public class ItemDefinitionController extends BaseController {

    @Autowired
    private ItemDefinitionService itemDefinitionService;

    /**
     * 查询项目定义表列表
     */
    @PreAuthorize("@ss.hasPermi('minc:item:definition:list')")
    @GetMapping("/list")
    public TableDataInfo list(ItemDefinitionQuery query) {
        startPage();
        List<ItemDefinitionDTO> list = itemDefinitionService.selectItemDefinitionDtoList(query);
        return getDataTable(list);
    }

    /**
     * 获取项目定义表详细信息
     */
    @PreAuthorize("@ss.hasPermi('minc:item:definition:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id) {
        return Result.success(itemDefinitionService.selectItemDefinitionDtoById(id));
    }

    /**
     * 新增项目定义表
     */
    @PreAuthorize("@ss.hasPermi('minc:item:definition:add')")
    @Log(title = "项目定义表", businessType = BusinessType.INSERT)
    @PostMapping
    public Result add(@RequestBody ItemDefinitionDTO dto) {
        try {
            return toAjax(itemDefinitionService.insertItemDefinitionDto(dto));
        } catch (Exception e) {
            log.error("新增项目定义表失败", e);
            return Result.error("新增失败：" + e.getMessage());
        }
    }

    /**
     * 批量新增项目定义表
     */
    @PreAuthorize("@ss.hasPermi('minc:item:definition:add')")
    @Log(title = "项目定义表", businessType = BusinessType.INSERT)
    @PostMapping("/batchAdd")
    public Result batchAdd(@RequestBody List<ItemDefinitionDTO> dtoList) {
        try {
            return toAjax(itemDefinitionService.batchInsertItemDefinitionDto(dtoList));
        } catch (Exception e) {
            log.error("批量新增项目定义表失败", e);
            return Result.error("批量新增失败：" + e.getMessage());
        }
    }

    /**
     * 修改项目定义表
     */
    @PreAuthorize("@ss.hasPermi('minc:item:definition:edit')")
    @Log(title = "项目定义表", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result edit(@RequestBody ItemDefinitionDTO dto) {
        try {
            return toAjax(itemDefinitionService.updateItemDefinitionDto(dto));
        } catch (Exception e) {
            log.error("修改项目定义表失败", e);
            return Result.error("修改失败：" + e.getMessage());
        }
    }

    /**
     * 删除项目定义表
     */
    @PreAuthorize("@ss.hasPermi('minc:item:definition:remove')")
    @Log(title = "项目定义表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids) {
        try {
            return toAjax(itemDefinitionService.deleteItemDefinitionDtoByIds(ids));
        } catch (Exception e) {
            log.error("删除项目定义表失败", e);
            return Result.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 导出项目定义表
     */
    @PreAuthorize("@ss.hasPermi('minc:item:definition:export')")
    @Log(title = "项目定义表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ItemDefinitionQuery query) {
        try {
            List<ItemDefinitionDTO> list = itemDefinitionService.selectItemDefinitionDtoList(query);
            ExcelUtil<ItemDefinitionDTO> util = new ExcelUtil<>(ItemDefinitionDTO.class);
            util.exportExcel(list, "项目定义表数据", response);
        } catch (Exception e) {
            log.error("导出项目定义表失败", e);
        }
    }

    /**
     * 获取项目定义表导入模板
     */
    @PreAuthorize("@ss.hasPermi('minc:item:definition:import')")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        try {
            // 设置下载文件的名称和类型
            String fileName = URLEncoder.encode("项目定义表模板", "UTF-8").replaceAll("\\+", "%20");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            // 创建一个空的模板数据
            List<ItemDefinitionExportDTO> templateList = new ArrayList<>();

            // 添加一个空行作为模板
            ItemDefinitionExportDTO template = new ItemDefinitionExportDTO();
            template.setStatus("1"); // 默认状态为有效
            templateList.add(template);

            // 使用EasyExcel导出模板，自动调整列宽
            EasyExcel.write(response.getOutputStream(), ItemDefinitionExportDTO.class)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .sheet("项目定义表")
                    .doWrite(templateList);
        } catch (Exception e) {
            log.error("下载项目定义表模板失败", e);
            e.printStackTrace(); // 打印详细错误信息
        }
    }

    /**
     * 导入项目定义表数据
     */
    @PreAuthorize("@ss.hasPermi('minc:item:definition:import')")
    @Log(title = "项目定义表", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public Result importData(MultipartFile file, boolean updateSupport) {
        try {
            ExcelUtil<ItemDefinitionExportDTO> util = new ExcelUtil<>(ItemDefinitionExportDTO.class);
            List<ItemDefinitionExportDTO> exportDtoList = util.importExcel(file.getInputStream());

            // 转换为业务DTO
            List<ItemDefinitionDTO> dtoList = new ArrayList<>();
            for (ItemDefinitionExportDTO exportDto : exportDtoList) {
                ItemDefinitionDTO dto = new ItemDefinitionDTO();
                dto.setItemCode(exportDto.getItemCode());
                dto.setRiskType(exportDto.getRiskType());
                dto.setCapitalItem(exportDto.getCapitalItem());
                dto.setCorrelationItem(exportDto.getCorrelationItem());
                dto.setParentItemCode(exportDto.getParentItemCode());
                dto.setSubRiskFactorFormula(exportDto.getSubRiskFactorFormula());
                dto.setCompanyFactorFormula(exportDto.getCompanyFactorFormula());
                dto.setCapitalCalculationFormula(exportDto.getCapitalCalculationFormula());
                dto.setStatus(exportDto.getStatus());
                dtoList.add(dto);
            }

            String operName = getUsername();
            String message = itemDefinitionService.importItemDefinitionDto(dtoList, updateSupport, operName);
            return Result.success(message);
        } catch (Exception e) {
            log.error("导入Excel异常", e);
            return Result.error("导入Excel失败：" + e.getMessage());
        }
    }
}
