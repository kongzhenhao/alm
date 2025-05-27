-- 分产品统计表菜单SQL

-- 菜单 SQL
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (
    '分产品统计',
    (SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE menu_name = '成本管理' AND parent_id = 0 LIMIT 1) AS temp),
    7,
    'statistics',
    'cost/product/statistics/index',
    1,
    0,
    'C',
    '0',
    '0',
    'cost:product:statistics:list',
    'chart',
    'admin',
    SYSDATE(),
    '',
    NULL,
    '分产品统计管理菜单'
);

-- 按钮 SQL
-- 查询按钮
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (
    '分产品统计查询',
    (SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE menu_name = '分产品统计' AND parent_id = (SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE menu_name = '成本管理' AND parent_id = 0 LIMIT 1) AS temp) LIMIT 1) AS temp2),
    1,
    '#',
    '',
    1,
    0,
    'F',
    '0',
    '0',
    'cost:product:statistics:query',
    '#',
    'admin',
    SYSDATE(),
    '',
    NULL,
    ''
);

-- 新增按钮
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (
    '分产品统计新增',
    (SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE menu_name = '分产品统计' AND parent_id = (SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE menu_name = '成本管理' AND parent_id = 0 LIMIT 1) AS temp) LIMIT 1) AS temp2),
    2,
    '#',
    '',
    1,
    0,
    'F',
    '0',
    '0',
    'cost:product:statistics:add',
    '#',
    'admin',
    SYSDATE(),
    '',
    NULL,
    ''
);

-- 修改按钮
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (
    '分产品统计修改',
    (SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE menu_name = '分产品统计' AND parent_id = (SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE menu_name = '成本管理' AND parent_id = 0 LIMIT 1) AS temp) LIMIT 1) AS temp2),
    3,
    '#',
    '',
    1,
    0,
    'F',
    '0',
    '0',
    'cost:product:statistics:edit',
    '#',
    'admin',
    SYSDATE(),
    '',
    NULL,
    ''
);

-- 删除按钮
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (
    '分产品统计删除',
    (SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE menu_name = '分产品统计' AND parent_id = (SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE menu_name = '成本管理' AND parent_id = 0 LIMIT 1) AS temp) LIMIT 1) AS temp2),
    4,
    '#',
    '',
    1,
    0,
    'F',
    '0',
    '0',
    'cost:product:statistics:remove',
    '#',
    'admin',
    SYSDATE(),
    '',
    NULL,
    ''
);

-- 导出按钮
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (
    '分产品统计导出',
    (SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE menu_name = '分产品统计' AND parent_id = (SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE menu_name = '成本管理' AND parent_id = 0 LIMIT 1) AS temp) LIMIT 1) AS temp2),
    5,
    '#',
    '',
    1,
    0,
    'F',
    '0',
    '0',
    'cost:product:statistics:export',
    '#',
    'admin',
    SYSDATE(),
    '',
    NULL,
    ''
);

-- 导入按钮
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (
    '分产品统计导入',
    (SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE menu_name = '分产品统计' AND parent_id = (SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE menu_name = '成本管理' AND parent_id = 0 LIMIT 1) AS temp) LIMIT 1) AS temp2),
    6,
    '#',
    '',
    1,
    0,
    'F',
    '0',
    '0',
    'cost:product:statistics:import',
    '#',
    'admin',
    SYSDATE(),
    '',
    NULL,
    ''
);
