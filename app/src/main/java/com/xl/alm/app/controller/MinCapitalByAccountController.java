package com.xl.alm.app.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.jd.lightning.common.annotation.Log;
import com.jd.lightning.common.core.controller.BaseController;
import com.jd.lightning.common.core.domain.Result;
import com.jd.lightning.common.core.page.TableDataInfo;
import com.jd.lightning.common.enums.BusinessType;
import com.jd.lightning.common.utils.poi.ExcelUtil;
import com.xl.alm.app.dto.ItemDefinitionDTO;
import com.xl.alm.app.dto.MinCapitalByAccountDTO;
import com.xl.alm.app.dto.MinCapitalByAccountExportDTO;
import com.xl.alm.app.query.ItemDefinitionQuery;
import com.xl.alm.app.query.MinCapitalByAccountQuery;
import com.xl.alm.app.service.ItemDefinitionService;
import com.xl.alm.app.service.MinCapitalByAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 市场及信用最低资本表Controller
 *
 * @author alm
 * @date 2024-01-01
 */
@Slf4j
@RestController
@RequestMapping("/minc/min/capital/by/account")
public class MinCapitalByAccountController extends BaseController {

    @Autowired
    private MinCapitalByAccountService minCapitalByAccountService;

    @Autowired
    private ItemDefinitionService itemDefinitionService;

    /**
     * 获取项目定义列表（用于下拉选择）
     */
    @GetMapping("/itemDefinitions")
    public Result getItemDefinitions() {
        try {
            List<ItemDefinitionDTO> itemDefinitions = itemDefinitionService.selectItemDefinitionDtoList(new ItemDefinitionQuery());
            //log.info("获取项目定义列表，总数量: {}, 过滤后数量: {}", itemDefinitions.size(), itemDefinitions.size());
            return Result.success(itemDefinitions);
        } catch (Exception e) {
            log.error("获取项目定义列表失败", e);
            return Result.error("获取项目定义列表失败：" + e.getMessage());
        }
    }

    /**
     * 查询市场及信用最低资本表列表
     */
    @PreAuthorize("@ss.hasPermi('minc:min:capital:by:account:list')")
    @GetMapping("/list")
    public TableDataInfo list(MinCapitalByAccountQuery query) {
        try {
            startPage();
            List<MinCapitalByAccountDTO> list = minCapitalByAccountService.selectMinCapitalByAccountDtoList(query);
            return getDataTable(list);
        } catch (Exception e) {
            log.error("查询市场及信用最低资本表列表失败", e);
            return getDataTable(new ArrayList<>());
        }
    }

    /**
     * 导出市场及信用最低资本表列表
     */
    @PreAuthorize("@ss.hasPermi('minc:min:capital:by:account:export')")
    @Log(title = "市场及信用最低资本表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MinCapitalByAccountQuery query) {
        try {
            List<MinCapitalByAccountDTO> list = minCapitalByAccountService.selectMinCapitalByAccountDtoList(query);

            // 转换为导出DTO，使用项目名称
            List<MinCapitalByAccountExportDTO> exportList = new ArrayList<>();
            for (MinCapitalByAccountDTO dto : list) {
                MinCapitalByAccountExportDTO exportDto = new MinCapitalByAccountExportDTO();
                exportDto.setAccountingPeriod(dto.getAccountingPeriod());
                exportDto.setItemName(dto.getItemName() != null ? dto.getItemName() : dto.getItemCode()); // 使用项目名称
                exportDto.setTraditionalAmount(dto.getTraditionalAmount());
                exportDto.setDividendAmount(dto.getDividendAmount());
                exportDto.setUniversalAmount(dto.getUniversalAmount());
                exportDto.setGeneralAmount(dto.getGeneralAmount());
                exportList.add(exportDto);
            }

            // 设置响应头
            String fileName = URLEncoder.encode("市场及信用最低资本表", "UTF-8").replaceAll("\\+", "%20");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            // 使用EasyExcel导出
            EasyExcel.write(response.getOutputStream(), MinCapitalByAccountExportDTO.class)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .sheet("市场及信用最低资本表")
                    .doWrite(exportList);

        } catch (IOException e) {
            log.error("导出市场及信用最低资本表失败", e);
        }
    }

    /**
     * 获取市场及信用最低资本表详细信息
     */
    @PreAuthorize("@ss.hasPermi('minc:min:capital:by:account:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id) {
        try {
            return Result.success(minCapitalByAccountService.selectMinCapitalByAccountDtoById(id));
        } catch (Exception e) {
            log.error("获取市场及信用最低资本表详细信息失败", e);
            return Result.error("获取详细信息失败：" + e.getMessage());
        }
    }

    /**
     * 新增市场及信用最低资本表
     */
    @PreAuthorize("@ss.hasPermi('minc:min:capital:by:account:add')")
    @Log(title = "市场及信用最低资本表", businessType = BusinessType.INSERT)
    @PostMapping
    public Result add(@RequestBody MinCapitalByAccountDTO minCapitalByAccountDTO) {
        try {
            // 检查是否已存在相同的记录
            MinCapitalByAccountDTO existDto = minCapitalByAccountService.selectValidMinCapitalByAccountDtoByUniqueKey(
                    minCapitalByAccountDTO.getAccountingPeriod(),
                    minCapitalByAccountDTO.getItemCode(),
                    minCapitalByAccountDTO.getAccountCode());

            if (existDto != null) {
                return Result.error("该账期、项目和账户的记录已存在，不能重复添加");
            }

            return toAjax(minCapitalByAccountService.insertMinCapitalByAccountDto(minCapitalByAccountDTO));
        } catch (Exception e) {
            log.error("新增市场及信用最低资本表失败", e);
            return Result.error("新增失败：" + e.getMessage());
        }
    }

    /**
     * 修改市场及信用最低资本表
     */
    @PreAuthorize("@ss.hasPermi('minc:min:capital:by:account:edit')")
    @Log(title = "市场及信用最低资本表", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result edit(@RequestBody MinCapitalByAccountDTO minCapitalByAccountDTO) {
        try {
            // 如果修改了唯一键字段，需要检查是否与其他记录冲突
            if (minCapitalByAccountDTO.getId() != null) {
                MinCapitalByAccountDTO existDto = minCapitalByAccountService.selectValidMinCapitalByAccountDtoByUniqueKey(
                        minCapitalByAccountDTO.getAccountingPeriod(),
                        minCapitalByAccountDTO.getItemCode(),
                        minCapitalByAccountDTO.getAccountCode());

                // 如果存在记录且不是当前记录，则说明有冲突
                if (existDto != null && !existDto.getId().equals(minCapitalByAccountDTO.getId())) {
                    return Result.error("该账期、项目和账户的记录已存在，不能重复");
                }
            }

            return toAjax(minCapitalByAccountService.updateMinCapitalByAccountDto(minCapitalByAccountDTO));
        } catch (Exception e) {
            log.error("修改市场及信用最低资本表失败", e);
            return Result.error("修改失败：" + e.getMessage());
        }
    }

    /**
     * 删除市场及信用最低资本表
     */
    @PreAuthorize("@ss.hasPermi('minc:min:capital:by:account:remove')")
    @Log(title = "市场及信用最低资本表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids) {
        try {
            return toAjax(minCapitalByAccountService.deleteMinCapitalByAccountDtoByIds(ids));
        } catch (Exception e) {
            log.error("删除市场及信用最低资本表失败", e);
            return Result.error("删除失败：" + e.getMessage());
        }
    }
}
