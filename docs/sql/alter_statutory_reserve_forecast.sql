-- 修改法定准备金预测表的字段精度

-- 修改法定准备金T1列的精度
ALTER TABLE t_base_statutory_reserve_forecast MODIFY COLUMN statutory_reserve_t1 DECIMAL(28,10) DEFAULT 0 COMMENT '法定准备金T1';

-- 修改法定准备金T2列的精度
ALTER TABLE t_base_statutory_reserve_forecast MODIFY COLUMN statutory_reserve_t2 DECIMAL(28,10) DEFAULT 0 COMMENT '法定准备金T2';

-- 修改法定准备金T3列的精度
ALTER TABLE t_base_statutory_reserve_forecast MODIFY COLUMN statutory_reserve_t3 DECIMAL(28,10) DEFAULT 0 COMMENT '法定准备金T3';
