-- 初始化数据库表结构
-- 字符集: utf8
-- 数据库: MySQL 8.0

-- 负债现金流表 (TB0001)
CREATE TABLE IF NOT EXISTS `t_dur_liability_cash_flow` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `account_period` varchar(6) NOT NULL COMMENT '账期，格式YYYYMM',
  `cash_flow_type` char(2) NOT NULL COMMENT '现金流类型,01:流入,02:流出',
  `bp_type` char(2) NOT NULL COMMENT '基点类型,01:+50bp,02:-50bp',
  `duration_type` char(2) NOT NULL COMMENT '久期类型,01:修正久期,02:有效久期',
  `design_type` varchar(50) NOT NULL COMMENT '设计类型',
  `is_short_term` char(1) NOT NULL DEFAULT 'N' COMMENT '是否为中短期险种',
  `actuarial_code` varchar(20) NOT NULL COMMENT '精算代码',
  `business_code` varchar(20) NOT NULL COMMENT '业务代码',
  `product_name` varchar(50) NOT NULL COMMENT '产品名称',
  `insurance_main_type` varchar(50) NOT NULL COMMENT '险种主类',
  `insurance_sub_type` varchar(50) NOT NULL COMMENT '险种细类',
  `cash_val_set` mediumtext NOT NULL COMMENT '现金流值集,分为1272项,格式{"1":{"date":"2025-01-01","val":0.25},"2":{"date":"2025-01-02","val":0.35},...,"1272":{"date":"2025-12-01","val":0.15}}',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `is_del` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除，0:否，1:是',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_unique` (`account_period`, `cash_flow_type`, `duration_type`, `design_type`, `is_short_term`, `actuarial_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='负债现金流表';

-- 折现曲线表 (TB0002)
CREATE TABLE IF NOT EXISTS `t_dur_discount_curve` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `account_period` varchar(6) NOT NULL COMMENT '账期，格式YYYYMM',
  `curve_type` char(2) NOT NULL COMMENT '曲线类型,01:中档,02:低档',
  `bp_type` char(2) NOT NULL COMMENT '基点类型,01:+50bp,02:-50bp',
  `duration_type` char(2) NOT NULL COMMENT '久期类型,01:修正久期,02:有效久期',
  `curve_val_set` mediumtext NOT NULL COMMENT '曲线值集,分为1272项,格式{"1":{"date":"2025-01-01","val":0.25},"2":{"date":"2025-01-02","val":0.35},...,"1272":{"date":"2025-12-01","val":0.15}}',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `is_del` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除，0:否，1:是',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_unique` (`account_period`, `curve_type`, `duration_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='折现曲线表';

-- 折现因子表 (TB0003)
CREATE TABLE IF NOT EXISTS `t_dur_discount_factor` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `account_period` varchar(6) NOT NULL COMMENT '账期，格式YYYYMM',
  `factor_type` char(2) NOT NULL COMMENT '因子类型,01:中档,02:低档',
  `bp_type` char(2) NOT NULL COMMENT '基点类型,01:+50bp,02:-50bp',
  `duration_type` char(2) NOT NULL COMMENT '久期类型,01:修正久期,02:有效久期',
  `factor_val_set` mediumtext NOT NULL COMMENT '因子值集,分为1272项,格式{"1":{"date":"2025-01-01","val":0.25},"2":{"date":"2025-01-02","val":0.35},...,"1272":{"date":"2025-12-01","val":0.15}}',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `is_del` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除，0:否，1:是',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_unique` (`account_period`, `factor_type`, `duration_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='折现因子表';

