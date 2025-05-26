package com.xl.alm.job.minc.mapper;

import com.xl.alm.job.minc.entity.MarketCreditCapitalEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 市场及信用最低资本计算Mapper接口
 *
 * @author AI Assistant
 */
@Mapper
public interface MarketCreditCapitalMapper {

    /**
     * 查询项目定义表中有最低资本计算公式的项目
     *
     * @return 项目定义数据列表
     */
    List<Map<String, Object>> selectItemDefinitionsWithCapitalFormula();

    /**
     * 查询指定项目编码和账户编码在TB0007表中的金额
     *
     * @param itemCode 项目编码
     * @param accountCode 账户编码
     * @param accountingPeriod 账期
     * @return 金额
     */
    BigDecimal selectAmountFromSummary(@Param("itemCode") String itemCode,
                                      @Param("accountCode") String accountCode,
                                      @Param("accountingPeriod") String accountingPeriod);

    /**
     * 查询指定项目编码的公司层面边际最低资本贡献因子
     *
     * @param itemCode 项目编码
     * @param accountingPeriod 账期
     * @return 公司层面边际最低资本贡献因子
     */
    BigDecimal selectCompanyMarginalFactor(@Param("itemCode") String itemCode,
                                          @Param("accountingPeriod") String accountingPeriod);

    /**
     * 查询所有账户编码
     *
     * @return 账户编码列表
     */
    List<String> selectAllAccountCodes();

    /**
     * 删除指定账期的市场及信用最低资本数据
     *
     * @param accountingPeriod 账期
     * @return 删除的记录数
     */
    int deleteByAccountingPeriod(@Param("accountingPeriod") String accountingPeriod);

    /**
     * 批量插入市场及信用最低资本数据
     *
     * @param entityList 实体列表
     * @return 插入的记录数
     */
    int batchInsertMarketCreditCapital(List<MarketCreditCapitalEntity> entityList);

    /**
     * 查询指定账期的市场及信用最低资本数据
     *
     * @param accountingPeriod 账期
     * @return 实体列表
     */
    List<MarketCreditCapitalEntity> selectByAccountingPeriod(@Param("accountingPeriod") String accountingPeriod);

    /**
     * 按风险类型和账户汇总金额
     *
     * @param accountingPeriod 账期
     * @return 汇总结果列表
     */
    List<Map<String, Object>> selectSummaryByRiskTypeAndAccount(@Param("accountingPeriod") String accountingPeriod);
}
