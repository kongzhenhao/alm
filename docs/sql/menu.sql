-- 久期管理主菜单 SQL
INSERT INTO alm.sys_menu (menu_name,parent_id,order_num,`path`,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('久期管理', 0, 10, 'dur', NULL, '', '', 1, 0, 'M', '0', '0', '', 'chart', 'admin', SYSDATE(), '', NULL, '久期管理菜单');

-- 获取久期管理菜单ID
SET @durMenuId = (SELECT id FROM alm.sys_menu WHERE menu_name = '久期管理' AND parent_id = 0 AND `path` = 'dur');

-- 折现曲线菜单 SQL
INSERT INTO alm.sys_menu (menu_name,parent_id,order_num,`path`,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('折现曲线', @durMenuId, 1, 'discountCurve', 'dur/discount/curve/index', '', '', 1, 0, 'C', '0', '0', 'dur:discount:curve:list', 'chart', 'admin', SYSDATE(), '', NULL, '折现曲线菜单');

-- 按钮父菜单ID
SET @parentId = LAST_INSERT_ID();

-- 按钮 SQL
INSERT INTO alm.sys_menu (menu_name,parent_id,order_num,`path`,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('折现曲线查询', @parentId, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'dur:discount:curve:query', '#', 'admin', SYSDATE(), '', NULL, ''),
('折现曲线新增', @parentId, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'dur:discount:curve:add', '#', 'admin', SYSDATE(), '', NULL, ''),
('折现曲线修改', @parentId, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'dur:discount:curve:edit', '#', 'admin', SYSDATE(), '', NULL, ''),
('折现曲线删除', @parentId, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'dur:discount:curve:remove', '#', 'admin', SYSDATE(), '', NULL, ''),
('折现曲线导出', @parentId, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'dur:discount:curve:export', '#', 'admin', SYSDATE(), '', NULL, ''),
('折现曲线导入', @parentId, 6, '#', '', '', '', 1, 0, 'F', '0', '0', 'dur:discount:curve:import', '#', 'admin', SYSDATE(), '', NULL, '');

-- 负债现金流汇总菜单 SQL
-- 使用久期管理菜单ID
INSERT INTO alm.sys_menu (menu_name,parent_id,order_num,`path`,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('负债现金流汇总', @durMenuId, 2, 'liabilityCashFlowSummary', 'dur/liability/cash/flow/summary/index', '', '', 1, 0, 'C', '0', '0', 'dur:liability:cash:flow:summary:list', 'table', 'admin', SYSDATE(), '', NULL, '负债现金流汇总菜单');

SET @parentId = LAST_INSERT_ID();

INSERT INTO alm.sys_menu (menu_name,parent_id,order_num,`path`,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('负债现金流汇总查询',@parentId,1,'','','','',1,0,'F','0','0','dur:liability:cash:flow:summary:query','#','admin',SYSDATE(),'',NULL,''),
('负债现金流汇总新增',@parentId,2,'','','','',1,0,'F','0','0','dur:liability:cash:flow:summary:add','#','admin',SYSDATE(),'',NULL,''),
('负债现金流汇总修改',@parentId,3,'','','','',1,0,'F','0','0','dur:liability:cash:flow:summary:edit','#','admin',SYSDATE(),'',NULL,''),
('负债现金流汇总删除',@parentId,4,'','','','',1,0,'F','0','0','dur:liability:cash:flow:summary:remove','#','admin',SYSDATE(),'',NULL,''),
('负债现金流汇总导出',@parentId,5,'','','','',1,0,'F','0','0','dur:liability:cash:flow:summary:export','#','admin',SYSDATE(),'',NULL,''),
('负债现金流汇总导入',@parentId,6,'','','','',1,0,'F','0','0','dur:liability:cash:flow:summary:import','#','admin',SYSDATE(),'',NULL,'');

-- 负债久期汇总菜单 SQL
-- 使用久期管理菜单ID
INSERT INTO alm.sys_menu (menu_name,parent_id,order_num,`path`,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('负债久期汇总', @durMenuId, 3, 'liabilityDurationSummary', 'dur/liability/duration/summary/index', '', '', 1, 0, 'C', '0', '0', 'dur:liability:duration:summary:list', 'table', 'admin', SYSDATE(), '', NULL, '负债久期汇总菜单');

SET @parentId = LAST_INSERT_ID();

INSERT INTO alm.sys_menu (menu_name,parent_id,order_num,`path`,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('负债久期汇总查询',@parentId,1,'','','','',1,0,'F','0','0','dur:liability:duration:summary:query','#','admin',SYSDATE(),'',NULL,''),
('负债久期汇总新增',@parentId,2,'','','','',1,0,'F','0','0','dur:liability:duration:summary:add','#','admin',SYSDATE(),'',NULL,''),
('负债久期汇总修改',@parentId,3,'','','','',1,0,'F','0','0','dur:liability:duration:summary:edit','#','admin',SYSDATE(),'',NULL,''),
('负债久期汇总删除',@parentId,4,'','','','',1,0,'F','0','0','dur:liability:duration:summary:remove','#','admin',SYSDATE(),'',NULL,''),
('负债久期汇总导出',@parentId,5,'','','','',1,0,'F','0','0','dur:liability:duration:summary:export','#','admin',SYSDATE(),'',NULL,''),
('负债久期汇总导入',@parentId,6,'','','','',1,0,'F','0','0','dur:liability:duration:summary:import','#','admin',SYSDATE(),'',NULL,'');

-- 分账户负债现金流现值汇总菜单 SQL
-- 使用久期管理菜单ID
INSERT INTO alm.sys_menu (menu_name,parent_id,order_num,`path`,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('分账户负债现金流现值汇总', @durMenuId, 4, 'subAccountLiabilityPresentValue', 'dur/sub/account/liability/present/value/index', '', '', 1, 0, 'C', '0', '0', 'dur:sub:account:liability:present:value:list', 'table', 'admin', SYSDATE(), '', NULL, '分账户负债现金流现值汇总菜单');

SET @parentId = LAST_INSERT_ID();

INSERT INTO alm.sys_menu (menu_name,parent_id,order_num,`path`,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('分账户负债现金流现值汇总查询',@parentId,1,'','','','',1,0,'F','0','0','dur:sub:account:liability:present:value:query','#','admin',SYSDATE(),'',NULL,''),
('分账户负债现金流现值汇总新增',@parentId,2,'','','','',1,0,'F','0','0','dur:sub:account:liability:present:value:add','#','admin',SYSDATE(),'',NULL,''),
('分账户负债现金流现值汇总修改',@parentId,3,'','','','',1,0,'F','0','0','dur:sub:account:liability:present:value:edit','#','admin',SYSDATE(),'',NULL,''),
('分账户负债现金流现值汇总删除',@parentId,4,'','','','',1,0,'F','0','0','dur:sub:account:liability:present:value:remove','#','admin',SYSDATE(),'',NULL,''),
('分账户负债现金流现值汇总导出',@parentId,5,'','','','',1,0,'F','0','0','dur:sub:account:liability:present:value:export','#','admin',SYSDATE(),'',NULL,''),
('分账户负债现金流现值汇总导入',@parentId,6,'','','','',1,0,'F','0','0','dur:sub:account:liability:present:value:import','#','admin',SYSDATE(),'',NULL,'');

-- 分账户负债久期汇总菜单 SQL
-- 使用久期管理菜单ID
INSERT INTO alm.sys_menu (menu_name,parent_id,order_num,`path`,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('分账户负债久期汇总', @durMenuId, 5, 'subAccountLiabilityDuration', 'dur/sub/account/liability/duration/index', '', '', 1, 0, 'C', '0', '0', 'dur:sub:account:liability:duration:list', 'table', 'admin', SYSDATE(), '', NULL, '分账户负债久期汇总菜单');

-- 按钮父菜单ID
SET @parentId = LAST_INSERT_ID();

-- 按钮 SQL
INSERT INTO alm.sys_menu (menu_name,parent_id,order_num,`path`,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('分账户负债久期汇总查询', @parentId, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'dur:sub:account:liability:duration:query', '#', 'admin', SYSDATE(), '', NULL, ''),
('分账户负债久期汇总新增', @parentId, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'dur:sub:account:liability:duration:add', '#', 'admin', SYSDATE(), '', NULL, ''),
('分账户负债久期汇总修改', @parentId, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'dur:sub:account:liability:duration:edit', '#', 'admin', SYSDATE(), '', NULL, ''),
('分账户负债久期汇总删除', @parentId, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'dur:sub:account:liability:duration:remove', '#', 'admin', SYSDATE(), '', NULL, ''),
('分账户负债久期汇总导出', @parentId, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'dur:sub:account:liability:duration:export', '#', 'admin', SYSDATE(), '', NULL, ''),
('分账户负债久期汇总导入', @parentId, 6, '#', '', '', '', 1, 0, 'F', '0', '0', 'dur:sub:account:liability:duration:import', '#', 'admin', SYSDATE(), '', NULL, '');

-- 关键久期参数菜单 SQL
-- 使用久期管理菜单ID
INSERT INTO alm.sys_menu (menu_name,parent_id,order_num,`path`,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('关键久期参数', @durMenuId, 6, 'keyDurationParameter', 'dur/key/duration/parameter/index', '', '', 1, 0, 'C', '0', '0', 'dur:key:duration:parameter:list', 'table', 'admin', SYSDATE(), '', NULL, '关键久期参数菜单');

-- 按钮父菜单ID
SET @parentId = LAST_INSERT_ID();

-- 按钮 SQL
INSERT INTO alm.sys_menu (menu_name,parent_id,order_num,`path`,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('关键久期参数查询', @parentId, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'dur:key:duration:parameter:query', '#', 'admin', SYSDATE(), '', NULL, ''),
('关键久期参数新增', @parentId, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'dur:key:duration:parameter:add', '#', 'admin', SYSDATE(), '', NULL, ''),
('关键久期参数修改', @parentId, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'dur:key:duration:parameter:edit', '#', 'admin', SYSDATE(), '', NULL, ''),
('关键久期参数删除', @parentId, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'dur:key:duration:parameter:remove', '#', 'admin', SYSDATE(), '', NULL, ''),
('关键久期参数导出', @parentId, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'dur:key:duration:parameter:export', '#', 'admin', SYSDATE(), '', NULL, ''),
('关键久期参数导入', @parentId, 6, '#', '', '', '', 1, 0, 'F', '0', '0', 'dur:key:duration:parameter:import', '#', 'admin', SYSDATE(), '', NULL, '');

-- 关键久期折现曲线菜单 SQL
-- 使用久期管理菜单ID
INSERT INTO alm.sys_menu (menu_name,parent_id,order_num,`path`,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('关键久期折现曲线', @durMenuId, 7, 'keyDurationDiscountCurve', 'dur/key/duration/discount/curve/index', '', '', 1, 0, 'C', '0', '0', 'dur:key:duration:discount:curve:list', 'table', 'admin', SYSDATE(), '', NULL, '关键久期折现曲线菜单');

-- 按钮父菜单ID
SET @parentId = LAST_INSERT_ID();

-- 按钮 SQL
INSERT INTO alm.sys_menu (menu_name,parent_id,order_num,`path`,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('关键久期折现曲线查询', @parentId, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'dur:key:duration:discount:curve:query', '#', 'admin', SYSDATE(), '', NULL, ''),
('关键久期折现曲线新增', @parentId, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'dur:key:duration:discount:curve:add', '#', 'admin', SYSDATE(), '', NULL, ''),
('关键久期折现曲线修改', @parentId, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'dur:key:duration:discount:curve:edit', '#', 'admin', SYSDATE(), '', NULL, ''),
('关键久期折现曲线删除', @parentId, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'dur:key:duration:discount:curve:remove', '#', 'admin', SYSDATE(), '', NULL, ''),
('关键久期折现曲线导出', @parentId, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'dur:key:duration:discount:curve:export', '#', 'admin', SYSDATE(), '', NULL, ''),
('关键久期折现曲线导入', @parentId, 6, '#', '', '', '', 1, 0, 'F', '0', '0', 'dur:key:duration:discount:curve:import', '#', 'admin', SYSDATE(), '', NULL, '');

-- 关键久期折现因子表菜单 SQL
-- 使用久期管理菜单ID
INSERT INTO alm.sys_menu (menu_name,parent_id,order_num,`path`,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('关键久期折现因子表', @durMenuId, 8, 'keyDurationDiscountFactors', 'dur/key/duration/discount/factors/index', '', '', 1, 0, 'C', '0', '0', 'dur:key:duration:discount:factors:list', 'table', 'admin', SYSDATE(), '', NULL, '关键久期折现因子表菜单');

-- 按钮父菜单ID
SET @parentId = LAST_INSERT_ID();

-- 按钮 SQL
INSERT INTO alm.sys_menu (menu_name,parent_id,order_num,`path`,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('关键久期折现因子表查询', @parentId, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'dur:key:duration:discount:factors:query', '#', 'admin', SYSDATE(), '', NULL, ''),
('关键久期折现因子表新增', @parentId, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'dur:key:duration:discount:factors:add', '#', 'admin', SYSDATE(), '', NULL, ''),
('关键久期折现因子表修改', @parentId, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'dur:key:duration:discount:factors:edit', '#', 'admin', SYSDATE(), '', NULL, ''),
('关键久期折现因子表删除', @parentId, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'dur:key:duration:discount:factors:remove', '#', 'admin', SYSDATE(), '', NULL, ''),
('关键久期折现因子表导出', @parentId, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'dur:key:duration:discount:factors:export', '#', 'admin', SYSDATE(), '', NULL, ''),
('关键久期折现因子表导入', @parentId, 6, '#', '', '', '', 1, 0, 'F', '0', '0', 'dur:key:duration:discount:factors:import', '#', 'admin', SYSDATE(), '', NULL, '');

-- 分中短负债基点价值DV10表菜单 SQL
-- 使用久期管理菜单ID
INSERT INTO alm.sys_menu (menu_name,parent_id,order_num,`path`,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('分中短负债基点价值DV10表', @durMenuId, 9, 'liabilityDv10ByDuration', 'dur/liability/dv10/by/duration/index', '', '', 1, 0, 'C', '0', '0', 'dur:liability:dv10:by:duration:list', 'table', 'admin', SYSDATE(), '', NULL, '分中短负债基点价值DV10表菜单');

-- 按钮父菜单ID
SET @parentId = LAST_INSERT_ID();

-- 按钮 SQL
INSERT INTO alm.sys_menu (menu_name,parent_id,order_num,`path`,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('分中短负债基点价值DV10表查询', @parentId, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'dur:liability:dv10:by:duration:query', '#', 'admin', SYSDATE(), '', NULL, ''),
('分中短负债基点价值DV10表新增', @parentId, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'dur:liability:dv10:by:duration:add', '#', 'admin', SYSDATE(), '', NULL, ''),
('分中短负债基点价值DV10表修改', @parentId, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'dur:liability:dv10:by:duration:edit', '#', 'admin', SYSDATE(), '', NULL, ''),
('分中短负债基点价值DV10表删除', @parentId, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'dur:liability:dv10:by:duration:remove', '#', 'admin', SYSDATE(), '', NULL, ''),
('分中短负债基点价值DV10表导出', @parentId, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'dur:liability:dv10:by:duration:export', '#', 'admin', SYSDATE(), '', NULL, ''),
('分中短负债基点价值DV10表导入', @parentId, 6, '#', '', '', '', 1, 0, 'F', '0', '0', 'dur:liability:dv10:by:duration:import', '#', 'admin', SYSDATE(), '', NULL, '');

-- 分账户负债基点价值DV10表菜单 SQL
-- 使用久期管理菜单ID
INSERT INTO alm.sys_menu (menu_name,parent_id,order_num,`path`,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('分账户负债基点价值DV10表', @durMenuId, 10, 'accountLiabilityDv10', 'dur/account/liability/dv10/index', '', '', 1, 0, 'C', '0', '0', 'dur:account:liability:dv10:list', 'table', 'admin', SYSDATE(), '', NULL, '分账户负债基点价值DV10表菜单');

-- 按钮父菜单ID
SET @parentId = LAST_INSERT_ID();

-- 按钮 SQL
INSERT INTO alm.sys_menu (menu_name,parent_id,order_num,`path`,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('分账户负债基点价值DV10表查询', @parentId, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'dur:account:liability:dv10:query', '#', 'admin', SYSDATE(), '', NULL, ''),
('分账户负债基点价值DV10表新增', @parentId, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'dur:account:liability:dv10:add', '#', 'admin', SYSDATE(), '', NULL, ''),
('分账户负债基点价值DV10表修改', @parentId, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'dur:account:liability:dv10:edit', '#', 'admin', SYSDATE(), '', NULL, ''),
('分账户负债基点价值DV10表删除', @parentId, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'dur:account:liability:dv10:remove', '#', 'admin', SYSDATE(), '', NULL, ''),
('分账户负债基点价值DV10表导出', @parentId, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'dur:account:liability:dv10:export', '#', 'admin', SYSDATE(), '', NULL, ''),
('分账户负债基点价值DV10表导入', @parentId, 6, '#', '', '', '', 1, 0, 'F', '0', '0', 'dur:account:liability:dv10:import', '#', 'admin', SYSDATE(), '', NULL, '');