-- 负债现金流汇总表 (TB0005)
CREATE TABLE IF NOT EXISTS `t_dur_liability_cash_flow_summary` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `account_period` varchar(6) NOT NULL COMMENT '账期，格式YYYYMM',
  `cash_flow_type` char(2) NOT NULL COMMENT '现金流类型,01:流入,02:流出',
  `bp_type` char(2) NOT NULL COMMENT '基点类型,01:+50bp,02:-50bp',
  `duration_type` char(2) NOT NULL COMMENT '久期类型,01:修正久期,02:有效久期',
  `design_type` varchar(50) NOT NULL COMMENT '设计类型',
  `is_short_term` char(1) NOT NULL DEFAULT 'N' COMMENT '是否为中短期险种',
  `cash_val_set` mediumtext NOT NULL COMMENT '现金流值集,分为1272项,格式{"1":{"date":"2025-01-01","val":0.25},"2":{"date":"2025-01-02","val":0.35},...,"1272":{"date":"2025-12-01","val":0.15}}',
  `present_cash_val_set` mediumtext NOT NULL COMMENT '现金流现值值集,分为1272项,格式{"1":{"date":"2025-01-01","val":0.25},"2":{"date":"2025-01-02","val":0.35},...,"1272":{"date":"2025-12-01","val":0.15}}',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `is_del` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除，0:否，1:是',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_unique` (`account_period`, `cash_flow_type`, `duration_type`, `design_type`, `is_short_term`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='负债现金流汇总表';

-- 负债久期汇总表 (TB0007)
CREATE TABLE IF NOT EXISTS `t_dur_liability_duration_summary` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `account_period` varchar(6) NOT NULL COMMENT '账期，格式YYYYMM',
  `cash_flow_type` char(2) NOT NULL COMMENT '现金流类型,01:流入,02:流出',
  `bp_type` char(2) NOT NULL COMMENT '基点类型,01:+50bp,02:-50bp',
  `duration_type` char(2) NOT NULL COMMENT '久期类型,01:修正久期,02:有效久期',
  `design_type` varchar(50) NOT NULL COMMENT '设计类型',
  `is_short_term` char(1) NOT NULL DEFAULT 'N' COMMENT '是否为中短期险种',
  `duration_val_set` mediumtext NOT NULL COMMENT '久期值集,分为1272项,格式{"1":{"date":"2025-01-01","val":0.25},"2":{"date":"2025-01-02","val":0.35},...,"1272":{"date":"2025-12-01","val":0.15}}',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `is_del` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除，0:否，1:是',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_unique` (`account_period`, `cash_flow_type`, `duration_type`, `design_type`, `is_short_term`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='负债久期汇总表';

-- 分账户负债现金流现值汇总表 (TB0008)
CREATE TABLE IF NOT EXISTS `t_dur_sub_account_liability_present_value` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `account_period` varchar(6) NOT NULL COMMENT '账期，格式YYYYMM',
  `cash_flow_type` char(2) NOT NULL COMMENT '现金流类型,01:流入,02:流出',
  `bp_type` char(2) NOT NULL COMMENT '基点类型,01:+50bp,02:-50bp',
  `duration_type` char(2) NOT NULL COMMENT '久期类型,01:修正久期,02:有效久期',
  `design_type` varchar(50) NOT NULL COMMENT '设计类型',
  `present_cash_val_set` mediumtext NOT NULL COMMENT '现金流现值值集,分为1272项,格式{"1":{"date":"2025-01-01","val":0.25},"2":{"date":"2025-01-02","val":0.35},...,"1272":{"date":"2025-12-01","val":0.15}}',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `is_del` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除，0:否，1:是',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_unique` (`account_period`, `cash_flow_type`, `duration_type`, `design_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='分账户负债现金流现值汇总表';

-- 分账户负债久期汇总表 (TB0009)
CREATE TABLE IF NOT EXISTS `t_dur_sub_account_liability_duration` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `account_period` varchar(6) NOT NULL COMMENT '账期，格式YYYYMM',
  `cash_flow_type` char(2) NOT NULL COMMENT '现金流类型,01:流入,02:流出',
  `bp_type` char(2) NOT NULL COMMENT '基点类型,01:+50bp,02:-50bp',
  `duration_type` char(2) NOT NULL COMMENT '久期类型,01:修正久期,02:有效久期',
  `design_type` varchar(50) NOT NULL COMMENT '设计类型',
  `duration_val_set` mediumtext NOT NULL COMMENT '久期值集,分为1272项,格式{"1":{"date":"2025-01-01","val":0.25},"2":{"date":"2025-01-02","val":0.35},...,"1272":{"date":"2025-12-01","val":0.15}}',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `is_del` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除，0:否，1:是',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_unique` (`account_period`, `cash_flow_type`, `duration_type`, `design_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='分账户负债久期汇总表';

