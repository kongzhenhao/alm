-- 久期管理主菜单 SQL
INSERT INTO sys_menu (menu_name,parent_id,order_num,path,component,query,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('久期管理', 0, 10, 'dur', NULL, '', 1, 0, 'M', '0', '0', '', 'chart', 'admin', SYSDATE(), '', NULL, '久期管理菜单');

-- 获取久期管理菜单ID
SET @durMenuId = (SELECT id FROM sys_menu WHERE menu_name = '久期管理' AND parent_id = 0 AND path = 'dur');

-- 1. 负债现金流表菜单 SQL (TB0001)
INSERT INTO sys_menu (menu_name,parent_id,order_num,path,component,query,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('负债现金流', @durMenuId, 1, 'liabilityCashFlow', 'dur/liability/cash/flow/index', '', 1, 0, 'C', '0', '0', 'dur:liability:cash:flow:list', 'table', 'admin', SYSDATE(), '', NULL, '负债现金流菜单');

-- 按钮父菜单ID
SET @parentId = LAST_INSERT_ID();

-- 按钮 SQL
INSERT INTO sys_menu (menu_name,parent_id,order_num,path,component,query,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('负债现金流查询', @parentId, 1, '#', '', '', 1, 0, 'F', '0', '0', 'dur:liability:cash:flow:query', '#', 'admin', SYSDATE(), '', NULL, ''),
('负债现金流新增', @parentId, 2, '#', '', '', 1, 0, 'F', '0', '0', 'dur:liability:cash:flow:add', '#', 'admin', SYSDATE(), '', NULL, ''),
('负债现金流修改', @parentId, 3, '#', '', '', 1, 0, 'F', '0', '0', 'dur:liability:cash:flow:edit', '#', 'admin', SYSDATE(), '', NULL, ''),
('负债现金流删除', @parentId, 4, '#', '', '', 1, 0, 'F', '0', '0', 'dur:liability:cash:flow:remove', '#', 'admin', SYSDATE(), '', NULL, ''),
('负债现金流导出', @parentId, 5, '#', '', '', 1, 0, 'F', '0', '0', 'dur:liability:cash:flow:export', '#', 'admin', SYSDATE(), '', NULL, ''),
('负债现金流导入', @parentId, 6, '#', '', '', 1, 0, 'F', '0', '0', 'dur:liability:cash:flow:import', '#', 'admin', SYSDATE(), '', NULL, '');

-- 2. 折现曲线表菜单 SQL (TB0002)
INSERT INTO sys_menu (menu_name,parent_id,order_num,path,component,query,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('折现曲线', @durMenuId, 2, 'discountCurve', 'dur/discount/curve/index', '', 1, 0, 'C', '0', '0', 'dur:discount:curve:list', 'chart', 'admin', SYSDATE(), '', NULL, '折现曲线菜单');

-- 按钮父菜单ID
SET @parentId = LAST_INSERT_ID();

-- 按钮 SQL
INSERT INTO sys_menu (menu_name,parent_id,order_num,path,component,query,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('折现曲线查询', @parentId, 1, '#', '', '', 1, 0, 'F', '0', '0', 'dur:discount:curve:query', '#', 'admin', SYSDATE(), '', NULL, ''),
('折现曲线新增', @parentId, 2, '#', '', '', 1, 0, 'F', '0', '0', 'dur:discount:curve:add', '#', 'admin', SYSDATE(), '', NULL, ''),
('折现曲线修改', @parentId, 3, '#', '', '', 1, 0, 'F', '0', '0', 'dur:discount:curve:edit', '#', 'admin', SYSDATE(), '', NULL, ''),
('折现曲线删除', @parentId, 4, '#', '', '', 1, 0, 'F', '0', '0', 'dur:discount:curve:remove', '#', 'admin', SYSDATE(), '', NULL, ''),
('折现曲线导出', @parentId, 5, '#', '', '', 1, 0, 'F', '0', '0', 'dur:discount:curve:export', '#', 'admin', SYSDATE(), '', NULL, ''),
('折现曲线导入', @parentId, 6, '#', '', '', 1, 0, 'F', '0', '0', 'dur:discount:curve:import', '#', 'admin', SYSDATE(), '', NULL, '');

-- 3. 折现因子表菜单 SQL (TB0003 - program_design.md)
INSERT INTO sys_menu (menu_name,parent_id,order_num,path,component,query,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('折现因子', @durMenuId, 3, 'discountFactor', 'dur/discount/factor/index', '', 1, 0, 'C', '0', '0', 'dur:discount:factor:list', 'table', 'admin', SYSDATE(), '', NULL, '折现因子菜单');

-- 按钮父菜单ID
SET @parentId = LAST_INSERT_ID();

-- 按钮 SQL
INSERT INTO sys_menu (menu_name,parent_id,order_num,path,component,query,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('折现因子查询', @parentId, 1, '#', '', '', 1, 0, 'F', '0', '0', 'dur:discount:factor:query', '#', 'admin', SYSDATE(), '', NULL, ''),
('折现因子新增', @parentId, 2, '#', '', '', 1, 0, 'F', '0', '0', 'dur:discount:factor:add', '#', 'admin', SYSDATE(), '', NULL, ''),
('折现因子修改', @parentId, 3, '#', '', '', 1, 0, 'F', '0', '0', 'dur:discount:factor:edit', '#', 'admin', SYSDATE(), '', NULL, ''),
('折现因子删除', @parentId, 4, '#', '', '', 1, 0, 'F', '0', '0', 'dur:discount:factor:remove', '#', 'admin', SYSDATE(), '', NULL, ''),
('折现因子导出', @parentId, 5, '#', '', '', 1, 0, 'F', '0', '0', 'dur:discount:factor:export', '#', 'admin', SYSDATE(), '', NULL, ''),
('折现因子导入', @parentId, 6, '#', '', '', 1, 0, 'F', '0', '0', 'dur:discount:factor:import', '#', 'admin', SYSDATE(), '', NULL, '');

-- 4. 关键久期参数表菜单 SQL (TB0003 - key_duration_program_design.md)
INSERT INTO sys_menu (menu_name,parent_id,order_num,path,component,query,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('关键久期参数管理', @durMenuId, 4, 'keyDurationParameter', 'dur/key/duration/parameter/index', '', 1, 0, 'C', '0', '0', 'dur:key:duration:parameter:list', 'table', 'admin', SYSDATE(), '', NULL, '关键久期参数菜单');

-- 按钮父菜单ID
SET @parentId = LAST_INSERT_ID();

-- 按钮 SQL
INSERT INTO sys_menu (menu_name,parent_id,order_num,path,component,query,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('关键久期参数查询', @parentId, 1, '#', '', '', 1, 0, 'F', '0', '0', 'dur:key:duration:parameter:query', '#', 'admin', SYSDATE(), '', NULL, ''),
('关键久期参数新增', @parentId, 2, '#', '', '', 1, 0, 'F', '0', '0', 'dur:key:duration:parameter:add', '#', 'admin', SYSDATE(), '', NULL, ''),
('关键久期参数修改', @parentId, 3, '#', '', '', 1, 0, 'F', '0', '0', 'dur:key:duration:parameter:edit', '#', 'admin', SYSDATE(), '', NULL, ''),
('关键久期参数删除', @parentId, 4, '#', '', '', 1, 0, 'F', '0', '0', 'dur:key:duration:parameter:remove', '#', 'admin', SYSDATE(), '', NULL, ''),
('关键久期参数导出', @parentId, 5, '#', '', '', 1, 0, 'F', '0', '0', 'dur:key:duration:parameter:export', '#', 'admin', SYSDATE(), '', NULL, ''),
('关键久期参数导入', @parentId, 6, '#', '', '', 1, 0, 'F', '0', '0', 'dur:key:duration:parameter:import', '#', 'admin', SYSDATE(), '', NULL, '');

-- 5. 关键久期折现曲线表菜单 SQL (TB0004)
INSERT INTO sys_menu (menu_name,parent_id,order_num,path,component,query,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('关键久期折现曲线', @durMenuId, 5, 'keyDurationDiscountCurve', 'dur/key/duration/discount/curve/index', '', 1, 0, 'C', '0', '0', 'dur:key:duration:discount:curve:list', 'chart', 'admin', SYSDATE(), '', NULL, '关键久期折现曲线菜单');

-- 按钮父菜单ID
SET @parentId = LAST_INSERT_ID();

-- 按钮 SQL
INSERT INTO sys_menu (menu_name,parent_id,order_num,path,component,query,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('关键久期折现曲线查询', @parentId, 1, '#', '', '', 1, 0, 'F', '0', '0', 'dur:key:duration:discount:curve:query', '#', 'admin', SYSDATE(), '', NULL, ''),
('关键久期折现曲线新增', @parentId, 2, '#', '', '', 1, 0, 'F', '0', '0', 'dur:key:duration:discount:curve:add', '#', 'admin', SYSDATE(), '', NULL, ''),
('关键久期折现曲线修改', @parentId, 3, '#', '', '', 1, 0, 'F', '0', '0', 'dur:key:duration:discount:curve:edit', '#', 'admin', SYSDATE(), '', NULL, ''),
('关键久期折现曲线删除', @parentId, 4, '#', '', '', 1, 0, 'F', '0', '0', 'dur:key:duration:discount:curve:remove', '#', 'admin', SYSDATE(), '', NULL, ''),
('关键久期折现曲线导出', @parentId, 5, '#', '', '', 1, 0, 'F', '0', '0', 'dur:key:duration:discount:curve:export', '#', 'admin', SYSDATE(), '', NULL, ''),
('关键久期折现曲线导入', @parentId, 6, '#', '', '', 1, 0, 'F', '0', '0', 'dur:key:duration:discount:curve:import', '#', 'admin', SYSDATE(), '', NULL, '');

-- 6. 关键久期折现因子表菜单 SQL (TB0005 - key_duration_program_design.md)
INSERT INTO sys_menu (menu_name,parent_id,order_num,path,component,query,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('关键久期折现因子', @durMenuId, 6, 'keyDurationDiscountFactors', 'dur/key/duration/discount/factors/index', '', 1, 0, 'C', '0', '0', 'dur:key:duration:discount:factors:list', 'table', 'admin', SYSDATE(), '', NULL, '关键久期折现因子菜单');

-- 按钮父菜单ID
SET @parentId = LAST_INSERT_ID();

-- 按钮 SQL
INSERT INTO sys_menu (menu_name,parent_id,order_num,path,component,query,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('关键久期折现因子查询', @parentId, 1, '#', '', '', 1, 0, 'F', '0', '0', 'dur:key:duration:discount:factors:query', '#', 'admin', SYSDATE(), '', NULL, ''),
('关键久期折现因子新增', @parentId, 2, '#', '', '', 1, 0, 'F', '0', '0', 'dur:key:duration:discount:factors:add', '#', 'admin', SYSDATE(), '', NULL, ''),
('关键久期折现因子修改', @parentId, 3, '#', '', '', 1, 0, 'F', '0', '0', 'dur:key:duration:discount:factors:edit', '#', 'admin', SYSDATE(), '', NULL, ''),
('关键久期折现因子删除', @parentId, 4, '#', '', '', 1, 0, 'F', '0', '0', 'dur:key:duration:discount:factors:remove', '#', 'admin', SYSDATE(), '', NULL, ''),
('关键久期折现因子导出', @parentId, 5, '#', '', '', 1, 0, 'F', '0', '0', 'dur:key:duration:discount:factors:export', '#', 'admin', SYSDATE(), '', NULL, ''),
('关键久期折现因子导入', @parentId, 6, '#', '', '', 1, 0, 'F', '0', '0', 'dur:key:duration:discount:factors:import', '#', 'admin', SYSDATE(), '', NULL, '');

-- 7. 负债现金流汇总表菜单 SQL (TB0005 - program_design.md)
INSERT INTO sys_menu (menu_name,parent_id,order_num,path,component,query,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('负债现金流汇总', @durMenuId, 7, 'liabilityCashFlowSummary', 'dur/liability/cash/flow/summary/index', '', 1, 0, 'C', '0', '0', 'dur:liability:cash:flow:summary:list', 'table', 'admin', SYSDATE(), '', NULL, '负债现金流汇总菜单');

-- 按钮父菜单ID
SET @parentId = LAST_INSERT_ID();

-- 按钮 SQL
INSERT INTO sys_menu (menu_name,parent_id,order_num,path,component,query,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('负债现金流汇总查询', @parentId, 1, '#', '', '', 1, 0, 'F', '0', '0', 'dur:liability:cash:flow:summary:query', '#', 'admin', SYSDATE(), '', NULL, ''),
('负债现金流汇总新增', @parentId, 2, '#', '', '', 1, 0, 'F', '0', '0', 'dur:liability:cash:flow:summary:add', '#', 'admin', SYSDATE(), '', NULL, ''),
('负债现金流汇总修改', @parentId, 3, '#', '', '', 1, 0, 'F', '0', '0', 'dur:liability:cash:flow:summary:edit', '#', 'admin', SYSDATE(), '', NULL, ''),
('负债现金流汇总删除', @parentId, 4, '#', '', '', 1, 0, 'F', '0', '0', 'dur:liability:cash:flow:summary:remove', '#', 'admin', SYSDATE(), '', NULL, ''),
('负债现金流汇总导出', @parentId, 5, '#', '', '', 1, 0, 'F', '0', '0', 'dur:liability:cash:flow:summary:export', '#', 'admin', SYSDATE(), '', NULL, ''),
('负债现金流汇总导入', @parentId, 6, '#', '', '', 1, 0, 'F', '0', '0', 'dur:liability:cash:flow:summary:import', '#', 'admin', SYSDATE(), '', NULL, '');

-- 8. 分中短负债基点价值DV10表菜单 SQL (TB0006)
INSERT INTO sys_menu (menu_name,parent_id,order_num,path,component,query,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('分中短负债基点价值DV10', @durMenuId, 8, 'liabilityDv10ByDuration', 'dur/liability/dv10/by/duration/index', '', 1, 0, 'C', '0', '0', 'dur:liability:dv10:by:duration:list', 'table', 'admin', SYSDATE(), '', NULL, '分中短负债基点价值DV10菜单');

-- 按钮父菜单ID
SET @parentId = LAST_INSERT_ID();

-- 按钮 SQL
INSERT INTO sys_menu (menu_name,parent_id,order_num,path,component,query,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('分中短负债基点价值DV10查询', @parentId, 1, '#', '', '', 1, 0, 'F', '0', '0', 'dur:liability:dv10:by:duration:query', '#', 'admin', SYSDATE(), '', NULL, ''),
('分中短负债基点价值DV10新增', @parentId, 2, '#', '', '', 1, 0, 'F', '0', '0', 'dur:liability:dv10:by:duration:add', '#', 'admin', SYSDATE(), '', NULL, ''),
('分中短负债基点价值DV10修改', @parentId, 3, '#', '', '', 1, 0, 'F', '0', '0', 'dur:liability:dv10:by:duration:edit', '#', 'admin', SYSDATE(), '', NULL, ''),
('分中短负债基点价值DV10删除', @parentId, 4, '#', '', '', 1, 0, 'F', '0', '0', 'dur:liability:dv10:by:duration:remove', '#', 'admin', SYSDATE(), '', NULL, ''),
('分中短负债基点价值DV10导出', @parentId, 5, '#', '', '', 1, 0, 'F', '0', '0', 'dur:liability:dv10:by:duration:export', '#', 'admin', SYSDATE(), '', NULL, ''),
('分中短负债基点价值DV10导入', @parentId, 6, '#', '', '', 1, 0, 'F', '0', '0', 'dur:liability:dv10:by:duration:import', '#', 'admin', SYSDATE(), '', NULL, '');

-- 9. 分账户负债基点价值DV10表菜单 SQL (TB0007 - key_duration_program_design.md)
INSERT INTO sys_menu (menu_name,parent_id,order_num,path,component,query,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('分账户负债基点价值DV10', @durMenuId, 9, 'accountLiabilityDv10', 'dur/account/liability/dv10/index', '', 1, 0, 'C', '0', '0', 'dur:account:liability:dv10:list', 'table', 'admin', SYSDATE(), '', NULL, '分账户负债基点价值DV10菜单');

-- 按钮父菜单ID
SET @parentId = LAST_INSERT_ID();

-- 按钮 SQL
INSERT INTO sys_menu (menu_name,parent_id,order_num,path,component,query,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('分账户负债基点价值DV10查询', @parentId, 1, '#', '', '', 1, 0, 'F', '0', '0', 'dur:account:liability:dv10:query', '#', 'admin', SYSDATE(), '', NULL, ''),
('分账户负债基点价值DV10新增', @parentId, 2, '#', '', '', 1, 0, 'F', '0', '0', 'dur:account:liability:dv10:add', '#', 'admin', SYSDATE(), '', NULL, ''),
('分账户负债基点价值DV10修改', @parentId, 3, '#', '', '', 1, 0, 'F', '0', '0', 'dur:account:liability:dv10:edit', '#', 'admin', SYSDATE(), '', NULL, ''),
('分账户负债基点价值DV10删除', @parentId, 4, '#', '', '', 1, 0, 'F', '0', '0', 'dur:account:liability:dv10:remove', '#', 'admin', SYSDATE(), '', NULL, ''),
('分账户负债基点价值DV10导出', @parentId, 5, '#', '', '', 1, 0, 'F', '0', '0', 'dur:account:liability:dv10:export', '#', 'admin', SYSDATE(), '', NULL, ''),
('分账户负债基点价值DV10导入', @parentId, 6, '#', '', '', 1, 0, 'F', '0', '0', 'dur:account:liability:dv10:import', '#', 'admin', SYSDATE(), '', NULL, '');

-- 10. 负债久期汇总表菜单 SQL (TB0007 - program_design.md)
INSERT INTO sys_menu (menu_name,parent_id,order_num,path,component,query,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('负债久期汇总', @durMenuId, 10, 'liabilityDurationSummary', 'dur/liability/duration/summary/index', '', 1, 0, 'C', '0', '0', 'dur:liability:duration:summary:list', 'table', 'admin', SYSDATE(), '', NULL, '负债久期汇总菜单');

-- 按钮父菜单ID
SET @parentId = LAST_INSERT_ID();

-- 按钮 SQL
INSERT INTO sys_menu (menu_name,parent_id,order_num,path,component,query,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('负债久期汇总查询', @parentId, 1, '#', '', '', 1, 0, 'F', '0', '0', 'dur:liability:duration:summary:query', '#', 'admin', SYSDATE(), '', NULL, ''),
('负债久期汇总新增', @parentId, 2, '#', '', '', 1, 0, 'F', '0', '0', 'dur:liability:duration:summary:add', '#', 'admin', SYSDATE(), '', NULL, ''),
('负债久期汇总修改', @parentId, 3, '#', '', '', 1, 0, 'F', '0', '0', 'dur:liability:duration:summary:edit', '#', 'admin', SYSDATE(), '', NULL, ''),
('负债久期汇总删除', @parentId, 4, '#', '', '', 1, 0, 'F', '0', '0', 'dur:liability:duration:summary:remove', '#', 'admin', SYSDATE(), '', NULL, ''),
('负债久期汇总导出', @parentId, 5, '#', '', '', 1, 0, 'F', '0', '0', 'dur:liability:duration:summary:export', '#', 'admin', SYSDATE(), '', NULL, ''),
('负债久期汇总导入', @parentId, 6, '#', '', '', 1, 0, 'F', '0', '0', 'dur:liability:duration:summary:import', '#', 'admin', SYSDATE(), '', NULL, '');

-- 11. 分账户负债现金流现值汇总表菜单 SQL (TB0008)
INSERT INTO sys_menu (menu_name,parent_id,order_num,path,component,query,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('分账户负债现金流现值汇总', @durMenuId, 11, 'subAccountLiabilityPresentValue', 'dur/sub/account/liability/present/value/index', '', 1, 0, 'C', '0', '0', 'dur:sub:account:liability:present:value:list', 'table', 'admin', SYSDATE(), '', NULL, '分账户负债现金流现值汇总菜单');

-- 按钮父菜单ID
SET @parentId = LAST_INSERT_ID();

-- 按钮 SQL
INSERT INTO sys_menu (menu_name,parent_id,order_num,path,component,query,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('分账户负债现金流现值汇总查询', @parentId, 1, '#', '', '', 1, 0, 'F', '0', '0', 'dur:sub:account:liability:present:value:query', '#', 'admin', SYSDATE(), '', NULL, ''),
('分账户负债现金流现值汇总新增', @parentId, 2, '#', '', '', 1, 0, 'F', '0', '0', 'dur:sub:account:liability:present:value:add', '#', 'admin', SYSDATE(), '', NULL, ''),
('分账户负债现金流现值汇总修改', @parentId, 3, '#', '', '', 1, 0, 'F', '0', '0', 'dur:sub:account:liability:present:value:edit', '#', 'admin', SYSDATE(), '', NULL, ''),
('分账户负债现金流现值汇总删除', @parentId, 4, '#', '', '', 1, 0, 'F', '0', '0', 'dur:sub:account:liability:present:value:remove', '#', 'admin', SYSDATE(), '', NULL, ''),
('分账户负债现金流现值汇总导出', @parentId, 5, '#', '', '', 1, 0, 'F', '0', '0', 'dur:sub:account:liability:present:value:export', '#', 'admin', SYSDATE(), '', NULL, ''),
('分账户负债现金流现值汇总导入', @parentId, 6, '#', '', '', 1, 0, 'F', '0', '0', 'dur:sub:account:liability:present:value:import', '#', 'admin', SYSDATE(), '', NULL, '');

-- 12. 分账户负债久期汇总表菜单 SQL (TB0009)
INSERT INTO sys_menu (menu_name,parent_id,order_num,path,component,query,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('分账户负债久期汇总', @durMenuId, 12, 'subAccountLiabilityDuration', 'dur/sub/account/liability/duration/index', '', 1, 0, 'C', '0', '0', 'dur:sub:account:liability:duration:list', 'table', 'admin', SYSDATE(), '', NULL, '分账户负债久期汇总菜单');

-- 按钮父菜单ID
SET @parentId = LAST_INSERT_ID();

-- 按钮 SQL
INSERT INTO sys_menu (menu_name,parent_id,order_num,path,component,query,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('分账户负债久期汇总查询', @parentId, 1, '#', '', '', 1, 0, 'F', '0', '0', 'dur:sub:account:liability:duration:query', '#', 'admin', SYSDATE(), '', NULL, ''),
('分账户负债久期汇总新增', @parentId, 2, '#', '', '', 1, 0, 'F', '0', '0', 'dur:sub:account:liability:duration:add', '#', 'admin', SYSDATE(), '', NULL, ''),
('分账户负债久期汇总修改', @parentId, 3, '#', '', '', 1, 0, 'F', '0', '0', 'dur:sub:account:liability:duration:edit', '#', 'admin', SYSDATE(), '', NULL, ''),
('分账户负债久期汇总删除', @parentId, 4, '#', '', '', 1, 0, 'F', '0', '0', 'dur:sub:account:liability:duration:remove', '#', 'admin', SYSDATE(), '', NULL, ''),
('分账户负债久期汇总导出', @parentId, 5, '#', '', '', 1, 0, 'F', '0', '0', 'dur:sub:account:liability:duration:export', '#', 'admin', SYSDATE(), '', NULL, ''),
('分账户负债久期汇总导入', @parentId, 6, '#', '', '', 1, 0, 'F', '0', '0', 'dur:sub:account:liability:duration:import', '#', 'admin', SYSDATE(), '', NULL, '');

-- 最低资本管理主菜单 SQL
INSERT INTO sys_menu (menu_name,parent_id,order_num,path,component,query,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('最低资本管理', 0, 20, 'minc', NULL, '', 1, 0, 'M', '0', '0', '', 'money', 'admin', SYSDATE(), '', NULL, '最低资本管理菜单');

-- 获取最低资本管理菜单ID
SET @mincMenuId = (SELECT menu_id FROM sys_menu WHERE menu_name = '最低资本管理' AND parent_id = 0 AND path = 'minc');

-- 分部门最低资本明细表菜单 SQL (TB0001)
INSERT INTO sys_menu (menu_name,parent_id,order_num,path,component,query,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('分部门最低资本明细管理', @mincMenuId, 1, 'deptMincapDetail', 'minc/dept/mincap/detail/index', '', 1, 0, 'C', '0', '0', 'minc:dept:mincap:detail:list', 'table', 'admin', SYSDATE(), '', NULL, '分部门最低资本明细菜单');

-- 按钮父菜单ID
SET @parentId = LAST_INSERT_ID();

-- 按钮 SQL
INSERT INTO sys_menu (menu_name,parent_id,order_num,path,component,query,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('分部门最低资本明细查询', @parentId, 1, '#', '', '', 1, 0, 'F', '0', '0', 'minc:dept:mincap:detail:query', '#', 'admin', SYSDATE(), '', NULL, ''),
('分部门最低资本明细新增', @parentId, 2, '#', '', '', 1, 0, 'F', '0', '0', 'minc:dept:mincap:detail:add', '#', 'admin', SYSDATE(), '', NULL, ''),
('分部门最低资本明细修改', @parentId, 3, '#', '', '', 1, 0, 'F', '0', '0', 'minc:dept:mincap:detail:edit', '#', 'admin', SYSDATE(), '', NULL, ''),
('分部门最低资本明细删除', @parentId, 4, '#', '', '', 1, 0, 'F', '0', '0', 'minc:dept:mincap:detail:remove', '#', 'admin', SYSDATE(), '', NULL, ''),
('分部门最低资本明细导出', @parentId, 5, '#', '', '', 1, 0, 'F', '0', '0', 'minc:dept:mincap:detail:export', '#', 'admin', SYSDATE(), '', NULL, ''),
('分部门最低资本明细导入', @parentId, 6, '#', '', '', 1, 0, 'F', '0', '0', 'minc:dept:mincap:detail:import', '#', 'admin', SYSDATE(), '', NULL, '');

-- 菜单 SQL
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('风险项目金额管理', (SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE menu_name = '最低资本模块' AND parent_id = 0) t), 2, 'amount', 'minc/risk/item/amount/index', 1, 0, 'C', '0', '0', 'minc:risk:item:amount:list', 'table', 'admin', SYSDATE(), '', NULL, '风险项目金额菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('风险项目金额查询', @parentId, 1, '#', '', 1, 0, 'F', '0', '0', 'minc:risk:item:amount:query', '#', 'admin', SYSDATE(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('风险项目金额新增', @parentId, 2, '#', '', 1, 0, 'F', '0', '0', 'minc:risk:item:amount:add', '#', 'admin', SYSDATE(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('风险项目金额修改', @parentId, 3, '#', '', 1, 0, 'F', '0', '0', 'minc:risk:item:amount:edit', '#', 'admin', SYSDATE(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('风险项目金额删除', @parentId, 4, '#', '', 1, 0, 'F', '0', '0', 'minc:risk:item:amount:remove', '#', 'admin', SYSDATE(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('风险项目金额导出', @parentId, 5, '#', '', 1, 0, 'F', '0', '0', 'minc:risk:item:amount:export', '#', 'admin', SYSDATE(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('风险项目金额导入', @parentId, 6, '#', '', 1, 0, 'F', '0', '0', 'minc:risk:item:amount:import', '#', 'admin', SYSDATE(), '', NULL, '');

-- 项目定义表菜单 SQL
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('项目定义管理', @mincMenuId, 3, 'itemDefinition', 'minc/item/definition/index', 1, 0, 'C', '0', '0', 'minc:item:definition:list', 'dict', 'admin', SYSDATE(), '', NULL, '项目定义管理菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('项目定义查询', @parentId, 1, '#', '', 1, 0, 'F', '0', '0', 'minc:item:definition:query', '#', 'admin', SYSDATE(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('项目定义新增', @parentId, 2, '#', '', 1, 0, 'F', '0', '0', 'minc:item:definition:add', '#', 'admin', SYSDATE(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('项目定义修改', @parentId, 3, '#', '', 1, 0, 'F', '0', '0', 'minc:item:definition:edit', '#', 'admin', SYSDATE(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('项目定义删除', @parentId, 4, '#', '', 1, 0, 'F', '0', '0', 'minc:item:definition:remove', '#', 'admin', SYSDATE(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('项目定义导出', @parentId, 5, '#', '', 1, 0, 'F', '0', '0', 'minc:item:definition:export', '#', 'admin', SYSDATE(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('项目定义导入', @parentId, 6, '#', '', 1, 0, 'F', '0', '0', 'minc:item:definition:import', '#', 'admin', SYSDATE(), '', NULL, '');

-- 相关系数表菜单 SQL (TB0004)
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('相关系数管理', @mincMenuId, 4, 'correlationCoef', 'minc/correlation/coef/index', 1, 0, 'C', '0', '0', 'minc:correlation:coef:list', 'table', 'admin', SYSDATE(), '', NULL, '相关系数管理菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('相关系数查询', @parentId, 1, '#', '', 1, 0, 'F', '0', '0', 'minc:correlation:coef:query', '#', 'admin', SYSDATE(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('相关系数新增', @parentId, 2, '#', '', 1, 0, 'F', '0', '0', 'minc:correlation:coef:add', '#', 'admin', SYSDATE(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('相关系数修改', @parentId, 3, '#', '', 1, 0, 'F', '0', '0', 'minc:correlation:coef:edit', '#', 'admin', SYSDATE(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('相关系数删除', @parentId, 4, '#', '', 1, 0, 'F', '0', '0', 'minc:correlation:coef:remove', '#', 'admin', SYSDATE(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('相关系数导出', @parentId, 5, '#', '', 1, 0, 'F', '0', '0', 'minc:correlation:coef:export', '#', 'admin', SYSDATE(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('相关系数导入', @parentId, 6, '#', '', 1, 0, 'F', '0', '0', 'minc:correlation:coef:import', '#', 'admin', SYSDATE(), '', NULL, '');

-- 边际最低资本贡献率表菜单 SQL (TB0006)
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('边际最低资本管理', @mincMenuId, 5, 'marginalCapital', 'minc/marginal/capital/index', 1, 0, 'C', '0', '0', 'minc:marginal:capital:list', 'money', 'admin', SYSDATE(), '', NULL, '边际最低资本管理菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('边际资本查询', @parentId, 1, '#', '', 1, 0, 'F', '0', '0', 'minc:marginal:capital:query', '#', 'admin', SYSDATE(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('边际资本新增', @parentId, 2, '#', '', 1, 0, 'F', '0', '0', 'minc:marginal:capital:add', '#', 'admin', SYSDATE(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('边际资本修改', @parentId, 3, '#', '', 1, 0, 'F', '0', '0', 'minc:marginal:capital:edit', '#', 'admin', SYSDATE(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('边际资本删除', @parentId, 4, '#', '', 1, 0, 'F', '0', '0', 'minc:marginal:capital:remove', '#', 'admin', SYSDATE(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('边际资本导出', @parentId, 5, '#', '', 1, 0, 'F', '0', '0', 'minc:marginal:capital:export', '#', 'admin', SYSDATE(), '', NULL, '');

-- 最低资本明细汇总表菜单 SQL (TB0007)
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('最低资本明细汇总', @mincMenuId, 6, 'minCapitalSummary', 'minc/min/capital/summary/index', 1, 0, 'C', '0', '0', 'minc:min:capital:summary:list', 'table', 'admin', SYSDATE(), '', NULL, '最低资本明细汇总菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('明细汇总查询', @parentId, 1, '#', '', 1, 0, 'F', '0', '0', 'minc:min:capital:summary:query', '#', 'admin', SYSDATE(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('明细汇总新增', @parentId, 2, '#', '', 1, 0, 'F', '0', '0', 'minc:min:capital:summary:add', '#', 'admin', SYSDATE(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('明细汇总修改', @parentId, 3, '#', '', 1, 0, 'F', '0', '0', 'minc:min:capital:summary:edit', '#', 'admin', SYSDATE(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('明细汇总删除', @parentId, 4, '#', '', 1, 0, 'F', '0', '0', 'minc:min:capital:summary:remove', '#', 'admin', SYSDATE(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('明细汇总导出', @parentId, 5, '#', '', 1, 0, 'F', '0', '0', 'minc:min:capital:summary:export', '#', 'admin', SYSDATE(), '', NULL, '');

-- 市场及信用最低资本表菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('市场及信用最低资本管理', @mincMenuId, '7', 'minCapitalByAccount', 'minc/min/capital/by/account/index', 1, 0, 'C', '0', '0', 'minc:min:capital:by:account:list', '#', 'admin', sysdate(), '', null, '市场及信用最低资本表菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('市场及信用最低资本表查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'minc:min:capital:by:account:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('市场及信用最低资本表新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'minc:min:capital:by:account:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('市场及信用最低资本表修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'minc:min:capital:by:account:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('市场及信用最低资本表删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'minc:min:capital:by:account:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('市场及信用最低资本表导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'minc:min:capital:by:account:export',       '#', 'admin', sysdate(), '', null, '');