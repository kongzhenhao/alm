package com.xl.alm.app.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.jd.lightning.common.annotation.Log;
import com.jd.lightning.common.core.controller.BaseController;
import com.jd.lightning.common.core.domain.Result;
import com.jd.lightning.common.core.page.TableDataInfo;
import com.jd.lightning.common.enums.BusinessType;
import com.xl.alm.app.dto.CorrelationCoefDTO;
import com.xl.alm.app.dto.CorrelationCoefExportDTO;
import com.xl.alm.app.dto.ItemDefinitionDTO;
import com.xl.alm.app.query.CorrelationCoefQuery;
import com.xl.alm.app.query.ItemDefinitionQuery;
import com.xl.alm.app.service.CorrelationCoefService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 相关系数表Controller
 *
 * @author AI Assistant
 */
@Slf4j
@RestController
@RequestMapping("/minc/correlation/coef")
public class CorrelationCoefController extends BaseController {

    @Autowired
    private CorrelationCoefService correlationCoefService;

    @Autowired
    private ItemDefinitionService itemDefinitionService;

    /**
     * 获取项目定义列表（用于下拉选择）
     */
    @PreAuthorize("@ss.hasPermi('minc:correlation:coef:list')")
    @GetMapping("/itemDefinitions")
    public Result getItemDefinitions() {
        try {
            // 使用空查询条件，获取所有未删除的项目定义
            ItemDefinitionQuery query = new ItemDefinitionQuery();
            // query的isDel默认值就是0，不需要再设置
            List<ItemDefinitionDTO> allList = itemDefinitionService.selectItemDefinitionDtoList(query);

            // 过滤掉correlationItem为空的记录，并去除连字符
            List<ItemDefinitionDTO> filteredList = allList.stream()
                    .filter(item -> item.getCorrelationItem() != null && !item.getCorrelationItem().trim().isEmpty())
                    .peek(item -> {
                        // 去除correlationItem中的连字符"-"
                        String cleanedItem = item.getCorrelationItem().replace("-", "");
                        item.setCorrelationItem(cleanedItem);
                    })
                    .collect(java.util.stream.Collectors.toList());

            //log.info("获取项目定义列表，总数量: {}, 过滤后数量: {}", allList.size(), filteredList.size());
            if (filteredList.size() > 0) {
                log.info("第一条有效数据示例: itemCode={}, correlationItem={}",
                        filteredList.get(0).getItemCode(), filteredList.get(0).getCorrelationItem());
            }
            return Result.success(filteredList);
        } catch (Exception e) {
            log.error("获取项目定义列表失败", e);
            return Result.error("获取项目定义列表失败：" + e.getMessage());
        }
    }

    /**
     * 查询相关系数表列表
     */
    @PreAuthorize("@ss.hasPermi('minc:correlation:coef:list')")
    @GetMapping("/list")
    public TableDataInfo list(CorrelationCoefQuery query) {
        startPage();
        List<CorrelationCoefDTO> list = correlationCoefService.selectCorrelationCoefDtoList(query);
        return getDataTable(list);
    }

    /**
     * 导出相关系数表列表
     */
    @PreAuthorize("@ss.hasPermi('minc:correlation:coef:export')")
    @Log(title = "相关系数表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CorrelationCoefQuery query) {
        List<CorrelationCoefDTO> list = correlationCoefService.selectCorrelationCoefDtoList(query);

        // 转换为导出DTO，使用项目名称而不是编码
        List<CorrelationCoefExportDTO> exportList = new ArrayList<>();
        for (CorrelationCoefDTO dto : list) {
            CorrelationCoefExportDTO exportDto = new CorrelationCoefExportDTO();
            exportDto.setAccountingPeriod(dto.getAccountingPeriod());
            exportDto.setItemNameX(dto.getItemNameX()); // 使用项目名称
            exportDto.setItemNameY(dto.getItemNameY()); // 使用项目名称
            exportDto.setCorrelationValue(dto.getCorrelationValue());
            exportDto.setStatus("1"); // 默认状态为有效
            exportList.add(exportDto);
        }

        ExcelUtil<CorrelationCoefExportDTO> util = new ExcelUtil<>(CorrelationCoefExportDTO.class);
        util.exportExcel(exportList, "相关系数表数据", response);
    }

    /**
     * 获取相关系数表详细信息
     */
    @PreAuthorize("@ss.hasPermi('minc:correlation:coef:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id) {
        return Result.success(correlationCoefService.selectCorrelationCoefDtoById(id));
    }

    /**
     * 新增相关系数表
     */
    @PreAuthorize("@ss.hasPermi('minc:correlation:coef:add')")
    @Log(title = "相关系数表", businessType = BusinessType.INSERT)
    @PostMapping
    public Result add(@RequestBody CorrelationCoefDTO correlationCoefDTO) {
        return toAjax(correlationCoefService.insertCorrelationCoefDto(correlationCoefDTO));
    }

