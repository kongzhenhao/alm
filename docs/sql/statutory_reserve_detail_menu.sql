-- 法定准备金明细管理菜单SQL

-- 菜单 SQL
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (
    '法定准备金明细',
    (SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE menu_name = '成本管理' AND parent_id = 0 LIMIT 1) AS temp),
    4,
    'statutory',
    'base/statutory/reserve/index',
    1,
    0,
    'C',
    '0',
    '0',
    'base:statutory:reserve:list',
    'chart',
    'admin',
    SYSDATE(),
    '',
    NULL,
    '法定准备金明细管理菜单'
);

-- 按钮 SQL
-- 查询按钮
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (
    '法定准备金明细查询',
    (SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE menu_name = '法定准备金明细' AND parent_id = (SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE menu_name = '成本管理' AND parent_id = 0 LIMIT 1) AS temp) LIMIT 1) AS temp2),
    1,
    '#',
    '',
    1,
    0,
    'F',
    '0',
    '0',
    'base:statutory:reserve:query',
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
    '法定准备金明细新增',
    (SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE menu_name = '法定准备金明细' AND parent_id = (SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE menu_name = '成本管理' AND parent_id = 0 LIMIT 1) AS temp) LIMIT 1) AS temp2),
    2,
    '#',
    '',
    1,
    0,
    'F',
    '0',
    '0',
    'base:statutory:reserve:add',
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
    '法定准备金明细修改',
    (SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE menu_name = '法定准备金明细' AND parent_id = (SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE menu_name = '成本管理' AND parent_id = 0 LIMIT 1) AS temp) LIMIT 1) AS temp2),
    3,
    '#',
    '',
    1,
    0,
    'F',
    '0',
    '0',
    'base:statutory:reserve:edit',
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
    '法定准备金明细删除',
    (SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE menu_name = '法定准备金明细' AND parent_id = (SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE menu_name = '成本管理' AND parent_id = 0 LIMIT 1) AS temp) LIMIT 1) AS temp2),
    4,
    '#',
    '',
    1,
    0,
    'F',
    '0',
    '0',
    'base:statutory:reserve:remove',
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
    '法定准备金明细导出',
    (SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE menu_name = '法定准备金明细' AND parent_id = (SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE menu_name = '成本管理' AND parent_id = 0 LIMIT 1) AS temp) LIMIT 1) AS temp2),
    5,
    '#',
    '',
    1,
    0,
    'F',
    '0',
    '0',
    'base:statutory:reserve:export',
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
    '法定准备金明细导入',
    (SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE menu_name = '法定准备金明细' AND parent_id = (SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE menu_name = '成本管理' AND parent_id = 0 LIMIT 1) AS temp) LIMIT 1) AS temp2),
    6,
    '#',
    '',
    1,
    0,
    'F',
    '0',
    '0',
    'base:statutory:reserve:import',
    '#',
    'admin',
    SYSDATE(),
    '',
    NULL,
    ''
);
