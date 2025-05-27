-- 成本管理模块字典数据（基于若依框架）

-- 1. 长短期标识字典类型
INSERT INTO sys_dict_type(dict_name, dict_type, status, create_by, create_time, remark) 
VALUES ('长短期标识', 'cost_term_type', '0', 'admin', SYSDATE(), '长短期标识字典');

-- 长短期标识字典数据
INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (1, '长期', 'L', 'cost_term_type', '', 'primary', 'Y', '0', 'admin', SYSDATE(), '长期保险');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (2, '短期', 'S', 'cost_term_type', '', 'info', 'N', '0', 'admin', SYSDATE(), '短期保险');

-- 2. 是否中短标识字典类型
INSERT INTO sys_dict_type(dict_name, dict_type, status, create_by, create_time, remark) 
VALUES ('是否中短', 'cost_short_term_flag', '0', 'admin', SYSDATE(), '是否中短标识字典');

-- 是否中短标识字典数据
INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (1, '是', 'Y', 'cost_short_term_flag', '', 'warning', 'N', '0', 'admin', SYSDATE(), '中短期产品');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (2, '否', 'N', 'cost_short_term_flag', '', 'success', 'Y', '0', 'admin', SYSDATE(), '非中短期产品');

-- 3. 设计类型字典类型
INSERT INTO sys_dict_type(dict_name, dict_type, status, create_by, create_time, remark) 
VALUES ('设计类型', 'cost_design_type', '0', 'admin', SYSDATE(), '产品设计类型字典');

-- 设计类型字典数据
INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (1, '传统险', '传统险', 'cost_design_type', '', 'primary', 'Y', '0', 'admin', SYSDATE(), '传统型保险产品');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (2, '分红险', '分红险', 'cost_design_type', '', 'success', 'N', '0', 'admin', SYSDATE(), '分红型保险产品');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (3, '万能险', '万能险', 'cost_design_type', '', 'info', 'N', '0', 'admin', SYSDATE(), '万能型保险产品');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (4, '投连险', '投连险', 'cost_design_type', '', 'warning', 'N', '0', 'admin', SYSDATE(), '投资连结保险产品');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (5, '普通账户', '普通账户', 'cost_design_type', '', 'danger', 'N', '0', 'admin', SYSDATE(), '普通账户汇总');

-- 4. 险种主类字典类型
INSERT INTO sys_dict_type(dict_name, dict_type, status, create_by, create_time, remark) 
VALUES ('险种主类', 'cost_insurance_main_type', '0', 'admin', SYSDATE(), '险种主类字典');

-- 险种主类字典数据
INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (1, '长期寿险', '长期寿险', 'cost_insurance_main_type', '', 'primary', 'Y', '0', 'admin', SYSDATE(), '长期寿险产品');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (2, '长期健康险', '长期健康险', 'cost_insurance_main_type', '', 'success', 'N', '0', 'admin', SYSDATE(), '长期健康险产品');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (3, '长期意外险', '长期意外险', 'cost_insurance_main_type', '', 'info', 'N', '0', 'admin', SYSDATE(), '长期意外险产品');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (4, '短期寿险', '短期寿险', 'cost_insurance_main_type', '', 'warning', 'N', '0', 'admin', SYSDATE(), '短期寿险产品');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (5, '短期健康险', '短期健康险', 'cost_insurance_main_type', '', 'danger', 'N', '0', 'admin', SYSDATE(), '短期健康险产品');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (6, '短期意外险', '短期意外险', 'cost_insurance_main_type', '', 'default', 'N', '0', 'admin', SYSDATE(), '短期意外险产品');

-- 5. 险种细类字典类型
INSERT INTO sys_dict_type(dict_name, dict_type, status, create_by, create_time, remark) 
VALUES ('险种细类', 'cost_insurance_sub_type', '0', 'admin', SYSDATE(), '险种细类字典');

-- 险种细类字典数据
INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (1, '年金险', '年金险', 'cost_insurance_sub_type', '', 'primary', 'N', '0', 'admin', SYSDATE(), '年金保险');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (2, '两全险', '两全险', 'cost_insurance_sub_type', '', 'success', 'N', '0', 'admin', SYSDATE(), '两全保险');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (3, '附加两全险', '附加两全险', 'cost_insurance_sub_type', '', 'info', 'N', '0', 'admin', SYSDATE(), '附加两全保险');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (4, '终身寿险', '终身寿险', 'cost_insurance_sub_type', '', 'warning', 'N', '0', 'admin', SYSDATE(), '终身寿险');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (5, '定期寿险', '定期寿险', 'cost_insurance_sub_type', '', 'danger', 'N', '0', 'admin', SYSDATE(), '定期寿险');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (6, '长期医疗险', '长期医疗险', 'cost_insurance_sub_type', '', 'primary', 'N', '0', 'admin', SYSDATE(), '长期医疗保险');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (7, '长期重疾险', '长期重疾险', 'cost_insurance_sub_type', '', 'success', 'N', '0', 'admin', SYSDATE(), '长期重疾保险');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (8, '长期防癌险', '长期防癌险', 'cost_insurance_sub_type', '', 'info', 'N', '0', 'admin', SYSDATE(), '长期防癌保险');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (9, '长期护理险', '长期护理险', 'cost_insurance_sub_type', '', 'warning', 'N', '0', 'admin', SYSDATE(), '长期护理保险');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (10, '长期意外伤害险', '长期意外伤害险', 'cost_insurance_sub_type', '', 'danger', 'N', '0', 'admin', SYSDATE(), '长期意外伤害保险');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (11, '长期意外医疗险', '长期意外医疗险', 'cost_insurance_sub_type', '', 'default', 'N', '0', 'admin', SYSDATE(), '长期意外医疗保险');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (12, '短期定期寿险', '短期定期寿险', 'cost_insurance_sub_type', '', 'primary', 'N', '0', 'admin', SYSDATE(), '短期定期寿险');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (13, '短期医疗险', '短期医疗险', 'cost_insurance_sub_type', '', 'success', 'N', '0', 'admin', SYSDATE(), '短期医疗保险');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (14, '短期重疾险', '短期重疾险', 'cost_insurance_sub_type', '', 'info', 'N', '0', 'admin', SYSDATE(), '短期重疾保险');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (15, '短期防癌险', '短期防癌险', 'cost_insurance_sub_type', '', 'warning', 'N', '0', 'admin', SYSDATE(), '短期防癌保险');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (16, '短期意外伤害险', '短期意外伤害险', 'cost_insurance_sub_type', '', 'danger', 'N', '0', 'admin', SYSDATE(), '短期意外伤害保险');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (17, '短期意外医疗险', '短期意外医疗险', 'cost_insurance_sub_type', '', 'default', 'N', '0', 'admin', SYSDATE(), '短期意外医疗保险');

