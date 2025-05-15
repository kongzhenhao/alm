-- 修改会计准备金明细表的字段精度

-- 修改存量累计规模保费列的精度
ALTER TABLE t_base_accounting_reserve_detail MODIFY COLUMN accumulated_premium DECIMAL(28,10) DEFAULT 0 COMMENT '存量累计规模保费';

-- 修改会计准备金合计列的精度
ALTER TABLE t_base_accounting_reserve_detail MODIFY COLUMN total_accounting_reserve DECIMAL(28,10) DEFAULT 0 COMMENT '会计准备金合计';

-- 修改其他金额字段的精度
ALTER TABLE t_base_accounting_reserve_detail MODIFY COLUMN account_value DECIMAL(28,10) DEFAULT 0 COMMENT '账户价值';
ALTER TABLE t_base_accounting_reserve_detail MODIFY COLUMN dividend_provision DECIMAL(28,10) DEFAULT 0 COMMENT '红利预提';
ALTER TABLE t_base_accounting_reserve_detail MODIFY COLUMN best_estimate DECIMAL(28,10) DEFAULT 0 COMMENT '最优估计';
ALTER TABLE t_base_accounting_reserve_detail MODIFY COLUMN risk_margin DECIMAL(28,10) DEFAULT 0 COMMENT '风险边际';
ALTER TABLE t_base_accounting_reserve_detail MODIFY COLUMN residual_margin DECIMAL(28,10) DEFAULT 0 COMMENT '剩余边际';
ALTER TABLE t_base_accounting_reserve_detail MODIFY COLUMN unmodeled_reserve DECIMAL(28,10) DEFAULT 0 COMMENT '未建模准备金';
ALTER TABLE t_base_accounting_reserve_detail MODIFY COLUMN waiver_reserve DECIMAL(28,10) DEFAULT 0 COMMENT '豁免准备金';
ALTER TABLE t_base_accounting_reserve_detail MODIFY COLUMN persistence_bonus_reserve DECIMAL(28,10) DEFAULT 0 COMMENT '持续奖准备金';
ALTER TABLE t_base_accounting_reserve_detail MODIFY COLUMN long_term_unearned DECIMAL(28,10) DEFAULT 0 COMMENT '长期险未到期';
ALTER TABLE t_base_accounting_reserve_detail MODIFY COLUMN short_term_unearned DECIMAL(28,10) DEFAULT 0 COMMENT '短险未到期';
ALTER TABLE t_base_accounting_reserve_detail MODIFY COLUMN unearned_premium_reserve DECIMAL(28,10) DEFAULT 0 COMMENT '未到期责任准备金';
ALTER TABLE t_base_accounting_reserve_detail MODIFY COLUMN reported_unpaid DECIMAL(28,10) DEFAULT 0 COMMENT '已报未决';
ALTER TABLE t_base_accounting_reserve_detail MODIFY COLUMN incurred_unreported DECIMAL(28,10) DEFAULT 0 COMMENT '未报未决';
ALTER TABLE t_base_accounting_reserve_detail MODIFY COLUMN claim_expense_reserve DECIMAL(28,10) DEFAULT 0 COMMENT '理赔费用准备金';
ALTER TABLE t_base_accounting_reserve_detail MODIFY COLUMN outstanding_claim_reserve DECIMAL(28,10) DEFAULT 0 COMMENT '未决赔款准备金';
ALTER TABLE t_base_accounting_reserve_detail MODIFY COLUMN reinsurance_unearned DECIMAL(28,10) DEFAULT 0 COMMENT '应收分保未到期责任准备金';
ALTER TABLE t_base_accounting_reserve_detail MODIFY COLUMN reinsurance_reported DECIMAL(28,10) DEFAULT 0 COMMENT '应收分保已报未决';
ALTER TABLE t_base_accounting_reserve_detail MODIFY COLUMN reinsurance_unreported DECIMAL(28,10) DEFAULT 0 COMMENT '应收分保未报未决';
ALTER TABLE t_base_accounting_reserve_detail MODIFY COLUMN reinsurance_claim_total DECIMAL(28,10) DEFAULT 0 COMMENT '应收分保未决合计';
ALTER TABLE t_base_accounting_reserve_detail MODIFY COLUMN reinsurance_total DECIMAL(28,10) DEFAULT 0 COMMENT '应收分保合计';
ALTER TABLE t_base_accounting_reserve_detail MODIFY COLUMN lapsed_policy_value DECIMAL(28,10) DEFAULT 0 COMMENT '失效保单现价';
ALTER TABLE t_base_accounting_reserve_detail MODIFY COLUMN fractional_month_dividend DECIMAL(28,10) DEFAULT 0 COMMENT '零头月红利';
ALTER TABLE t_base_accounting_reserve_detail MODIFY COLUMN unpaid_dividend DECIMAL(28,10) DEFAULT 0 COMMENT '应付未付红利';