-- 关键久期参数表 (TB0003)
CREATE TABLE IF NOT EXISTS `t_dur_key_duration_parameter` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `account_period` varchar(6) NOT NULL COMMENT '账期,格式YYYYMM',
  `key_duration` char(2) NOT NULL COMMENT '关键期限点',
  `parameter_val_set` mediumtext NOT NULL COMMENT '关键久期参数值集,分为1273项,格式{"1":{"date":"2025-01-01","val":0.25},"2":{"date":"2025-01-02","val":0.35},...,"1273":{"date":"2025-12-01","val":0.15}}',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `is_del` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除，0:否，1:是',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_unique` (`account_period`, `key_duration`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='关键久期参数表';

-- 关键久期折现曲线表 (TB0004)
CREATE TABLE IF NOT EXISTS `t_dur_key_duration_discount_curve` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `account_period` varchar(6) NOT NULL COMMENT '账期,格式YYYYMM',
  `curve_type` char(2) NOT NULL COMMENT '曲线类型,01:中档,02:低档',
  `key_duration` char(2) NOT NULL COMMENT '关键期限点',
  `stress_direction` char(2) NOT NULL COMMENT '压力方向,01:上升,02:下降',
  `duration_type` char(2) NOT NULL COMMENT '久期类型,01:修正久期,02:有效久期,03:关键久期',
  `curve_val_set` mediumtext NOT NULL COMMENT '曲线值集,分为1273项,格式{"1":{"date":"2025-01-01","val":0.25},"2":{"date":"2025-01-02","val":0.35},...,"1273":{"date":"2025-12-01","val":0.15}}',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `is_del` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除，0:否，1:是',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_unique` (`account_period`, `curve_type`, `key_duration`, `stress_direction`, `duration_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='关键久期折现曲线表';

-- 关键久期折现因子表 (TB0005)
CREATE TABLE IF NOT EXISTS `t_dur_key_duration_discount_factors` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `account_period` varchar(6) NOT NULL COMMENT '账期,格式YYYYMM',
  `curve_type` char(2) NOT NULL COMMENT '曲线类型,01:中档,02:低档',
  `key_duration` char(2) NOT NULL COMMENT '关键期限点',
  `stress_direction` char(2) NOT NULL COMMENT '压力方向,01:上升,02:下降',
  `duration_type` char(2) NOT NULL COMMENT '久期类型,01:修正久期,02:有效久期,03:关键久期',
  `factor_val_set` mediumtext NOT NULL COMMENT '因子值集,分为1273项,格式{"1":{"date":"2025-01-01","val":0.25},"2":{"date":"2025-01-02","val":0.35},...,"1273":{"date":"2025-12-01","val":0.15}}',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `is_del` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除，0:否，1:是',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_unique` (`account_period`, `curve_type`, `key_duration`, `stress_direction`, `duration_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='关键久期折现因子表';

-- 分中短负债基点价值DV10表 (TB0006)
CREATE TABLE IF NOT EXISTS `t_dur_liability_dv10_by_duration` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `account_period` varchar(6) NOT NULL COMMENT '账期,格式YYYYMM',
  `cash_flow_type` char(2) NOT NULL COMMENT '现金流类型,01:流入,02:流出',
  `design_type` char(2) NOT NULL COMMENT '设计类型',
  `short_term_flag` char(1) NOT NULL DEFAULT 'N' COMMENT '是否为中短期险种,Y:是,N:否',
  `value_type` char(2) NOT NULL COMMENT '现值类型,01:上升,02:下降,03:净值',
  `term_0` decimal(18,10) DEFAULT 0 COMMENT '0年期限点的DV10现值',
  `term_0_5` decimal(18,10) DEFAULT 0 COMMENT '0.5年期限点的DV10现值',
  `term_1` decimal(18,10) DEFAULT 0 COMMENT '1年期限点的DV10现值',
  `term_2` decimal(18,10) DEFAULT 0 COMMENT '2年期限点的DV10现值',
  `term_3` decimal(18,10) DEFAULT 0 COMMENT '3年期限点的DV10现值',
  `term_4` decimal(18,10) DEFAULT 0 COMMENT '4年期限点的DV10现值',
  `term_5` decimal(18,10) DEFAULT 0 COMMENT '5年期限点的DV10现值',
  `term_6` decimal(18,10) DEFAULT 0 COMMENT '6年期限点的DV10现值',
  `term_7` decimal(18,10) DEFAULT 0 COMMENT '7年期限点的DV10现值',
  `term_8` decimal(18,10) DEFAULT 0 COMMENT '8年期限点的DV10现值',
  `term_10` decimal(18,10) DEFAULT 0 COMMENT '10年期限点的DV10现值',
  `term_12` decimal(18,10) DEFAULT 0 COMMENT '12年期限点的DV10现值',
  `term_15` decimal(18,10) DEFAULT 0 COMMENT '15年期限点的DV10现值',
  `term_20` decimal(18,10) DEFAULT 0 COMMENT '20年期限点的DV10现值',
  `term_25` decimal(18,10) DEFAULT 0 COMMENT '25年期限点的DV10现值',
  `term_30` decimal(18,10) DEFAULT 0 COMMENT '30年期限点的DV10现值',
  `term_35` decimal(18,10) DEFAULT 0 COMMENT '35年期限点的DV10现值',
  `term_40` decimal(18,10) DEFAULT 0 COMMENT '40年期限点的DV10现值',
  `term_45` decimal(18,10) DEFAULT 0 COMMENT '45年期限点的DV10现值',
  `term_50` decimal(18,10) DEFAULT 0 COMMENT '50年期限点的DV10现值',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `is_del` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除，0:否，1:是',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_unique` (`account_period`, `cash_flow_type`, `design_type`, `short_term_flag`, `value_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='分中短负债基点价值DV10表';

