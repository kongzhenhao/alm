-- 初始化数据库表结构
-- 字符集: utf8
-- 数据库: MySQL 8.0

-- 负债现金流表 (TB0001)
CREATE TABLE IF NOT EXISTS `t_dur_liability_cash_flow` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `account_period` varchar(6) NOT NULL COMMENT '账期，格式YYYYMM',
  `cash_flow_type` char(2) NOT NULL COMMENT '现金流类型,01:流入,02:流出,03:流出+50bp,04:流出-50bp',
  `duration_type` char(2) NOT NULL COMMENT '久期类型,01:修正久期,02:有效久期',
  `design_type` varchar(50) NOT NULL COMMENT '设计类型',
  `is_short_term` char(1) NOT NULL DEFAULT 'N' COMMENT '是否为中短期险种',
  `actuarial_code` varchar(20) NOT NULL COMMENT '精算代码',
  `business_code` varchar(20) NOT NULL COMMENT '业务代码',
  `product_name` varchar(50) NOT NULL COMMENT '产品名称',
  `insurance_main_type` varchar(50) NOT NULL COMMENT '险种主类',
  `insurance_sub_type` varchar(50) NOT NULL COMMENT '险种细类',
  `cash_val_set` varchar(65535) NOT NULL COMMENT '现金流值集,分为1272项,格式{"1":{"date":"2025-01-01","val":0.25},"2":{"date":"2025-01-02","val":0.35},...,"1272":{"date":"2025-12-01","val":0.15}}',
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
  `duration_type` char(2) NOT NULL COMMENT '久期类型,01:修正久期,02:有效久期',
  `curve_val_set` varchar(65535) NOT NULL COMMENT '曲线值集,分为1272项,格式{"1":{"date":"2025-01-01","val":0.25},"2":{"date":"2025-01-02","val":0.35},...,"1272":{"date":"2025-12-01","val":0.15}}',
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
  `duration_type` char(2) NOT NULL COMMENT '久期类型,01:修正久期,02:有效久期',
  `factor_val_set` varchar(65535) NOT NULL COMMENT '因子值集,分为1272项,格式{"1":{"date":"2025-01-01","val":0.25},"2":{"date":"2025-01-02","val":0.35},...,"1272":{"date":"2025-12-01","val":0.15}}',
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
  `cash_flow_type` char(2) NOT NULL COMMENT '现金流类型,01:流入,02:流出,03:流出+50bp,04:流出-50bp',
  `duration_type` char(2) NOT NULL COMMENT '久期类型,01:修正久期,02:有效久期',
  `design_type` varchar(50) NOT NULL COMMENT '设计类型',
  `is_short_term` char(1) NOT NULL DEFAULT 'N' COMMENT '是否为中短期险种',
  `cash_val_set` varchar(65535) NOT NULL COMMENT '现金流值集,分为1272项,格式{"1":{"date":"2025-01-01","val":0.25},"2":{"date":"2025-01-02","val":0.35},...,"1272":{"date":"2025-12-01","val":0.15}}',
  `present_cash_val_set` varchar(65535) NOT NULL COMMENT '现金流现值值集,分为1272项,格式{"1":{"date":"2025-01-01","val":0.25},"2":{"date":"2025-01-02","val":0.35},...,"1272":{"date":"2025-12-01","val":0.15}}',
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
  `cash_flow_type` char(2) NOT NULL COMMENT '现金流类型,01:流入,02:流出,03:流出+50bp,04:流出-50bp',
  `duration_type` char(2) NOT NULL COMMENT '久期类型,01:修正久期,02:有效久期',
  `design_type` varchar(50) NOT NULL COMMENT '设计类型',
  `is_short_term` char(1) NOT NULL DEFAULT 'N' COMMENT '是否为中短期险种',
  `duration_val_set` varchar(65535) NOT NULL COMMENT '久期值集,分为1272项,格式{"1":{"date":"2025-01-01","val":0.25},"2":{"date":"2025-01-02","val":0.35},...,"1272":{"date":"2025-12-01","val":0.15}}',
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
  `cash_flow_type` char(2) NOT NULL COMMENT '现金流类型,01:流入,02:流出,03:流出+50bp,04:流出-50bp',
  `duration_type` char(2) NOT NULL COMMENT '久期类型,01:修正久期,02:有效久期',
  `design_type` varchar(50) NOT NULL COMMENT '设计类型',
  `present_cash_val_set` varchar(65535) NOT NULL COMMENT '现金流现值值集,分为1272项,格式{"1":{"date":"2025-01-01","val":0.25},"2":{"date":"2025-01-02","val":0.35},...,"1272":{"date":"2025-12-01","val":0.15}}',
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
  `cash_flow_type` char(2) NOT NULL COMMENT '现金流类型,01:流入,02:流出,03:流出+50bp,04:流出-50bp',
  `duration_type` char(2) NOT NULL COMMENT '久期类型,01:修正久期,02:有效久期',
  `design_type` varchar(50) NOT NULL COMMENT '设计类型',
  `duration_val_set` varchar(65535) NOT NULL COMMENT '久期值集,分为1272项,格式{"1":{"date":"2025-01-01","val":0.25},"2":{"date":"2025-01-02","val":0.35},...,"1272":{"date":"2025-12-01","val":0.15}}',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `is_del` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除，0:否，1:是',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_unique` (`account_period`, `cash_flow_type`, `duration_type`, `design_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='分账户负债久期汇总表';
