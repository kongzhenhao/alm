-- 字典类型数据
INSERT INTO sys_dict_type(dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark)
VALUES ('现金流类型', 'dur_cash_flow_type', '0', 'admin', SYSDATE(), '', NULL, '负债现金流类型');

INSERT INTO sys_dict_type(dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark)
VALUES ('基点类型', 'dur_bp_type', '0', 'admin', SYSDATE(), '', NULL, '基点类型');

INSERT INTO sys_dict_type(dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark)
VALUES ('久期类型', 'dur_duration_type', '0', 'admin', SYSDATE(), '', NULL, '久期类型');

INSERT INTO sys_dict_type(dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark)
VALUES ('曲线类型', 'dur_curve_type', '0', 'admin', SYSDATE(), '', NULL, '折现曲线类型');

INSERT INTO sys_dict_type(dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark)
VALUES ('因子类型', 'dur_factor_type', '0', 'admin', SYSDATE(), '', NULL, '折现因子类型');

INSERT INTO sys_dict_type(dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark)
VALUES ('是否中短期险种', 'dur_is_short_term', '0', 'admin', SYSDATE(), '', NULL, '是否为中短期险种');

INSERT INTO sys_dict_type(dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark)
VALUES ('是否删除', 'sys_is_del', '0', 'admin', SYSDATE(), '', NULL, '是否删除');

-- 字典数据
-- 现金流类型
INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES (1, '流入', '01', 'dur_cash_flow_type', NULL, 'success', 'Y', '0', 'admin', SYSDATE(), '', NULL, NULL);

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES (2, '流出', '02', 'dur_cash_flow_type', NULL, 'danger', 'N', '0', 'admin', SYSDATE(), '', NULL, NULL);

-- 基点类型
INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES (1, '+50bp', '01', 'dur_bp_type', NULL, 'primary', 'Y', '0', 'admin', SYSDATE(), '', NULL, NULL);

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES (2, '-50bp', '02', 'dur_bp_type', NULL, 'info', 'N', '0', 'admin', SYSDATE(), '', NULL, NULL);

-- 添加 0bp 基点类型
INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES (3, '0bp', '03', 'dur_bp_type', NULL, 'success', 'N', '0', 'admin', SYSDATE(), '', NULL, NULL);

-- 久期类型
INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES (1, '修正久期', '01', 'dur_duration_type', NULL, 'primary', 'Y', '0', 'admin', SYSDATE(), '', NULL, NULL);

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES (2, '有效久期', '02', 'dur_duration_type', NULL, 'success', 'N', '0', 'admin', SYSDATE(), '', NULL, NULL);

-- 曲线类型
INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES (1, '中档', '01', 'dur_curve_type', NULL, 'primary', 'Y', '0', 'admin', SYSDATE(), '', NULL, NULL);

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES (2, '低档', '02', 'dur_curve_type', NULL, 'warning', 'N', '0', 'admin', SYSDATE(), '', NULL, NULL);

-- 因子类型
INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES (1, '中档', '01', 'dur_factor_type', NULL, 'primary', 'Y', '0', 'admin', SYSDATE(), '', NULL, NULL);

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES (2, '低档', '02', 'dur_factor_type', NULL, 'warning', 'N', '0', 'admin', SYSDATE(), '', NULL, NULL);

-- 是否中短期险种
INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES (1, '是', 'Y', 'dur_is_short_term', NULL, 'primary', 'N', '0', 'admin', SYSDATE(), '', NULL, NULL);

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES (2, '否', 'N', 'dur_is_short_term', NULL, 'danger', 'Y', '0', 'admin', SYSDATE(), '', NULL, NULL);

-- 是否删除
INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES (1, '否', '0', 'sys_is_del', NULL, 'primary', 'Y', '0', 'admin', SYSDATE(), '', NULL, NULL);

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES (2, '是', '1', 'sys_is_del', NULL, 'danger', 'N', '0', 'admin', SYSDATE(), '', NULL, NULL);

-- 检查并添加字典数据（分账户负债久期汇总表相关）
-- 现金流类型、基点类型、久期类型已存在，不需要重复添加

-- 新增字典类型：压力方向
INSERT INTO sys_dict_type(dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark)
VALUES ('压力方向', 'dur_stress_direction', '0', 'admin', SYSDATE(), '', NULL, '压力方向');

-- 新增字典类型：现值类型
INSERT INTO sys_dict_type(dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark)
VALUES ('现值类型', 'dur_value_type', '0', 'admin', SYSDATE(), '', NULL, '现值类型');

-- 压力方向字典数据
INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES (1, '上升', '01', 'dur_stress_direction', NULL, 'danger', 'Y', '0', 'admin', SYSDATE(), '', NULL, NULL);

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES (2, '下降', '02', 'dur_stress_direction', NULL, 'success', 'N', '0', 'admin', SYSDATE(), '', NULL, NULL);

-- 现值类型字典数据
INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES (1, '上升', '01', 'dur_value_type', NULL, 'danger', 'N', '0', 'admin', SYSDATE(), '', NULL, NULL);

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES (2, '下降', '02', 'dur_value_type', NULL, 'success', 'N', '0', 'admin', SYSDATE(), '', NULL, NULL);

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES (3, '净值', '03', 'dur_value_type', NULL, 'primary', 'Y', '0', 'admin', SYSDATE(), '', NULL, NULL);

-- 添加久期类型：关键久期
INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES (3, '关键久期', '03', 'dur_duration_type', NULL, 'info', 'N', '0', 'admin', SYSDATE(), '', NULL, NULL);
