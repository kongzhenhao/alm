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

-- 最低资本模块字典数据

-- 新增字典类型：账户类型
INSERT INTO sys_dict_type(dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark)
VALUES ('账户类型', 'minc_account', '0', 'admin', SYSDATE(), '', NULL, '最低资本账户类型');

-- 账户类型字典数据
INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES (1, '传统账户', 'AC001', 'minc_account', NULL, 'primary', 'Y', '0', 'admin', SYSDATE(), '', NULL, '传统型保险账户');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES (2, '分红账户', 'AC002', 'minc_account', NULL, 'success', 'N', '0', 'admin', SYSDATE(), '', NULL, '分红型保险账户');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES (3, '万能账户', 'AC003', 'minc_account', NULL, 'info', 'N', '0', 'admin', SYSDATE(), '', NULL, '万能型保险账户');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES (4, '独立账户', 'AC004', 'minc_account', NULL, 'warning', 'N', '0', 'admin', SYSDATE(), '', NULL, '独立账户');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES (5, '资本补充债账户', 'AC005', 'minc_account', NULL, 'danger', 'N', '0', 'admin', SYSDATE(), '', NULL, '资本补充债账户');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES (6, '普通账户', 'AC006', 'minc_account', NULL, 'default', 'N', '0', 'admin', SYSDATE(), '', NULL, '普通账户，所有账户的汇总');

-- 新增字典类型：风险项目
INSERT INTO sys_dict_type(dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark)
VALUES ('风险项目', 'minc_risk_item', '0', 'admin', SYSDATE(), '', NULL, '最低资本风险项目');




-- 新增字典类型：风险类型
INSERT INTO sys_dict_type(dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark)
VALUES ('风险类型', 'minc_risk_type', '0', 'admin', SYSDATE(), '', NULL, '最低资本风险类型');

-- 风险类型字典数据
INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES (1, '市场风险', '01', 'minc_risk_type', NULL, 'primary', 'N', '0', 'admin', SYSDATE(), '', NULL, '市场风险');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES (2, '信用风险', '02', 'minc_risk_type', NULL, 'danger', 'N', '0', 'admin', SYSDATE(), '', NULL, '信用风险');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES (3, '保险风险', '03', 'minc_risk_type', NULL, 'success', 'N', '0', 'admin', SYSDATE(), '', NULL, '保险风险');

-- 新增字典类型：项目状态
INSERT INTO sys_dict_type(dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark)
VALUES ('项目状态', 'minc_item_status', '0', 'admin', SYSDATE(), '', NULL, '最低资本项目状态');

-- 项目状态字典数据
INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES (1, '有效', '1', 'minc_item_status', NULL, 'primary', 'Y', '0', 'admin', SYSDATE(), '', NULL, '有效状态');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES (2, '无效', '0', 'minc_item_status', NULL, 'danger', 'N', '0', 'admin', SYSDATE(), '', NULL, '无效状态');
