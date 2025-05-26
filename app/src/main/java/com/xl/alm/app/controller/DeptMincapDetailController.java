package com.xl.alm.app.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.jd.lightning.common.annotation.Log;
import com.jd.lightning.common.core.controller.BaseController;
import com.jd.lightning.common.core.domain.Result;
import com.jd.lightning.common.core.page.TableDataInfo;
import com.jd.lightning.common.enums.BusinessType;
import com.jd.lightning.common.utils.StringUtils;
import com.xl.alm.app.dto.DeptMincapDetailDTO;
import com.xl.alm.app.dto.DeptMincapDetailExportDTO;
import com.xl.alm.app.query.DeptMincapDetailQuery;
import com.xl.alm.app.service.DeptMincapDetailService;
import com.xl.alm.app.util.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 分部门最低资本明细表 Controller
 *
 * @author AI Assistant
 */
@Slf4j
@RestController
@RequestMapping("/minc/dept/mincap/detail")
public class DeptMincapDetailController extends BaseController {

    @Autowired
    private DeptMincapDetailService deptMincapDetailService;

    /**
     * 查询分部门最低资本明细列表
     */
    @PreAuthorize("@ss.hasPermi('minc:dept:mincap:detail:list')")
    @GetMapping("/list")
    public TableDataInfo list(DeptMincapDetailQuery query) {
        // 如果提供了详细查询条件（账期+部门+项目编码），则不进行分页，返回所有匹配记录
        if (StringUtils.isNotEmpty(query.getAccountingPeriod()) &&
            StringUtils.isNotEmpty(query.getDepartment()) &&
            StringUtils.isNotEmpty(query.getItemCode())) {
            List<DeptMincapDetailDTO> list = deptMincapDetailService.selectDeptMincapDetailDtoList(query);
            return getDataTable(list);
        } else {
            // 否则进行分页查询
            startPage();
            List<DeptMincapDetailDTO> list = deptMincapDetailService.selectDeptMincapDetailDtoList(query);
            return getDataTable(list);
        }
    }

    /**
     * 获取分部门最低资本明细详细信息
     */
    @PreAuthorize("@ss.hasPermi('minc:dept:mincap:detail:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id) {
        return Result.success(deptMincapDetailService.selectDeptMincapDetailDtoById(id));
    }

    /**
     * 新增分部门最低资本明细
     */
    @PreAuthorize("@ss.hasPermi('minc:dept:mincap:detail:add')")
    @Log(title = "分部门最低资本明细", businessType = BusinessType.INSERT)
    @PostMapping
    public Result add(@RequestBody DeptMincapDetailDTO dto) {
        try {
            // 准备批量提交的数据
            List<DeptMincapDetailDTO> batchData = new ArrayList<>();

            // 账户编码映射
            Map<String, String> accountCodeMap = new HashMap<>();
            accountCodeMap.put("traditionalAmount", "AC001"); // 传统保险账户
            accountCodeMap.put("dividendAmount", "AC002");    // 分红保险账户
            accountCodeMap.put("universalAmount", "AC003");   // 万能保险账户
            accountCodeMap.put("independentAmount", "AC004"); // 独立账户
            accountCodeMap.put("companyTotalAmount", "AC005"); // 资本补充债账户

            // 处理各账户类型的数据
            for (Map.Entry<String, String> entry : accountCodeMap.entrySet()) {
                String fieldName = entry.getKey();
                String accountCode = entry.getValue();

                // 获取字段值
                BigDecimal amount = null;
                try {
                    if ("traditionalAmount".equals(fieldName)) {
                        amount = dto.getTraditionalAmount();
                    } else if ("dividendAmount".equals(fieldName)) {
                        amount = dto.getDividendAmount();
                    } else if ("universalAmount".equals(fieldName)) {
                        amount = dto.getUniversalAmount();
                    } else if ("independentAmount".equals(fieldName)) {
                        amount = dto.getIndependentAmount();
                    } else if ("companyTotalAmount".equals(fieldName)) {
                        amount = dto.getCompanyTotalAmount();
                    }
                } catch (Exception e) {
                    log.error("获取字段值失败：" + fieldName, e);
                    continue;
                }

                // 如果金额为空，则设置为0
                if (amount == null) {
                    amount = BigDecimal.ZERO;
                }

                // 创建新的DTO对象
                DeptMincapDetailDTO newDto = new DeptMincapDetailDTO();
                newDto.setAccountingPeriod(dto.getAccountingPeriod());
                newDto.setDepartment(dto.getDepartment());
                newDto.setItemCode(dto.getItemCode());
                newDto.setItemName(dto.getItemName());
                newDto.setAccountCode(accountCode);
                newDto.setAmount(amount);
                newDto.setCreateBy(getUsername());
                newDto.setUpdateBy(getUsername());

                batchData.add(newDto);
            }

            // 查询是否已存在相同条件的记录
            DeptMincapDetailQuery query = new DeptMincapDetailQuery();
            query.setAccountingPeriod(dto.getAccountingPeriod());
            query.setDepartment(dto.getDepartment());
            query.setItemCode(dto.getItemCode());

            List<DeptMincapDetailDTO> existingList = deptMincapDetailService.selectDeptMincapDetailDtoList(query);
            if (!existingList.isEmpty()) {
                // 如果已存在记录，则返回错误
                return Result.error("已存在相同条件的记录，请勿重复添加");
            }

            // 批量添加
            return toAjax(deptMincapDetailService.batchInsertDeptMincapDetailDto(batchData));
        } catch (Exception e) {
            log.error("新增分部门最低资本明细失败", e);
            return Result.error("新增失败：" + e.getMessage());
        }
    }

