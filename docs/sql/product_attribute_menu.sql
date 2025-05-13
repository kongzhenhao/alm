-- 产品属性管理菜单SQL

-- 菜单 SQL
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('产品属性管理', (SELECT menu_id FROM sys_menu WHERE menu_name = '成本管理' AND parent_id = 0 LIMIT 1), 1, 'attribute', 'base/product/attribute/index', 1, 0, 'C', '0', '0', 'base:product:attribute:list', 'table', 'admin', SYSDATE(), '', NULL, '产品属性管理菜单');

-- 按钮 SQL
-- 查询按钮
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('产品属性查询', (SELECT menu_id FROM sys_menu WHERE menu_name = '产品属性管理' AND parent_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '成本管理' AND parent_id = 0 LIMIT 1) LIMIT 1), 1, '#', '', 1, 0, 'F', '0', '0', 'base:product:attribute:query', '#', 'admin', SYSDATE(), '', NULL, '');

-- 新增按钮
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('产品属性新增', (SELECT menu_id FROM sys_menu WHERE menu_name = '产品属性管理' AND parent_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '成本管理' AND parent_id = 0 LIMIT 1) LIMIT 1), 2, '#', '', 1, 0, 'F', '0', '0', 'base:product:attribute:add', '#', 'admin', SYSDATE(), '', NULL, '');

-- 修改按钮
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('产品属性修改', (SELECT menu_id FROM sys_menu WHERE menu_name = '产品属性管理' AND parent_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '成本管理' AND parent_id = 0 LIMIT 1) LIMIT 1), 3, '#', '', 1, 0, 'F', '0', '0', 'base:product:attribute:edit', '#', 'admin', SYSDATE(), '', NULL, '');

-- 删除按钮
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('产品属性删除', (SELECT menu_id FROM sys_menu WHERE menu_name = '产品属性管理' AND parent_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '成本管理' AND parent_id = 0 LIMIT 1) LIMIT 1), 4, '#', '', 1, 0, 'F', '0', '0', 'base:product:attribute:remove', '#', 'admin', SYSDATE(), '', NULL, '');

-- 导出按钮
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('产品属性导出', (SELECT menu_id FROM sys_menu WHERE menu_name = '产品属性管理' AND parent_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '成本管理' AND parent_id = 0 LIMIT 1) LIMIT 1), 5, '#', '', 1, 0, 'F', '0', '0', 'base:product:attribute:export', '#', 'admin', SYSDATE(), '', NULL, '');

-- 导入按钮
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('产品属性导入', (SELECT menu_id FROM sys_menu WHERE menu_name = '产品属性管理' AND parent_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '成本管理' AND parent_id = 0 LIMIT 1) LIMIT 1), 6, '#', '', 1, 0, 'F', '0', '0', 'base:product:attribute:import', '#', 'admin', SYSDATE(), '', NULL, '');
