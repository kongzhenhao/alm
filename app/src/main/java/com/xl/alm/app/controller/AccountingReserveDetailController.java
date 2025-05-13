package com.xl.alm.app.controller;

import com.jd.lightning.common.annotation.Log;
import com.jd.lightning.common.core.controller.BaseController;
import com.jd.lightning.common.core.domain.Result;
import com.jd.lightning.common.core.page.TableDataInfo;
import com.jd.lightning.common.enums.BusinessType;
import com.jd.lightning.common.utils.SecurityUtils;
import com.xl.alm.app.entity.AccountingReserveDetailEntity;
import com.xl.alm.app.query.AccountingReserveDetailQuery;
import com.xl.alm.app.service.IAccountingReserveDetailService;
import com.xl.alm.app.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 会计准备金明细表 控制器
 *
 * @author AI Assistant
 */
@RestController
@RequestMapping("/base/accounting/reserve")
public class AccountingReserveDetailController extends BaseController {
    @Autowired
    private IAccountingReserveDetailService accountingReserveDetailService;

    /**
     * 查询会计准备金明细列表
     */
    @PreAuthorize("@ss.hasPermi('base:accounting:reserve:list')")
    @GetMapping("/list")
    public TableDataInfo list(AccountingReserveDetailQuery query) {
        startPage();
        List<AccountingReserveDetailEntity> list = accountingReserveDetailService.selectAccountingReserveDetailList(query);
        return getDataTable(list);
    }

    /**
     * 导出会计准备金明细列表
     */
    @PreAuthorize("@ss.hasPermi('base:accounting:reserve:export')")
    @Log(title = "会计准备金明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AccountingReserveDetailQuery query) {
        List<AccountingReserveDetailEntity> list = accountingReserveDetailService.selectAccountingReserveDetailList(query);
        ExcelUtil<AccountingReserveDetailEntity> util = new ExcelUtil<>(AccountingReserveDetailEntity.class);
        util.exportExcel(list, "会计准备金明细数据", response);
    }

    /**
     * 获取会计准备金明细详细信息
     */
    @PreAuthorize("@ss.hasPermi('base:accounting:reserve:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id) {
        return success(accountingReserveDetailService.selectAccountingReserveDetailById(id));
    }

    /**
     * 新增会计准备金明细
     */
    @PreAuthorize("@ss.hasPermi('base:accounting:reserve:add')")
    @Log(title = "会计准备金明细", businessType = BusinessType.INSERT)
    @PostMapping
    public Result add(@RequestBody AccountingReserveDetailEntity accountingReserveDetail) {
        accountingReserveDetail.setCreateBy(SecurityUtils.getUsername());
        return toAjax(accountingReserveDetailService.insertAccountingReserveDetail(accountingReserveDetail));
    }

    /**
     * 修改会计准备金明细
     */
    @PreAuthorize("@ss.hasPermi('base:accounting:reserve:edit')")
    @Log(title = "会计准备金明细", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result edit(@RequestBody AccountingReserveDetailEntity accountingReserveDetail) {
        accountingReserveDetail.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(accountingReserveDetailService.updateAccountingReserveDetail(accountingReserveDetail));
    }

    /**
     * 删除会计准备金明细
     */
    @PreAuthorize("@ss.hasPermi('base:accounting:reserve:remove')")
    @Log(title = "会计准备金明细", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids) {
        return toAjax(accountingReserveDetailService.deleteAccountingReserveDetailByIds(ids));
    }

    /**
     * 导入会计准备金明细数据
     */
    @PreAuthorize("@ss.hasPermi('base:accounting:reserve:import')")
    @Log(title = "会计准备金明细", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public Result importData(MultipartFile file, boolean updateSupport) throws Exception {
        String operName = SecurityUtils.getUsername();
        String message = accountingReserveDetailService.importAccountingReserveDetail(file, updateSupport, operName);
        return success(message);
    }

    /**
     * 下载会计准备金明细导入模板
     */
    @PreAuthorize("@ss.hasPermi('base:accounting:reserve:import')")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        accountingReserveDetailService.importTemplateAccountingReserveDetail(response);
    }
}