    /**
     * 批量新增分部门最低资本明细
     */
    @PreAuthorize("@ss.hasPermi('minc:dept:mincap:detail:add')")
    @Log(title = "分部门最低资本明细", businessType = BusinessType.INSERT)
    @PostMapping("/batchAdd")
    public Result batchAdd(@RequestBody List<DeptMincapDetailDTO> dtoList) {
        try {
            return toAjax(deptMincapDetailService.batchInsertDeptMincapDetailDto(dtoList));
        } catch (Exception e) {
            log.error("批量新增分部门最低资本明细失败", e);
            return Result.error("批量新增失败：" + e.getMessage());
        }
    }

    /**
     * 修改分部门最低资本明细
     */
    @PreAuthorize("@ss.hasPermi('minc:dept:mincap:detail:edit')")
    @Log(title = "分部门最低资本明细", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result edit(@RequestBody DeptMincapDetailDTO dto) {
        try {
            // 准备批量提交的数据
            List<DeptMincapDetailDTO> batchData = new ArrayList<>();

            // 账户编码映射
            Map<String, String> accountCodeMap = new HashMap<>();
            accountCodeMap.put("traditionalAmount", "AC001"); // 传统保险账户
            accountCodeMap.put("dividendAmount", "AC002");    // 分红保险账户
            accountCodeMap.put("universalAmount", "AC003");   // 万能保险账户
            accountCodeMap.put("independentAmount", "AC004"); // 独立账户
            accountCodeMap.put("companyTotalAmount", "AC005"); // 资本补充债账户

            // 处理各账户类型的数据
            for (Map.Entry<String, String> entry : accountCodeMap.entrySet()) {
                String fieldName = entry.getKey();
                String accountCode = entry.getValue();

                // 获取字段值
                BigDecimal amount = null;
                try {
                    if ("traditionalAmount".equals(fieldName)) {
                        amount = dto.getTraditionalAmount();
                    } else if ("dividendAmount".equals(fieldName)) {
                        amount = dto.getDividendAmount();
                    } else if ("universalAmount".equals(fieldName)) {
                        amount = dto.getUniversalAmount();
                    } else if ("independentAmount".equals(fieldName)) {
                        amount = dto.getIndependentAmount();
                    } else if ("companyTotalAmount".equals(fieldName)) {
                        amount = dto.getCompanyTotalAmount();
                    }
                } catch (Exception e) {
                    log.error("获取字段值失败：" + fieldName, e);
                    continue;
                }

                // 如果金额为空，则设置为0
                if (amount == null) {
                    amount = BigDecimal.ZERO;
                }

                // 创建新的DTO对象
                DeptMincapDetailDTO newDto = new DeptMincapDetailDTO();
                newDto.setAccountingPeriod(dto.getAccountingPeriod());
                newDto.setDepartment(dto.getDepartment());
                newDto.setItemCode(dto.getItemCode());
                newDto.setItemName(dto.getItemName());
                newDto.setAccountCode(accountCode);
                newDto.setAmount(amount);
                newDto.setUpdateBy(getUsername());

                batchData.add(newDto);
            }

            // 使用Service层的事务方法进行更新
            return toAjax(deptMincapDetailService.updateBatchDeptMincapDetailDto(dto.getAccountingPeriod(),
                                                                                dto.getDepartment(),
                                                                                dto.getItemCode(),
                                                                                batchData));
        } catch (Exception e) {
            log.error("修改分部门最低资本明细失败", e);
            return Result.error("修改失败：" + e.getMessage());
        }
    }

    /**
     * 删除分部门最低资本明细
     */
    @PreAuthorize("@ss.hasPermi('minc:dept:mincap:detail:remove')")
    @Log(title = "分部门最低资本明细", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids) {
        return toAjax(deptMincapDetailService.deleteDeptMincapDetailDtoByIds(ids));
    }

    /**
     * 根据条件删除分部门最低资本明细
     */
    @PreAuthorize("@ss.hasPermi('minc:dept:mincap:detail:remove')")
    @Log(title = "分部门最低资本明细", businessType = BusinessType.DELETE)
    @DeleteMapping("/condition")
    public Result removeByCondition(@RequestParam String accountingPeriod,
                                   @RequestParam String department,
                                   @RequestParam String itemCode) {
        try {
            int result = deptMincapDetailService.deleteDeptMincapDetailDtoByCondition(accountingPeriod, department, itemCode);
            return toAjax(result);
        } catch (Exception e) {
            log.error("删除分部门最低资本明细失败", e);
            return Result.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 根据账期删除分部门最低资本明细
     */
    @PreAuthorize("@ss.hasPermi('minc:dept:mincap:detail:remove')")
    @Log(title = "分部门最低资本明细", businessType = BusinessType.DELETE)
    @DeleteMapping("/period/{accountingPeriod}")
    public Result removeByPeriod(@PathVariable String accountingPeriod) {
        return toAjax(deptMincapDetailService.deleteDeptMincapDetailDtoByPeriod(accountingPeriod));
    }

    /**
     * 导入分部门最低资本明细
     */
    @PreAuthorize("@ss.hasPermi('minc:dept:mincap:detail:import')")
    @Log(title = "分部门最低资本明细", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public Result importData(MultipartFile file, @RequestParam(defaultValue = "false") boolean updateSupport) throws Exception {
        try {
            log.info("开始导入分部门最低资本明细数据，是否更新已有数据：" + updateSupport);

            // 使用EasyExcel读取Excel文件
            List<DeptMincapDetailExportDTO> importList = EasyExcel.read(file.getInputStream())
                    .head(DeptMincapDetailExportDTO.class)
                    .sheet()
                    .doReadSync();

            // 转换为DTO
            List<DeptMincapDetailDTO> dtoList = new ArrayList<>();
            for (DeptMincapDetailExportDTO importDTO : importList) {
                // 跳过空行
                if (importDTO.getDepartment() == null && importDTO.getItemName() == null) {
                    continue;
                }

                DeptMincapDetailDTO dto = new DeptMincapDetailDTO();
                dto.setAccountingPeriod(importDTO.getAccountingPeriod());
                dto.setDepartment(importDTO.getDepartment());
                dto.setItemName(importDTO.getItemName());

                // 处理金额，确保不超出数据库字段范围
                dto.setTraditionalAmount(processAmount(importDTO.getTraditionalAmount()));
                dto.setDividendAmount(processAmount(importDTO.getDividendAmount()));
                dto.setUniversalAmount(processAmount(importDTO.getUniversalAmount()));
                dto.setIndependentAmount(processAmount(importDTO.getIndependentAmount()));
                dto.setCompanyTotalAmount(processAmount(importDTO.getCapitalSupplementAmount()));
                dtoList.add(dto);
            }

            String operName = getUsername();
            String message = deptMincapDetailService.importDeptMincapDetailDto(dtoList, updateSupport, operName);
            return Result.success(message);
        } catch (Exception e) {
            log.error("导入Excel异常", e);
            return Result.error("导入Excel失败：" + e.getMessage());
        }
    }

    /**
     * 导出分部门最低资本明细
     */
    @PreAuthorize("@ss.hasPermi('minc:dept:mincap:detail:export')")
    @Log(title = "分部门最低资本明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DeptMincapDetailQuery query) {
        try {
            // 获取数据
            List<DeptMincapDetailDTO> list = deptMincapDetailService.selectDeptMincapDetailDtoList(query);

            // 转换为导出DTO
            List<DeptMincapDetailExportDTO> exportList = new ArrayList<>();
            for (DeptMincapDetailDTO dto : list) {
                DeptMincapDetailExportDTO exportDTO = new DeptMincapDetailExportDTO();
                exportDTO.setAccountingPeriod(dto.getAccountingPeriod());
                exportDTO.setDepartment(dto.getDepartment());
                exportDTO.setItemName(dto.getItemName());
                exportDTO.setTraditionalAmount(dto.getTraditionalAmount());
                exportDTO.setDividendAmount(dto.getDividendAmount());
                exportDTO.setUniversalAmount(dto.getUniversalAmount());
                exportDTO.setIndependentAmount(dto.getIndependentAmount());
                exportDTO.setCapitalSupplementAmount(dto.getCompanyTotalAmount());
                exportList.add(exportDTO);
            }

            // 设置下载文件的名称和类型
            String fileName = URLEncoder.encode("分部门最低资本明细数据", "UTF-8").replaceAll("\\+", "%20");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            // 使用EasyExcel导出数据，自动调整列宽
            EasyExcel.write(response.getOutputStream(), DeptMincapDetailExportDTO.class)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .sheet("分部门最低资本明细")
                    .doWrite(exportList);
        } catch (Exception e) {
            log.error("导出Excel异常", e);
        }
    }

    /**
     * 获取分部门最低资本明细模板Excel
     */
    @PreAuthorize("@ss.hasPermi('minc:dept:mincap:detail:export')")
    @PostMapping("/exportTemplate")
    public void exportTemplate(HttpServletResponse response) {
        try {
            // 设置下载文件的名称和类型
            String fileName = URLEncoder.encode("分部门最低资本明细模板", "UTF-8").replaceAll("\\+", "%20");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            // 创建一个空的模板数据
            List<DeptMincapDetailExportDTO> templateList = new ArrayList<>();
            DeptMincapDetailExportDTO template = new DeptMincapDetailExportDTO();
            templateList.add(template);

            // 使用EasyExcel导出模板，自动调整列宽
            EasyExcel.write(response.getOutputStream(), DeptMincapDetailExportDTO.class)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .sheet("分部门最低资本明细")
                    .doWrite(templateList);
        } catch (Exception e) {
            log.error("导出模板Excel异常", e);
        }
    }

    /**
     * 处理金额，确保不超出数据库字段范围
     *
     * @param amount 原始金额
     * @return 处理后的金额
     */
    private BigDecimal processAmount(BigDecimal amount) {
        if (amount == null) {
            return BigDecimal.ZERO;
        }

        try {
            // 设置精度为10位小数
            amount = amount.setScale(10, BigDecimal.ROUND_HALF_UP);

            // 检查整数部分是否超过15位
            BigDecimal absAmount = amount.abs();
            String amountStr = absAmount.toPlainString();
            int decimalPointIndex = amountStr.indexOf('.');
            String integerPart = decimalPointIndex > 0 ? amountStr.substring(0, decimalPointIndex) : amountStr;

            if (integerPart.length() > 15) {
                log.warn("金额超出范围，将被截断：" + amount);
                // 截断为最大值或最小值
                BigDecimal maxValue = new BigDecimal("999999999999999.9999999999");
                BigDecimal minValue = new BigDecimal("-999999999999999.9999999999");
                return amount.compareTo(BigDecimal.ZERO) > 0 ? maxValue : minValue;
            }

            return amount;
        } catch (Exception e) {
            log.error("处理金额时出错：" + amount, e);
            return BigDecimal.ZERO;
        }
    }
}
