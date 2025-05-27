package com.xl.alm.app.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.jd.lightning.common.annotation.Log;
import com.jd.lightning.common.core.controller.BaseController;
import com.jd.lightning.common.core.domain.Result;
import com.jd.lightning.common.core.page.TableDataInfo;
import com.jd.lightning.common.enums.BusinessType;
import com.jd.lightning.common.utils.StringUtils;
import com.xl.alm.app.dto.IrHedgeRatioDTO;
import com.xl.alm.app.query.IrHedgeRatioQuery;
import com.xl.alm.app.service.IrHedgeRatioService;
import com.xl.alm.app.util.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 利率风险对冲率表Controller
 *
 * @author AI Assistant
 */
@Slf4j
@RestController
@RequestMapping("/minc/ir/hedge/ratio")
public class IrHedgeRatioController extends BaseController {

    @Autowired
    private IrHedgeRatioService irHedgeRatioService;

    /**
     * 查询利率风险对冲率表列表
     */
    @PreAuthorize("@ss.hasPermi('minc:ir:hedge:ratio:list')")
    @GetMapping("/list")
    public TableDataInfo list(IrHedgeRatioQuery query) {
        try {
            startPage();
            List<IrHedgeRatioDTO> list = irHedgeRatioService.selectIrHedgeRatioDtoList(query);
            return getDataTable(list);
        } catch (Exception e) {
            log.error("查询利率风险对冲率表列表失败", e);
            return getDataTable(new ArrayList<>());
        }
    }

    /**
     * 获取利率风险对冲率表详细信息
     */
    @PreAuthorize("@ss.hasPermi('minc:ir:hedge:ratio:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id) {
        try {
            return Result.success(irHedgeRatioService.selectIrHedgeRatioDtoById(id));
        } catch (Exception e) {
            log.error("获取利率风险对冲率表详细信息失败", e);
            return Result.error("获取详细信息失败：" + e.getMessage());
        }
    }

    /**
     * 新增利率风险对冲率表
     */
    @PreAuthorize("@ss.hasPermi('minc:ir:hedge:ratio:add')")
    @Log(title = "利率风险对冲率表", businessType = BusinessType.INSERT)
    @PostMapping
    public Result add(@Validated @RequestBody IrHedgeRatioDTO dto) {
        try {
            // 检查唯一性约束
            IrHedgeRatioDTO existing = irHedgeRatioService.selectIrHedgeRatioDtoByUniqueKey(
                dto.getAccountingPeriod(), dto.getItemName(), dto.getAccountCode());
            if (existing != null) {
                return Result.error("该账期、项目名称、账户编码的记录已存在");
            }
            return toAjax(irHedgeRatioService.insertIrHedgeRatioDto(dto));
        } catch (Exception e) {
            log.error("新增利率风险对冲率表失败", e);
            return Result.error("新增失败：" + e.getMessage());
        }
    }