-- 6. 业务类型字典类型
INSERT INTO sys_dict_type(dict_name, dict_type, status, create_by, create_time, remark) 
VALUES ('业务类型', 'cost_business_type', '0', 'admin', SYSDATE(), '业务类型字典');

-- 业务类型字典数据
INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (1, '有效业务', '有效业务', 'cost_business_type', '', 'primary', 'Y', '0', 'admin', SYSDATE(), '有效业务数据');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (2, '新业务', '新业务', 'cost_business_type', '', 'success', 'N', '0', 'admin', SYSDATE(), '新业务数据');

-- 7. 情景名称字典类型
INSERT INTO sys_dict_type(dict_name, dict_type, status, create_by, create_time, remark) 
VALUES ('情景名称', 'cost_scenario_name', '0', 'admin', SYSDATE(), '情景名称字典');

-- 情景名称字典数据
INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (1, '基本情景', '基本情景', 'cost_scenario_name', '', 'primary', 'Y', '0', 'admin', SYSDATE(), '基本情景数据');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (2, '压力情景一', '压力情景一', 'cost_scenario_name', '', 'success', 'N', '0', 'admin', SYSDATE(), '压力情景一数据');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (3, '压力情景二', '压力情景二', 'cost_scenario_name', '', 'info', 'N', '0', 'admin', SYSDATE(), '压力情景二数据');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (4, '压力情景三', '压力情景三', 'cost_scenario_name', '', 'warning', 'N', '0', 'admin', SYSDATE(), '压力情景三数据');

-- 8. 现金流类型字典类型
INSERT INTO sys_dict_type(dict_name, dict_type, status, create_by, create_time, remark) 
VALUES ('现金流类型', 'cost_cash_flow_type', '0', 'admin', SYSDATE(), '现金流类型字典');

-- 现金流类型字典数据
INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (1, '流入', '流入', 'cost_cash_flow_type', '', 'success', 'N', '0', 'admin', SYSDATE(), '现金流入');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (2, '流出', '流出', 'cost_cash_flow_type', '', 'danger', 'N', '0', 'admin', SYSDATE(), '现金流出');

-- 9. 基点类型字典类型
INSERT INTO sys_dict_type(dict_name, dict_type, status, create_by, create_time, remark) 
VALUES ('基点类型', 'cost_basis_point_type', '0', 'admin', SYSDATE(), '基点类型字典');

-- 基点类型字典数据
INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (1, '基准情景', '0bp', 'cost_basis_point_type', '', 'primary', 'Y', '0', 'admin', SYSDATE(), '基准情景(0bp)');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (2, '上升50bp', '+50bp', 'cost_basis_point_type', '', 'success', 'N', '0', 'admin', SYSDATE(), '利率上升50bp');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (3, '下降50bp', '-50bp', 'cost_basis_point_type', '', 'danger', 'N', '0', 'admin', SYSDATE(), '利率下降50bp');

-- 10. 久期类型字典类型
INSERT INTO sys_dict_type(dict_name, dict_type, status, create_by, create_time, remark) 
VALUES ('久期类型', 'cost_duration_type', '0', 'admin', SYSDATE(), '久期类型字典');

-- 久期类型字典数据
INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (1, '修正久期', '修正久期', 'cost_duration_type', '', 'primary', 'Y', '0', 'admin', SYSDATE(), '修正久期计算');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (2, '有效久期', '有效久期', 'cost_duration_type', '', 'success', 'N', '0', 'admin', SYSDATE(), '有效久期计算');

-- 11. 新业务标识字典类型
INSERT INTO sys_dict_type(dict_name, dict_type, status, create_by, create_time, remark) 
VALUES ('新业务标识', 'cost_new_business_flag', '0', 'admin', SYSDATE(), '新业务标识字典');

-- 新业务标识字典数据
INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (1, '是', 'Y', 'cost_new_business_flag', '', 'primary', 'Y', '0', 'admin', SYSDATE(), '新业务');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (2, '否', 'N', 'cost_new_business_flag', '', 'danger', 'N', '0', 'admin', SYSDATE(), '非新业务');

-- 12. 报监管中短标识字典类型
INSERT INTO sys_dict_type(dict_name, dict_type, status, create_by, create_time, remark) 
VALUES ('报监管中短标识', 'cost_reg_mid_id', '0', 'admin', SYSDATE(), '报监管中短标识字典');

-- 报监管中短标识字典数据
INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (1, '是', 'Y', 'cost_reg_mid_id', '', 'warning', 'N', '0', 'admin', SYSDATE(), '报监管中短');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (2, '否', 'N', 'cost_reg_mid_id', '', 'success', 'Y', '0', 'admin', SYSDATE(), '非报监管中短');
