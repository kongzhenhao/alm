DELIMITER //

CREATE PROCEDURE calculate_liability_dv10_by_duration(IN p_account_period VARCHAR(6))
BEGIN
    -- 声明变量
    DECLARE done INT DEFAULT FALSE;
    DECLARE v_cash_flow_type CHAR(2);
    DECLARE v_design_type VARCHAR(50);
    DECLARE v_short_term_flag CHAR(1);
    DECLARE v_cash_val_set MEDIUMTEXT;
    DECLARE v_key_duration VARCHAR(10);
    DECLARE v_stress_direction CHAR(2);
    DECLARE v_factor_val_set MEDIUMTEXT;
    
    -- 声明游标
    DECLARE cur_cash_flow CURSOR FOR 
        SELECT 
            cf.cash_flow_type,
            cf.design_type,
            cf.is_short_term,
            cf.cash_val_set
        FROM 
            t_dur_liability_cash_flow cf
        WHERE 
            cf.account_period = p_account_period
            AND cf.is_del = 0
            AND cf.duration_type = '03'; -- 关键久期
    
    DECLARE cur_factors CURSOR FOR 
        SELECT 
            f.key_duration,
            f.stress_direction,
            f.factor_val_set
        FROM 
            t_dur_key_duration_discount_factors f
        WHERE 
            f.account_period = p_account_period
            AND f.is_del = 0
            AND f.duration_type = '03'; -- 关键久期
    
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
    
    -- 删除已有数据
    UPDATE t_dur_liability_dv10_by_duration 
    SET is_del = 1 
    WHERE account_period = p_account_period;
    
    -- 创建临时表存储计算结果
    DROP TEMPORARY TABLE IF EXISTS temp_dv10_results;
    CREATE TEMPORARY TABLE temp_dv10_results (
        cash_flow_type CHAR(2),
        design_type VARCHAR(50),
        short_term_flag CHAR(1),
        value_type CHAR(2),
        term_0 DECIMAL(18,10),
        term_0_5 DECIMAL(18,10),
        term_1 DECIMAL(18,10),
        term_2 DECIMAL(18,10),
        term_3 DECIMAL(18,10),
        term_4 DECIMAL(18,10),
        term_5 DECIMAL(18,10),
        term_6 DECIMAL(18,10),
        term_7 DECIMAL(18,10),
        term_8 DECIMAL(18,10),
        term_10 DECIMAL(18,10),
        term_12 DECIMAL(18,10),
        term_15 DECIMAL(18,10),
        term_20 DECIMAL(18,10),
        term_25 DECIMAL(18,10),
        term_30 DECIMAL(18,10),
        term_35 DECIMAL(18,10),
        term_40 DECIMAL(18,10),
        term_45 DECIMAL(18,10),
        term_50 DECIMAL(18,10)
    );
    
    -- 创建索引以加速查询
    CREATE INDEX idx_temp_dv10 ON temp_dv10_results(cash_flow_type, design_type, short_term_flag, value_type);
    
    -- 预先填充结果表
    INSERT INTO temp_dv10_results
    SELECT DISTINCT
        cf.cash_flow_type,
        cf.design_type,
        cf.is_short_term,
        sd.stress_direction,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    FROM
        t_dur_liability_cash_flow cf
    CROSS JOIN
        (SELECT '01' AS stress_direction UNION SELECT '02' UNION SELECT '03') sd
    WHERE
        cf.account_period = p_account_period
        AND cf.is_del = 0;
    
    -- 使用批处理方式计算DV10值
    -- 这里可以使用存储过程的逻辑来计算每个组合的DV10值
    -- 然后更新temp_dv10_results表中的对应记录
    
    -- 最后将计算结果插入到目标表
    INSERT INTO t_dur_liability_dv10_by_duration (
        account_period, cash_flow_type, design_type, short_term_flag, value_type,
        term_0, term_0_5, term_1, term_2, term_3, term_4, term_5, term_6, term_7, term_8,
        term_10, term_12, term_15, term_20, term_25, term_30, term_35, term_40, term_45, term_50,
        create_time, is_del
    )
    SELECT
        p_account_period,
        cash_flow_type,
        CASE
            WHEN design_type = '传统险' THEN '01'
            WHEN design_type = '分红险' THEN '02'
            WHEN design_type = '万能险' THEN '03'
            WHEN design_type = '投连险' THEN '04'
            ELSE SUBSTRING(design_type, 1, 2)
        END,
        short_term_flag,
        value_type,
        term_0, term_0_5, term_1, term_2, term_3, term_4, term_5, term_6, term_7, term_8,
        term_10, term_12, term_15, term_20, term_25, term_30, term_35, term_40, term_45, term_50,
        NOW(),
        0
    FROM
        temp_dv10_results;
    
    -- 清理临时表
    DROP TEMPORARY TABLE IF EXISTS temp_dv10_results;
END //

DELIMITER ;
