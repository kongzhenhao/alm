-- 折现曲线菜单 SQL
INSERT INTO alm.sys_menu (menu_name,parent_id,order_num,`path`,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('折现曲线',1,1,'discountCurve','dur/discount/curve/index','','',1,0,'C','0','0','dur:discount:curve:list','chart','admin',SYSDATE(),'',NULL,'折现曲线菜单'),
('折现曲线查询',100,1,'','','','',1,0,'F','0','0','dur:discount:curve:query','#','admin',SYSDATE(),'',NULL,''),
('折现曲线新增',100,2,'','','','',1,0,'F','0','0','dur:discount:curve:add','#','admin',SYSDATE(),'',NULL,''),
('折现曲线修改',100,3,'','','','',1,0,'F','0','0','dur:discount:curve:edit','#','admin',SYSDATE(),'',NULL,''),
('折现曲线删除',100,4,'','','','',1,0,'F','0','0','dur:discount:curve:remove','#','admin',SYSDATE(),'',NULL,''),
('折现曲线导出',100,5,'','','','',1,0,'F','0','0','dur:discount:curve:export','#','admin',SYSDATE(),'',NULL,''),
('折现曲线导入',100,6,'','','','',1,0,'F','0','0','dur:discount:curve:import','#','admin',SYSDATE(),'',NULL,'');

-- 负债现金流汇总菜单 SQL
INSERT INTO alm.sys_menu (menu_name,parent_id,order_num,`path`,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('负债现金流汇总',1,2,'liabilityCashFlowSummary','dur/liability/cash/flow/summary/index','','',1,0,'C','0','0','dur:liability:cash:flow:summary:list','table','admin',SYSDATE(),'',NULL,'负债现金流汇总菜单');

SET @parentId = LAST_INSERT_ID();

INSERT INTO alm.sys_menu (menu_name,parent_id,order_num,`path`,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('负债现金流汇总查询',@parentId,1,'','','','',1,0,'F','0','0','dur:liability:cash:flow:summary:query','#','admin',SYSDATE(),'',NULL,''),
('负债现金流汇总新增',@parentId,2,'','','','',1,0,'F','0','0','dur:liability:cash:flow:summary:add','#','admin',SYSDATE(),'',NULL,''),
('负债现金流汇总修改',@parentId,3,'','','','',1,0,'F','0','0','dur:liability:cash:flow:summary:edit','#','admin',SYSDATE(),'',NULL,''),
('负债现金流汇总删除',@parentId,4,'','','','',1,0,'F','0','0','dur:liability:cash:flow:summary:remove','#','admin',SYSDATE(),'',NULL,''),
('负债现金流汇总导出',@parentId,5,'','','','',1,0,'F','0','0','dur:liability:cash:flow:summary:export','#','admin',SYSDATE(),'',NULL,''),
('负债现金流汇总导入',@parentId,6,'','','','',1,0,'F','0','0','dur:liability:cash:flow:summary:import','#','admin',SYSDATE(),'',NULL,'');

-- 负债久期汇总菜单 SQL
INSERT INTO alm.sys_menu (menu_name,parent_id,order_num,`path`,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('负债久期汇总',1,3,'liabilityDurationSummary','dur/liability/duration/summary/index','','',1,0,'C','0','0','dur:liability:duration:summary:list','table','admin',SYSDATE(),'',NULL,'负债久期汇总菜单');

SET @parentId = LAST_INSERT_ID();

INSERT INTO alm.sys_menu (menu_name,parent_id,order_num,`path`,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('负债久期汇总查询',@parentId,1,'','','','',1,0,'F','0','0','dur:liability:duration:summary:query','#','admin',SYSDATE(),'',NULL,''),
('负债久期汇总新增',@parentId,2,'','','','',1,0,'F','0','0','dur:liability:duration:summary:add','#','admin',SYSDATE(),'',NULL,''),
('负债久期汇总修改',@parentId,3,'','','','',1,0,'F','0','0','dur:liability:duration:summary:edit','#','admin',SYSDATE(),'',NULL,''),
('负债久期汇总删除',@parentId,4,'','','','',1,0,'F','0','0','dur:liability:duration:summary:remove','#','admin',SYSDATE(),'',NULL,''),
('负债久期汇总导出',@parentId,5,'','','','',1,0,'F','0','0','dur:liability:duration:summary:export','#','admin',SYSDATE(),'',NULL,''),
('负债久期汇总导入',@parentId,6,'','','','',1,0,'F','0','0','dur:liability:duration:summary:import','#','admin',SYSDATE(),'',NULL,'');

-- 分账户负债现金流现值汇总菜单 SQL
INSERT INTO alm.sys_menu (menu_name,parent_id,order_num,`path`,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('分账户负债现金流现值汇总',1,4,'subAccountLiabilityPresentValue','dur/sub/account/liability/present/value/index','','',1,0,'C','0','0','dur:sub:account:liability:present:value:list','table','admin',SYSDATE(),'',NULL,'分账户负债现金流现值汇总菜单');

SET @parentId = LAST_INSERT_ID();

INSERT INTO alm.sys_menu (menu_name,parent_id,order_num,`path`,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('分账户负债现金流现值汇总查询',@parentId,1,'','','','',1,0,'F','0','0','dur:sub:account:liability:present:value:query','#','admin',SYSDATE(),'',NULL,''),
('分账户负债现金流现值汇总新增',@parentId,2,'','','','',1,0,'F','0','0','dur:sub:account:liability:present:value:add','#','admin',SYSDATE(),'',NULL,''),
('分账户负债现金流现值汇总修改',@parentId,3,'','','','',1,0,'F','0','0','dur:sub:account:liability:present:value:edit','#','admin',SYSDATE(),'',NULL,''),
('分账户负债现金流现值汇总删除',@parentId,4,'','','','',1,0,'F','0','0','dur:sub:account:liability:present:value:remove','#','admin',SYSDATE(),'',NULL,''),
('分账户负债现金流现值汇总导出',@parentId,5,'','','','',1,0,'F','0','0','dur:sub:account:liability:present:value:export','#','admin',SYSDATE(),'',NULL,''),
('分账户负债现金流现值汇总导入',@parentId,6,'','','','',1,0,'F','0','0','dur:sub:account:liability:present:value:import','#','admin',SYSDATE(),'',NULL,'');

-- 分账户负债久期汇总菜单 SQL
INSERT INTO alm.sys_menu (menu_name,parent_id,order_num,`path`,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
('分账户负债久期汇总', (SELECT menu_id FROM sys_menu WHERE menu_name = '久期管理' AND parent_id = 0), 5, 'subAccountLiabilityDuration', 'dur/sub/account/liability/duration/index', '', '', 1, 0, 'C', '0', '0', 'dur:sub:account:liability:duration:list', 'table', 'admin', SYSDATE(), '', NULL, '分账户负债久期汇总菜单');

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
