package com.xl.alm.app.service;

import com.xl.alm.app.entity.AccountingReserveDetailEntity;
import com.xl.alm.app.query.AccountingReserveDetailQuery;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 会计准备金明细表 服务接口
 *
 * @author AI Assistant
 */
public interface IAccountingReserveDetailService {
    /**
     * 查询会计准备金明细列表
     *
     * @param query 会计准备金明细查询条件
     * @return 会计准备金明细列表
     */
    List<AccountingReserveDetailEntity> selectAccountingReserveDetailList(AccountingReserveDetailQuery query);

    /**
     * 根据ID查询会计准备金明细
     *
     * @param id 会计准备金明细ID
     * @return 会计准备金明细
     */
    AccountingReserveDetailEntity selectAccountingReserveDetailById(Long id);

    /**
     * 新增会计准备金明细
     *
     * @param accountingReserveDetail 会计准备金明细
     * @return 结果
     */
    int insertAccountingReserveDetail(AccountingReserveDetailEntity accountingReserveDetail);

    /**
     * 修改会计准备金明细
     *
     * @param accountingReserveDetail 会计准备金明细
     * @return 结果
     */
    int updateAccountingReserveDetail(AccountingReserveDetailEntity accountingReserveDetail);

    /**
     * 批量删除会计准备金明细
     *
     * @param ids 需要删除的会计准备金明细ID
     * @return 结果
     */
    int deleteAccountingReserveDetailByIds(Long[] ids);

    /**
     * 删除会计准备金明细信息
     *
     * @param id 会计准备金明细ID
     * @return 结果
     */
    int deleteAccountingReserveDetailById(Long id);

    /**
     * 导入会计准备金明细数据
     *
     * @param file 导入文件
     * @param updateSupport 是否更新已存在数据
     * @param operName 操作人员
     * @return 结果
     */
    String importAccountingReserveDetail(MultipartFile file, boolean updateSupport, String operName);

    /**
     * 导出会计准备金明细数据模板
     *
     * @param response HTTP响应对象
     */
    void importTemplateAccountingReserveDetail(HttpServletResponse response);
}