-- 分账户负债基点价值DV10表 (TB0007)
CREATE TABLE IF NOT EXISTS `t_dur_account_liability_dv10` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `account_period` varchar(6) NOT NULL COMMENT '账期,格式YYYYMM',
  `cash_flow_type` char(2) NOT NULL COMMENT '现金流类型,01:流入,02:流出',
  `design_type` char(2) NOT NULL COMMENT '设计类型',
  `value_type` char(2) NOT NULL COMMENT '现值类型,01:上升,02:下降,03:净值',
  `term_0` decimal(18,10) DEFAULT 0 COMMENT '0年期限点的DV10现值',
  `term_0_5` decimal(18,10) DEFAULT 0 COMMENT '0.5年期限点的DV10现值',
  `term_1` decimal(18,10) DEFAULT 0 COMMENT '1年期限点的DV10现值',
  `term_2` decimal(18,10) DEFAULT 0 COMMENT '2年期限点的DV10现值',
  `term_3` decimal(18,10) DEFAULT 0 COMMENT '3年期限点的DV10现值',
  `term_4` decimal(18,10) DEFAULT 0 COMMENT '4年期限点的DV10现值',
  `term_5` decimal(18,10) DEFAULT 0 COMMENT '5年期限点的DV10现值',
  `term_6` decimal(18,10) DEFAULT 0 COMMENT '6年期限点的DV10现值',
  `term_7` decimal(18,10) DEFAULT 0 COMMENT '7年期限点的DV10现值',
  `term_8` decimal(18,10) DEFAULT 0 COMMENT '8年期限点的DV10现值',
  `term_10` decimal(18,10) DEFAULT 0 COMMENT '10年期限点的DV10现值',
  `term_12` decimal(18,10) DEFAULT 0 COMMENT '12年期限点的DV10现值',
  `term_15` decimal(18,10) DEFAULT 0 COMMENT '15年期限点的DV10现值',
  `term_20` decimal(18,10) DEFAULT 0 COMMENT '20年期限点的DV10现值',
  `term_25` decimal(18,10) DEFAULT 0 COMMENT '25年期限点的DV10现值',
  `term_30` decimal(18,10) DEFAULT 0 COMMENT '30年期限点的DV10现值',
  `term_35` decimal(18,10) DEFAULT 0 COMMENT '35年期限点的DV10现值',
  `term_40` decimal(18,10) DEFAULT 0 COMMENT '40年期限点的DV10现值',
  `term_45` decimal(18,10) DEFAULT 0 COMMENT '45年期限点的DV10现值',
  `term_50` decimal(18,10) DEFAULT 0 COMMENT '50年期限点的DV10现值',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `is_del` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除，0:否，1:是',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_unique` (`account_period`, `cash_flow_type`, `design_type`, `value_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='分账户负债基点价值DV10表';
