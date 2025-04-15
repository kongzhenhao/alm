-- 字典类型和字典数据
-- 字符集: utf8
-- 数据库: MySQL 8.0

-- ----------------------------
-- 1、字典类型
-- ----------------------------

-- 现金流类型
INSERT INTO sys_dict_type(dict_name, dict_type, status, create_by, create_time, remark) 
VALUES ('现金流类型', 'cash_flow_type', '0', 'admin', SYSDATE(), '现金流类型列表');

-- 久期类型
INSERT INTO sys_dict_type(dict_name, dict_type, status, create_by, create_time, remark) 
VALUES ('久期类型', 'duration_type', '0', 'admin', SYSDATE(), '久期类型列表');

-- 设计类型
INSERT INTO sys_dict_type(dict_name, dict_type, status, create_by, create_time, remark) 
VALUES ('设计类型', 'design_type', '0', 'admin', SYSDATE(), '设计类型列表');

-- 险种主类
INSERT INTO sys_dict_type(dict_name, dict_type, status, create_by, create_time, remark) 
VALUES ('险种主类', 'insurance_main_type', '0', 'admin', SYSDATE(), '险种主类列表');

-- 险种细类
INSERT INTO sys_dict_type(dict_name, dict_type, status, create_by, create_time, remark) 
VALUES ('险种细类', 'insurance_sub_type', '0', 'admin', SYSDATE(), '险种细类列表');

-- 是否中短期险种
INSERT INTO sys_dict_type(dict_name, dict_type, status, create_by, create_time, remark) 
VALUES ('是否中短期险种', 'is_short_term', '0', 'admin', SYSDATE(), '是否中短期险种列表');

-- ----------------------------
-- 2、字典数据
-- ----------------------------

-- 现金流类型字典数据
INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time, remark) 
VALUES (1, '流入', '01', 'cash_flow_type', 'primary', 'Y', '0', 'admin', SYSDATE(), '现金流入');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time, remark) 
VALUES (2, '流出', '02', 'cash_flow_type', 'info', 'N', '0', 'admin', SYSDATE(), '现金流出');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time, remark) 
VALUES (3, '流出+50bp', '03', 'cash_flow_type', 'warning', 'N', '0', 'admin', SYSDATE(), '现金流出+50bp');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time, remark) 
VALUES (4, '流出-50bp', '04', 'cash_flow_type', 'warning', 'N', '0', 'admin', SYSDATE(), '现金流出-50bp');

-- 久期类型字典数据
INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time, remark) 
VALUES (1, '修正久期', '01', 'duration_type', 'primary', 'Y', '0', 'admin', SYSDATE(), '修正久期');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time, remark) 
VALUES (2, '有效久期', '02', 'duration_type', 'success', 'N', '0', 'admin', SYSDATE(), '有效久期');

-- 是否中短期险种字典数据
INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time, remark) 
VALUES (1, '是', 'Y', 'is_short_term', 'primary', 'Y', '0', 'admin', SYSDATE(), '是');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time, remark) 
VALUES (2, '否', 'N', 'is_short_term', 'danger', 'N', '0', 'admin', SYSDATE(), '否');

-- 设计类型字典数据
-- 注意：设计类型的具体值需要根据业务需求添加，这里仅作为示例
INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time, remark) 
VALUES (1, '标准型', 'STANDARD', 'design_type', 'primary', 'Y', '0', 'admin', SYSDATE(), '标准设计类型');

-- 险种主类字典数据
-- 注意：险种主类的具体值需要根据业务需求添加，这里仅作为示例
INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time, remark) 
VALUES (1, '寿险', 'LIFE', 'insurance_main_type', 'primary', 'Y', '0', 'admin', SYSDATE(), '寿险类');

-- 险种细类字典数据
-- 注意：险种细类的具体值需要根据业务需求添加，这里仅作为示例
INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time, remark) 
VALUES (1, '终身寿险', 'WHOLE_LIFE', 'insurance_sub_type', 'primary', 'Y', '0', 'admin', SYSDATE(), '终身寿险');
