package com.xl.alm.job.minc.mapper;

import com.xl.alm.job.minc.entity.MarginalCapitalCalculationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 边际最低资本贡献率计算 Mapper接口
 * 对应UC0007：计算边际最低资本贡献率数据
 *
 * @author AI Assistant
 */
@Mapper
public interface MarginalCapitalCalculationMapper {

    /**
     * 查询风险项目金额表数据，获取再保后金额
     *
     * @param accountingPeriod 账期
     * @return 风险项目金额数据列表
     */
    List<Map<String, Object>> selectRiskItemAmounts(@Param("accountingPeriod") String accountingPeriod);

    /**
     * 查询所有项目定义表数据
     *
     * @return 项目定义数据列表
     */
    List<Map<String, Object>> selectAllItemDefinitions();

    /**
     * 查询项目定义表中有子风险公式的项目
     *
     * @return 项目定义数据列表
     */
    List<Map<String, Object>> selectItemDefinitionsWithSubRiskFormula();

    /**
     * 查询项目定义表中有公司层面公式的项目
     *
     * @return 项目定义数据列表
     */
    List<Map<String, Object>> selectItemDefinitionsWithCompanyFormula();

    /**
     * 查询指定项目编码的再保后金额
     *
     * @param itemCode 项目编码
     * @param accountingPeriod 账期
     * @return 再保后金额
     */
    BigDecimal selectAmountByItemCode(@Param("itemCode") String itemCode, @Param("accountingPeriod") String accountingPeriod);

    /**
     * 查询两个项目之间的相关系数
     *
     * @param itemCodeX 第一个项目编码
     * @param itemCodeY 第二个项目编码
     * @param accountingPeriod 账期
     * @return 相关系数
     */
    BigDecimal selectCorrelationValue(@Param("itemCodeX") String itemCodeX,
                                     @Param("itemCodeY") String itemCodeY,
                                     @Param("accountingPeriod") String accountingPeriod);

    /**
     * 删除指定账期的边际最低资本贡献率表数据
     *
     * @param accountingPeriod 账期
     * @return 删除的记录数
     */
    int deleteMarginalCapitalByPeriod(@Param("accountingPeriod") String accountingPeriod);

    /**
     * 批量插入边际最低资本贡献率表数据
     *
     * @param entityList 数据列表
     * @return 插入的记录数
     */
    int batchInsertMarginalCapital(List<MarginalCapitalCalculationEntity> entityList);

    /**
     * 更新子风险层面边际最低资本贡献因子
     *
     * @param itemCode 项目编码
     * @param accountingPeriod 账期
     * @param subRiskMarginalFactor 子风险层面边际最低资本贡献因子
     * @return 更新的记录数
     */
    int updateSubRiskMarginalFactor(@Param("itemCode") String itemCode,
                                   @Param("accountingPeriod") String accountingPeriod,
                                   @Param("subRiskMarginalFactor") BigDecimal subRiskMarginalFactor);

    /**
     * 查询指定项目的子风险层面边际最低资本贡献因子
     *
     * @param itemCode 项目编码
     * @param accountingPeriod 账期
     * @return 子风险层面边际最低资本贡献因子
     */
    BigDecimal selectSubRiskMarginalFactor(@Param("itemCode") String itemCode,
                                          @Param("accountingPeriod") String accountingPeriod);

    /**
     * 更新公司层面边际最低资本贡献因子
     *
     * @param itemCode 项目编码
     * @param accountingPeriod 账期
     * @param companyMarginalFactor 公司层面边际最低资本贡献因子
     * @return 更新的记录数
     */
    int updateCompanyMarginalFactor(@Param("itemCode") String itemCode,
                                   @Param("accountingPeriod") String accountingPeriod,
                                   @Param("companyMarginalFactor") BigDecimal companyMarginalFactor);

    /**
     * 查询所有一级风险项目（项目编码不包含下划线的项目）
     *
     * @return 一级风险项目列表
     */
    List<Map<String, Object>> selectFirstLevelRiskItems();
}
