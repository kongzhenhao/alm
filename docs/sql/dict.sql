-- 生成字典数据
-- 字符集: utf8
-- 数据库: MySQL 8.0

-- 现金流类型字典
INSERT INTO sys_dict_type (dict_name, dict_type, status, create_by, create_time, remark) 
VALUES ('现金流类型', 'cash_flow_type', '0', 'admin', NOW(), '现金流类型字典');

INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time, remark) 
VALUES (1, '流入', '01', 'cash_flow_type', 'default', 'Y', '0', 'admin', NOW(), NULL);

INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time, remark) 
VALUES (2, '流出', '02', 'cash_flow_type', 'default', 'N', '0', 'admin', NOW(), NULL);

INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time, remark) 
VALUES (3, '流出+50bp', '03', 'cash_flow_type', 'default', 'N', '0', 'admin', NOW(), NULL);

INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time, remark) 
VALUES (4, '流出-50bp', '04', 'cash_flow_type', 'default', 'N', '0', 'admin', NOW(), NULL);

-- 久期类型字典
INSERT INTO sys_dict_type (dict_name, dict_type, status, create_by, create_time, remark) 
VALUES ('久期类型', 'duration_type', '0', 'admin', NOW(), '久期类型字典');

INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time, remark) 
VALUES (1, '修正久期', '01', 'duration_type', 'default', 'Y', '0', 'admin', NOW(), NULL);

INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time, remark) 
VALUES (2, '有效久期', '02', 'duration_type', 'default', 'N', '0', 'admin', NOW(), NULL);

-- 曲线类型字典
INSERT INTO sys_dict_type (dict_name, dict_type, status, create_by, create_time, remark) 
VALUES ('曲线类型', 'curve_type', '0', 'admin', NOW(), '曲线类型字典');

INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time, remark) 
VALUES (1, '中档', '01', 'curve_type', 'default', 'Y', '0', 'admin', NOW(), NULL);

INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time, remark) 
VALUES (2, '低档', '02', 'curve_type', 'default', 'N', '0', 'admin', NOW(), NULL);

-- 因子类型字典
INSERT INTO sys_dict_type (dict_name, dict_type, status, create_by, create_time, remark) 
VALUES ('因子类型', 'factor_type', '0', 'admin', NOW(), '因子类型字典');

INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time, remark) 
VALUES (1, '中档', '01', 'factor_type', 'default', 'Y', '0', 'admin', NOW(), NULL);

INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time, remark) 
VALUES (2, '低档', '02', 'factor_type', 'default', 'N', '0', 'admin', NOW(), NULL);

-- 是否为中短期险种字典
INSERT INTO sys_dict_type (dict_name, dict_type, status, create_by, create_time, remark) 
VALUES ('是否为中短期险种', 'is_short_term', '0', 'admin', NOW(), '是否为中短期险种字典');

INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time, remark) 
VALUES (1, '是', 'Y', 'is_short_term', 'success', 'N', '0', 'admin', NOW(), NULL);

INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time, remark) 
VALUES (2, '否', 'N', 'is_short_term', 'danger', 'Y', '0', 'admin', NOW(), NULL);

-- 是否删除字典
INSERT INTO sys_dict_type (dict_name, dict_type, status, create_by, create_time, remark) 
VALUES ('是否删除', 'is_del', '0', 'admin', NOW(), '是否删除字典');

INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time, remark) 
VALUES (1, '否', '0', 'is_del', 'success', 'Y', '0', 'admin', NOW(), NULL);

INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time, remark) 
VALUES (2, '是', '1', 'is_del', 'danger', 'N', '0', 'admin', NOW(), NULL);
