-- 修改法定准备金明细表的accumulated_premium列的精度
-- 修改法定准备金明细表的字段精度

-- 修改存量累计规模保费列的精度
ALTER TABLE t_base_statutory_reserve_detail MODIFY COLUMN accumulated_premium DECIMAL(28,10) DEFAULT 0 COMMENT '存量累计规模保费';

-- 修改法定准备金合计列的精度
ALTER TABLE t_base_statutory_reserve_detail MODIFY COLUMN total_statutory_reserve DECIMAL(28,10) DEFAULT 0 COMMENT '法定准备金合计';

-- 修改其他金额字段的精度
ALTER TABLE t_base_statutory_reserve_detail MODIFY COLUMN account_value DECIMAL(28,10) DEFAULT 0 COMMENT '账户价值';
ALTER TABLE t_base_statutory_reserve_detail MODIFY COLUMN statutory_reserve DECIMAL(28,10) DEFAULT 0 COMMENT '法定/非单位准备金';
ALTER TABLE t_base_statutory_reserve_detail MODIFY COLUMN guaranteed_rate_reserve DECIMAL(28,10) DEFAULT 0 COMMENT '保证利率准备金';
ALTER TABLE t_base_statutory_reserve_detail MODIFY COLUMN lapsed_policy_value DECIMAL(28,10) DEFAULT 0 COMMENT '失效单现价';
ALTER TABLE t_base_statutory_reserve_detail MODIFY COLUMN waiver_reserve DECIMAL(28,10) DEFAULT 0 COMMENT '豁免责任准备金';
ALTER TABLE t_base_statutory_reserve_detail MODIFY COLUMN unmodeled_reserve DECIMAL(28,10) DEFAULT 0 COMMENT '未建模准备金';
ALTER TABLE t_base_statutory_reserve_detail MODIFY COLUMN persistence_bonus_reserve DECIMAL(28,10) DEFAULT 0 COMMENT '持续奖准备金';
ALTER TABLE t_base_statutory_reserve_detail MODIFY COLUMN long_term_unearned DECIMAL(28,10) DEFAULT 0 COMMENT '长期未到期准备金';
ALTER TABLE t_base_statutory_reserve_detail MODIFY COLUMN short_term_unearned DECIMAL(28,10) DEFAULT 0 COMMENT '短险未到期准备金';
ALTER TABLE t_base_statutory_reserve_detail MODIFY COLUMN unearned_premium_reserve DECIMAL(28,10) DEFAULT 0 COMMENT '未到期责任准备金';
ALTER TABLE t_base_statutory_reserve_detail MODIFY COLUMN reported_unpaid DECIMAL(28,10) DEFAULT 0 COMMENT '已报未决赔款';
ALTER TABLE t_base_statutory_reserve_detail MODIFY COLUMN incurred_unreported DECIMAL(28,10) DEFAULT 0 COMMENT '未报未决赔款';
ALTER TABLE t_base_statutory_reserve_detail MODIFY COLUMN claim_expense_reserve DECIMAL(28,10) DEFAULT 0 COMMENT '理赔费用准备金';
ALTER TABLE t_base_statutory_reserve_detail MODIFY COLUMN outstanding_claim_reserve DECIMAL(28,10) DEFAULT 0 COMMENT '未决赔款准备金';
