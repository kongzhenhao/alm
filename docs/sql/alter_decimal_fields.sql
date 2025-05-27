-- 修改 t_cost_product_statistics 表中的 statutory_reserve_t0, statutory_reserve_t1, statutory_reserve_t2, statutory_reserve_t3 字段
ALTER TABLE t_cost_product_statistics 
    MODIFY COLUMN statutory_reserve_t0 DECIMAL(30,10) DEFAULT 0 COMMENT '法定准备金T0',
    MODIFY COLUMN statutory_reserve_t1 DECIMAL(30,10) DEFAULT 0 COMMENT '法定准备金T1',
    MODIFY COLUMN statutory_reserve_t2 DECIMAL(30,10) DEFAULT 0 COMMENT '法定准备金T2',
    MODIFY COLUMN statutory_reserve_t3 DECIMAL(30,10) DEFAULT 0 COMMENT '法定准备金T3';

-- 修改 t_cost_business_type_summary 表中的 statutory_reserve_t0, statutory_reserve_t1, statutory_reserve_t2, statutory_reserve_t3 字段
ALTER TABLE t_cost_business_type_summary 
    MODIFY COLUMN statutory_reserve_t0 DECIMAL(30,10) DEFAULT 0 COMMENT '法定准备金T0',
    MODIFY COLUMN statutory_reserve_t1 DECIMAL(30,10) DEFAULT 0 COMMENT '法定准备金T1',
    MODIFY COLUMN statutory_reserve_t2 DECIMAL(30,10) DEFAULT 0 COMMENT '法定准备金T2',
    MODIFY COLUMN statutory_reserve_t3 DECIMAL(30,10) DEFAULT 0 COMMENT '法定准备金T3';

-- 修改 t_cost_account_summary 表中的 statutory_reserve_t0, statutory_reserve_t1, statutory_reserve_t2, statutory_reserve_t3 字段
ALTER TABLE t_cost_account_summary 
    MODIFY COLUMN statutory_reserve_t0 DECIMAL(30,10) DEFAULT 0 COMMENT '法定准备金T0',
    MODIFY COLUMN statutory_reserve_t1 DECIMAL(30,10) DEFAULT 0 COMMENT '法定准备金T1',
    MODIFY COLUMN statutory_reserve_t2 DECIMAL(30,10) DEFAULT 0 COMMENT '法定准备金T2',
    MODIFY COLUMN statutory_reserve_t3 DECIMAL(30,10) DEFAULT 0 COMMENT '法定准备金T3';
