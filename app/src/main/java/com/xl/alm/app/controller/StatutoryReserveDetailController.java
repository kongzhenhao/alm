package com.xl.alm.app.controller;

import com.jd.lightning.common.annotation.Log;
import com.jd.lightning.common.core.controller.BaseController;
import com.jd.lightning.common.core.domain.Result;
import com.jd.lightning.common.core.page.TableDataInfo;
import com.jd.lightning.common.enums.BusinessType;
import com.jd.lightning.common.utils.SecurityUtils;
import com.xl.alm.app.entity.StatutoryReserveDetailEntity;
import com.xl.alm.app.dto.StatutoryReserveDetailDTO;
import com.xl.alm.app.query.StatutoryReserveDetailQuery;
import com.xl.alm.app.service.IStatutoryReserveDetailService;
import com.xl.alm.app.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 法定准备金明细表 控制器
 *
 * @author AI Assistant
 */
@RestController
@RequestMapping("/base/statutory/reserve")
public class StatutoryReserveDetailController extends BaseController {
    @Autowired
    private IStatutoryReserveDetailService statutoryReserveDetailService;

    /**
     * 查询法定准备金明细列表
     */
    @PreAuthorize("@ss.hasPermi('base:statutory:reserve:list')")
    @GetMapping("/list")
    public TableDataInfo list(StatutoryReserveDetailQuery query) {
        startPage();
        List<StatutoryReserveDetailEntity> list = statutoryReserveDetailService.selectStatutoryReserveDetailList(query);
        return getDataTable(list);
    }

    /**
     * 导出法定准备金明细列表
     */
    @PreAuthorize("@ss.hasPermi('base:statutory:reserve:export')")
    @Log(title = "法定准备金明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StatutoryReserveDetailQuery query) {
        List<StatutoryReserveDetailEntity> list = statutoryReserveDetailService.selectStatutoryReserveDetailList(query);

        // 将Entity转换为DTO
        List<StatutoryReserveDetailDTO> dtoList = new ArrayList<>();
        for (StatutoryReserveDetailEntity entity : list) {
            StatutoryReserveDetailDTO dto = new StatutoryReserveDetailDTO();
            dto.setId(entity.getId());
            dto.setAccountingPeriod(entity.getAccountingPeriod());
            dto.setActuarialCode(entity.getActuarialCode());
            dto.setBusinessCode(entity.getBusinessCode());
            dto.setProductName(entity.getProductName());
            dto.setTermType(entity.getTermType());
            dto.setDesignType(entity.getDesignType());
            dto.setShortTermFlag(entity.getShortTermFlag());
            dto.setValidPolicyCount(entity.getValidPolicyCount());
            dto.setAccumulatedPremium(entity.getAccumulatedPremium());
            dto.setAccountValue(entity.getAccountValue());
            dto.setStatutoryReserve(entity.getStatutoryReserve());
            dto.setGuaranteedRateReserve(entity.getGuaranteedRateReserve());
            dto.setLapsedPolicyValue(entity.getLapsedPolicyValue());
            dto.setWaiverReserve(entity.getWaiverReserve());
            dto.setUnmodeledReserve(entity.getUnmodeledReserve());
            dto.setPersistenceBonusReserve(entity.getPersistenceBonusReserve());
            dto.setLongTermUnearned(entity.getLongTermUnearned());
            dto.setShortTermUnearned(entity.getShortTermUnearned());
            dto.setUnearnedPremiumReserve(entity.getUnearnedPremiumReserve());
            dto.setReportedUnpaid(entity.getReportedUnpaid());
            dto.setIncurredUnreported(entity.getIncurredUnreported());
            dto.setClaimExpenseReserve(entity.getClaimExpenseReserve());
            dto.setOutstandingClaimReserve(entity.getOutstandingClaimReserve());
            dto.setTotalStatutoryReserve(entity.getTotalStatutoryReserve());
            dto.setRemark(entity.getRemark());
            dto.setCreateBy(entity.getCreateBy());
            dto.setCreateTime(entity.getCreateTime());
            dto.setUpdateBy(entity.getUpdateBy());
            dto.setUpdateTime(entity.getUpdateTime());
            dto.setIsDel(entity.getIsDel());
            dtoList.add(dto);
        }

        ExcelUtil<StatutoryReserveDetailDTO> util = new ExcelUtil<>(StatutoryReserveDetailDTO.class);
        util.exportExcel(dtoList, "法定准备金明细数据", response);
    }

    /**
     * 获取法定准备金明细详细信息
     */
    @PreAuthorize("@ss.hasPermi('base:statutory:reserve:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id) {
        return success(statutoryReserveDetailService.selectStatutoryReserveDetailById(id));
    }

    /**
     * 新增法定准备金明细
     */
    @PreAuthorize("@ss.hasPermi('base:statutory:reserve:add')")
    @Log(title = "法定准备金明细", businessType = BusinessType.INSERT)
    @PostMapping
    public Result add(@RequestBody StatutoryReserveDetailEntity statutoryReserveDetail) {
        statutoryReserveDetail.setCreateBy(SecurityUtils.getUsername());
        return toAjax(statutoryReserveDetailService.insertStatutoryReserveDetail(statutoryReserveDetail));
    }

    /**
     * 修改法定准备金明细
     */
    @PreAuthorize("@ss.hasPermi('base:statutory:reserve:edit')")
    @Log(title = "法定准备金明细", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result edit(@RequestBody StatutoryReserveDetailEntity statutoryReserveDetail) {
        statutoryReserveDetail.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(statutoryReserveDetailService.updateStatutoryReserveDetail(statutoryReserveDetail));
    }

    /**
     * 删除法定准备金明细
     */
    @PreAuthorize("@ss.hasPermi('base:statutory:reserve:remove')")
    @Log(title = "法定准备金明细", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids) {
        return toAjax(statutoryReserveDetailService.deleteStatutoryReserveDetailByIds(ids));
    }

    /**
     * 导入法定准备金明细数据
     */
    @PreAuthorize("@ss.hasPermi('base:statutory:reserve:import')")
    @Log(title = "法定准备金明细", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public Result importData(MultipartFile file, boolean updateSupport) throws Exception {
        String operName = SecurityUtils.getUsername();
        String message = statutoryReserveDetailService.importStatutoryReserveDetail(file, updateSupport, operName);
        return success(message);
    }

    /**
     * 下载法定准备金明细导入模板
     */
    @GetMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        statutoryReserveDetailService.importTemplateStatutoryReserveDetail(response);
    }
}