    /**
     * 修改相关系数表
     */
    @PreAuthorize("@ss.hasPermi('minc:correlation:coef:edit')")
    @Log(title = "相关系数表", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result edit(@RequestBody CorrelationCoefDTO correlationCoefDTO) {
        return toAjax(correlationCoefService.updateCorrelationCoefDto(correlationCoefDTO));
    }

    /**
     * 删除相关系数表
     */
    @PreAuthorize("@ss.hasPermi('minc:correlation:coef:remove')")
    @Log(title = "相关系数表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids) {
        return toAjax(correlationCoefService.deleteCorrelationCoefDtoByIds(ids));
    }

    /**
     * 获取导入模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        try {
            // 设置响应头
            String fileName = URLEncoder.encode("相关系数表导入模板", "UTF-8").replaceAll("\\+", "%20");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            // 获取项目定义列表，用于生成示例数据
            ItemDefinitionQuery query = new ItemDefinitionQuery();
            List<ItemDefinitionDTO> itemDefinitionList = itemDefinitionService.selectItemDefinitionDtoList(query);

            // 创建模板数据
            List<CorrelationCoefExportDTO> templateList = new ArrayList<>();

            // 添加一个示例行
            CorrelationCoefExportDTO template = new CorrelationCoefExportDTO();
            template.setAccountingPeriod("202312"); // 示例账期
            template.setStatus("1"); // 默认状态为有效
            template.setCorrelationValue(new java.math.BigDecimal("0.85")); // 示例相关系数

            // 如果有项目定义数据，使用前两个作为示例
            if (itemDefinitionList.size() >= 2) {
                template.setItemNameX(itemDefinitionList.get(0).getCorrelationItem());
                template.setItemNameY(itemDefinitionList.get(1).getCorrelationItem());
            } else {
                template.setItemNameX("利率风险"); // 默认示例
                template.setItemNameY("违约风险"); // 默认示例
            }

            templateList.add(template);

            // 使用EasyExcel导出模板，自动调整列宽
            EasyExcel.write(response.getOutputStream(), CorrelationCoefExportDTO.class)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .sheet("相关系数表")
                    .doWrite(templateList);
        } catch (Exception e) {
            log.error("下载导入模板异常", e);
        }
    }

    /**
     * 导入相关系数表数据
     */
    @PreAuthorize("@ss.hasPermi('minc:correlation:coef:import')")
    @Log(title = "相关系数表", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public Result importData(MultipartFile file, boolean updateSupport) {
        try {
            ExcelUtil<CorrelationCoefExportDTO> util = new ExcelUtil<>(CorrelationCoefExportDTO.class);
            List<CorrelationCoefExportDTO> exportDtoList = util.importExcel(file.getInputStream());

            // 获取所有项目定义，用于项目名称到编码的转换
            ItemDefinitionQuery query = new ItemDefinitionQuery();
            List<ItemDefinitionDTO> itemDefinitionList = itemDefinitionService.selectItemDefinitionDtoList(query);

            // 创建项目名称到编码的映射
            Map<String, String> nameToCodeMap = new HashMap<>();
            for (ItemDefinitionDTO item : itemDefinitionList) {
                if (item.getCorrelationItem() != null && !item.getCorrelationItem().trim().isEmpty()) {
                    nameToCodeMap.put(item.getCorrelationItem().trim(), item.getItemCode());
                }
            }

            // 转换为业务DTO
            List<CorrelationCoefDTO> dtoList = new ArrayList<>();
            for (CorrelationCoefExportDTO exportDto : exportDtoList) {
                CorrelationCoefDTO dto = new CorrelationCoefDTO();
                dto.setAccountingPeriod(exportDto.getAccountingPeriod());

                // 根据项目名称查找对应的项目编码
                String itemCodeX = nameToCodeMap.get(exportDto.getItemNameX());
                String itemCodeY = nameToCodeMap.get(exportDto.getItemNameY());

                if (itemCodeX == null) {
                    throw new RuntimeException("找不到项目X对应的编码：" + exportDto.getItemNameX());
                }
                if (itemCodeY == null) {
                    throw new RuntimeException("找不到项目Y对应的编码：" + exportDto.getItemNameY());
                }

                dto.setItemCodeX(itemCodeX);
                dto.setItemCodeY(itemCodeY);
                dto.setCorrelationValue(exportDto.getCorrelationValue());
                // 不设置status字段，因为数据库表中没有这个字段
                dtoList.add(dto);
            }

            String operName = getUsername();
            String message = correlationCoefService.importCorrelationCoefDto(dtoList, updateSupport, operName);
            return Result.success(message);
        } catch (Exception e) {
            log.error("导入Excel异常", e);
            return Result.error("导入Excel失败：" + e.getMessage());
        }
    }
}