    /**
     * 修改利率风险对冲率表
     */
    @PreAuthorize("@ss.hasPermi('minc:ir:hedge:ratio:edit')")
    @Log(title = "利率风险对冲率表", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result edit(@Validated @RequestBody IrHedgeRatioDTO dto) {
        try {
            // 检查唯一性约束（排除当前记录）
            IrHedgeRatioDTO existing = irHedgeRatioService.selectIrHedgeRatioDtoByUniqueKey(
                dto.getAccountingPeriod(), dto.getItemName(), dto.getAccountCode());
            if (existing != null && !existing.getId().equals(dto.getId())) {
                return Result.error("该账期、项目名称、账户编码的记录已存在");
            }
            return toAjax(irHedgeRatioService.updateIrHedgeRatioDto(dto));
        } catch (Exception e) {
            log.error("修改利率风险对冲率表失败", e);
            return Result.error("修改失败：" + e.getMessage());
        }
    }

    /**
     * 删除利率风险对冲率表
     */
    @PreAuthorize("@ss.hasPermi('minc:ir:hedge:ratio:remove')")
    @Log(title = "利率风险对冲率表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids) {
        try {
            return toAjax(irHedgeRatioService.deleteIrHedgeRatioDtoByIds(ids));
        } catch (Exception e) {
            log.error("删除利率风险对冲率表失败", e);
            return Result.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 根据账期删除利率风险对冲率表
     */
    @PreAuthorize("@ss.hasPermi('minc:ir:hedge:ratio:remove')")
    @Log(title = "利率风险对冲率表", businessType = BusinessType.DELETE)
    @DeleteMapping("/period/{accountingPeriod}")
    public Result removeByPeriod(@PathVariable String accountingPeriod) {
        try {
            return toAjax(irHedgeRatioService.deleteIrHedgeRatioDtoByPeriod(accountingPeriod));
        } catch (Exception e) {
            log.error("根据账期删除利率风险对冲率表失败", e);
            return Result.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 导出利率风险对冲率表
     */
    @PreAuthorize("@ss.hasPermi('minc:ir:hedge:ratio:export')")
    @Log(title = "利率风险对冲率表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, IrHedgeRatioQuery query) {
        try {
            List<IrHedgeRatioDTO> list = irHedgeRatioService.selectIrHedgeRatioDtoList(query);

            String fileName = URLEncoder.encode("利率风险对冲率表_" + System.currentTimeMillis(), "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");

            EasyExcel.write(response.getOutputStream(), IrHedgeRatioDTO.class)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .sheet("利率风险对冲率表")
                    .doWrite(list);
        } catch (IOException e) {
            log.error("导出利率风险对冲率表失败", e);
        }
    }

    /**
     * 获取利率风险对冲率表导入模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        try {
            ExcelUtil<IrHedgeRatioDTO> util = new ExcelUtil<>(IrHedgeRatioDTO.class);
            util.exportTemplateExcel(response, "利率风险对冲率表导入模板");
        } catch (Exception e) {
            log.error("下载利率风险对冲率表导入模板失败", e);
        }
    }

    /**
     * 导入利率风险对冲率表数据
     */
    @PreAuthorize("@ss.hasPermi('minc:ir:hedge:ratio:import')")
    @Log(title = "利率风险对冲率表", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public Result importData(MultipartFile file, boolean updateSupport) {
        try {
            if (file == null || file.isEmpty()) {
                return Result.error("上传文件不能为空");
            }

            ExcelUtil<IrHedgeRatioDTO> util = new ExcelUtil<>(IrHedgeRatioDTO.class);
            List<IrHedgeRatioDTO> dtoList = util.importExcel(file.getInputStream());
            if (dtoList == null || dtoList.isEmpty()) {
                return Result.error("导入数据不能为空");
            }

            StringBuilder resultMsg = new StringBuilder();
            int successNum = 0;
            int failureNum = 0;

            for (IrHedgeRatioDTO dto : dtoList) {
                try {
                    // 检查必填字段
                    if (StringUtils.isEmpty(dto.getAccountingPeriod()) ||
                        StringUtils.isEmpty(dto.getItemName()) ||
                        StringUtils.isEmpty(dto.getAccountCode())) {
                        failureNum++;
                        resultMsg.append("<br/>第").append(successNum + failureNum).append("行：账期、项目名称、账户编码不能为空");
                        continue;
                    }

                    // 检查是否存在
                    IrHedgeRatioDTO existing = irHedgeRatioService.selectIrHedgeRatioDtoByUniqueKey(
                        dto.getAccountingPeriod(), dto.getItemName(), dto.getAccountCode());

                    if (existing != null) {
                        if (updateSupport) {
                            dto.setId(existing.getId());
                            irHedgeRatioService.updateIrHedgeRatioDto(dto);
                            successNum++;
                        } else {
                            failureNum++;
                            resultMsg.append("<br/>第").append(successNum + failureNum).append("行：该记录已存在");
                        }
                    } else {
                        irHedgeRatioService.insertIrHedgeRatioDto(dto);
                        successNum++;
                    }
                } catch (Exception e) {
                    failureNum++;
                    resultMsg.append("<br/>第").append(successNum + failureNum).append("行：").append(e.getMessage());
                }
            }

            if (failureNum > 0) {
                resultMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
                return Result.error(resultMsg.toString());
            } else {
                return Result.success("恭喜您，数据已全部导入成功！共 " + successNum + " 条");
            }
        } catch (Exception e) {
            log.error("导入利率风险对冲率表数据失败", e);
            return Result.error("导入失败：" + e.getMessage());
        }
    }
}
