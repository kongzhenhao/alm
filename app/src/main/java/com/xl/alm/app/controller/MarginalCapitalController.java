package com.xl.alm.app.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.jd.lightning.common.annotation.Log;
import com.jd.lightning.common.core.controller.BaseController;
import com.jd.lightning.common.core.domain.Result;
import com.jd.lightning.common.core.page.TableDataInfo;
import com.jd.lightning.common.enums.BusinessType;
import com.xl.alm.app.dto.ItemDefinitionDTO;
import com.xl.alm.app.dto.MarginalCapitalDTO;
import com.xl.alm.app.dto.MarginalCapitalExportDTO;
import com.xl.alm.app.query.ItemDefinitionQuery;
import com.xl.alm.app.query.MarginalCapitalQuery;
import com.xl.alm.app.service.ItemDefinitionService;
import com.xl.alm.app.service.MarginalCapitalService;
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
 * 边际最低资本贡献率表Controller
 *
 * @author AI Assistant
 */
@Slf4j
@RestController
@RequestMapping("/minc/marginal/capital")
public class MarginalCapitalController extends BaseController {

    @Autowired
    private MarginalCapitalService marginalCapitalService;

    @Autowired
    private ItemDefinitionService itemDefinitionService;

    /**
     * 获取项目定义列表（用于下拉选择）
     */
    @PreAuthorize("@ss.hasPermi('minc:marginal:capital:list')")
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
            if (filteredList.size() > 0) {
                log.info("第一条有效数据示例: itemCode={}, capitalItem={}",
                        filteredList.get(0).getItemCode(), filteredList.get(0).getCapitalItem());
            }
            return Result.success(filteredList);
        } catch (Exception e) {
            log.error("获取项目定义列表失败", e);
            return Result.error("获取项目定义列表失败：" + e.getMessage());
        }
    }

    /**
     * 查询边际最低资本贡献率表列表
     */
    @PreAuthorize("@ss.hasPermi('minc:marginal:capital:list')")
    @GetMapping("/list")
    public TableDataInfo list(MarginalCapitalQuery query) {
        startPage();
        List<MarginalCapitalDTO> list = marginalCapitalService.selectMarginalCapitalDtoList(query);
        return getDataTable(list);
    }

    /**
     * 导出边际最低资本贡献率表列表
     */
    @PreAuthorize("@ss.hasPermi('minc:marginal:capital:export')")
    @Log(title = "边际最低资本贡献率表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MarginalCapitalQuery query) {
        List<MarginalCapitalDTO> list = marginalCapitalService.selectMarginalCapitalDtoList(query);

        // 转换为导出DTO，使用项目名称而不是编码
        List<MarginalCapitalExportDTO> exportList = new ArrayList<>();
        for (MarginalCapitalDTO dto : list) {
            MarginalCapitalExportDTO exportDto = new MarginalCapitalExportDTO();
            exportDto.setAccountingPeriod(dto.getAccountingPeriod());
            exportDto.setItemName(dto.getItemName()); // 使用项目名称
            exportDto.setReinsuAfterAmount(dto.getReinsuAfterAmount());
            exportDto.setSubRiskMarginalFactor(dto.getSubRiskMarginalFactor());
            exportDto.setCompanyMarginalFactor(dto.getCompanyMarginalFactor());
            exportList.add(exportDto);
        }

        ExcelUtil<MarginalCapitalExportDTO> util = new ExcelUtil<>(MarginalCapitalExportDTO.class);
        util.exportExcel(exportList, "边际最低资本贡献率表数据", response);
    }

    /**
     * 获取边际最低资本贡献率表详细信息
     */
    @PreAuthorize("@ss.hasPermi('minc:marginal:capital:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id) {
        return Result.success(marginalCapitalService.selectMarginalCapitalDtoById(id));
    }

    /**
     * 新增边际最低资本贡献率表
     */
    @PreAuthorize("@ss.hasPermi('minc:marginal:capital:add')")
    @Log(title = "边际最低资本贡献率表", businessType = BusinessType.INSERT)
    @PostMapping
    public Result add(@RequestBody MarginalCapitalDTO marginalCapitalDTO) {
        return toAjax(marginalCapitalService.insertMarginalCapitalDto(marginalCapitalDTO));
    }

    /**
     * 修改边际最低资本贡献率表
     */
    @PreAuthorize("@ss.hasPermi('minc:marginal:capital:edit')")
    @Log(title = "边际最低资本贡献率表", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result edit(@RequestBody MarginalCapitalDTO marginalCapitalDTO) {
        return toAjax(marginalCapitalService.updateMarginalCapitalDto(marginalCapitalDTO));
    }

    /**
     * 删除边际最低资本贡献率表
     */
    @PreAuthorize("@ss.hasPermi('minc:marginal:capital:remove')")
    @Log(title = "边际最低资本贡献率表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids) {
        return toAjax(marginalCapitalService.deleteMarginalCapitalDtoByIds(ids));
    }

    /**
     * 获取导入模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        try {
            // 设置响应头
            String fileName = URLEncoder.encode("边际最低资本贡献率表导入模板", "UTF-8").replaceAll("\\+", "%20");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            // 获取项目定义列表，用于生成示例数据
            ItemDefinitionQuery query = new ItemDefinitionQuery();
            List<ItemDefinitionDTO> itemDefinitionList = itemDefinitionService.selectItemDefinitionDtoList(query);

            // 创建模板数据
            List<MarginalCapitalExportDTO> templateList = new ArrayList<>();

            // 添加一个示例行
            MarginalCapitalExportDTO template = new MarginalCapitalExportDTO();
            template.setAccountingPeriod("202312"); // 示例账期
            template.setReinsuAfterAmount(new java.math.BigDecimal("1000000.00")); // 示例再保后金额
            template.setSubRiskMarginalFactor(new java.math.BigDecimal("0.5830")); // 示例子风险边际因子
            template.setCompanyMarginalFactor(new java.math.BigDecimal("0.4520")); // 示例公司边际因子

            // 如果有项目定义数据，使用第一个作为示例
            if (itemDefinitionList.size() > 0) {
                template.setItemName(itemDefinitionList.get(0).getCapitalItem());
            } else {
                template.setItemName("市场风险-利率风险最低资本"); // 默认示例
            }

            templateList.add(template);

            // 使用EasyExcel导出模板，自动调整列宽
            EasyExcel.write(response.getOutputStream(), MarginalCapitalExportDTO.class)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .sheet("边际最低资本贡献率表")
                    .doWrite(templateList);
        } catch (Exception e) {
            log.error("下载导入模板异常", e);
        }
    }

    /**
     * 导入边际最低资本贡献率表数据
     */
    @PreAuthorize("@ss.hasPermi('minc:marginal:capital:import')")
    @Log(title = "边际最低资本贡献率表", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public Result importData(MultipartFile file, boolean updateSupport) {
        try {
            ExcelUtil<MarginalCapitalExportDTO> util = new ExcelUtil<>(MarginalCapitalExportDTO.class);
            List<MarginalCapitalExportDTO> exportDtoList = util.importExcel(file.getInputStream());

            // 获取所有项目定义，用于项目名称到编码的转换
            ItemDefinitionQuery query = new ItemDefinitionQuery();
            List<ItemDefinitionDTO> itemDefinitionList = itemDefinitionService.selectItemDefinitionDtoList(query);

            // 创建项目名称到编码的映射
            Map<String, String> nameToCodeMap = new HashMap<>();
            for (ItemDefinitionDTO item : itemDefinitionList) {
                if (item.getCapitalItem() != null && !item.getCapitalItem().trim().isEmpty()) {
                    nameToCodeMap.put(item.getCapitalItem().trim(), item.getItemCode());
                }
            }

            // 转换为业务DTO
            List<MarginalCapitalDTO> dtoList = new ArrayList<>();
            for (MarginalCapitalExportDTO exportDto : exportDtoList) {
                MarginalCapitalDTO dto = new MarginalCapitalDTO();
                dto.setAccountingPeriod(exportDto.getAccountingPeriod());

                // 根据项目名称查找对应的项目编码
                String itemCode = nameToCodeMap.get(exportDto.getItemName());

                if (itemCode == null) {
                    throw new RuntimeException("找不到项目对应的编码：" + exportDto.getItemName());
                }

                dto.setItemCode(itemCode);
                dto.setReinsuAfterAmount(exportDto.getReinsuAfterAmount());
                dto.setSubRiskMarginalFactor(exportDto.getSubRiskMarginalFactor());
                dto.setCompanyMarginalFactor(exportDto.getCompanyMarginalFactor());
                dtoList.add(dto);
            }

            String operName = getUsername();
            String message = marginalCapitalService.importMarginalCapitalDto(dtoList, updateSupport, operName);
            return Result.success(message);
        } catch (Exception e) {
            log.error("导入Excel异常", e);
            return Result.error("导入Excel失败：" + e.getMessage());
        }
    }
}
