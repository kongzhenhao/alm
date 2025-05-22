-- 分账户有效成本率表
CREATE TABLE IF NOT EXISTS t_cost_account_effective_rate (
    id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    accounting_period VARCHAR(6) NOT NULL COMMENT '账期',
    design_type VARCHAR(2) NOT NULL COMMENT '设计类型',
    effective_cost_rate DECIMAL(10,6) DEFAULT 0 COMMENT '有效成本率',
    cash_flow_set TEXT COMMENT '现金流值集，格式：{"0":{"日期":"2023/06/30","值":"2100"},"1":{"日期":"2023/07/31","值":"2100"}}',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME COMMENT '更新时间',
    is_del TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除，0:否，1:是',
    PRIMARY KEY (id),
    UNIQUE KEY idx_account_effective_rate_unique (accounting_period, design_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分账户有效成本率表';
