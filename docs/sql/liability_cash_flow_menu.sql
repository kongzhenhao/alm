-- 负债现金流数据管理菜单SQL

-- 菜单 SQL
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (
    '负债现金流数据',
    (SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE menu_name = '成本管理' AND parent_id = 0 LIMIT 1) AS temp),
    7,
    'liability',
    'cost/liability-cash-flow/index',
    1,
    0,
    'C',
    '0',
    '0',
    'cost:liability:list',
    'chart',
    'admin',
    SYSDATE(),
    '',
    NULL,
    '负债现金流数据管理菜单'
);

-- 按钮 SQL
-- 查询按钮
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (
    '负债现金流数据查询',
    (SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE menu_name = '负债现金流数据' AND parent_id = (SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE menu_name = '成本管理' AND parent_id = 0 LIMIT 1) AS temp) LIMIT 1) AS temp2),
    1,
    '#',
    '',
    1,
    0,
    'F',
    '0',
    '0',
    'cost:liability:query',
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
    '负债现金流数据新增',
    (SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE menu_name = '负债现金流数据' AND parent_id = (SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE menu_name = '成本管理' AND parent_id = 0 LIMIT 1) AS temp) LIMIT 1) AS temp2),
    2,
    '#',
    '',
    1,
    0,
    'F',
    '0',
    '0',
    'cost:liability:add',
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
    '负债现金流数据修改',
    (SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE menu_name = '负债现金流数据' AND parent_id = (SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE menu_name = '成本管理' AND parent_id = 0 LIMIT 1) AS temp) LIMIT 1) AS temp2),
    3,
    '#',
    '',
    1,
    0,
    'F',
    '0',
    '0',
    'cost:liability:edit',
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
    '负债现金流数据删除',
    (SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE menu_name = '负债现金流数据' AND parent_id = (SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE menu_name = '成本管理' AND parent_id = 0 LIMIT 1) AS temp) LIMIT 1) AS temp2),
    4,
    '#',
    '',
    1,
    0,
    'F',
    '0',
    '0',
    'cost:liability:remove',
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
    '负债现金流数据导出',
    (SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE menu_name = '负债现金流数据' AND parent_id = (SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE menu_name = '成本管理' AND parent_id = 0 LIMIT 1) AS temp) LIMIT 1) AS temp2),
    5,
    '#',
    '',
    1,
    0,
    'F',
    '0',
    '0',
    'cost:liability:export',
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
    '负债现金流数据导入',
    (SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE menu_name = '负债现金流数据' AND parent_id = (SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE menu_name = '成本管理' AND parent_id = 0 LIMIT 1) AS temp) LIMIT 1) AS temp2),
    6,
    '#',
    '',
    1,
    0,
    'F',
    '0',
    '0',
    'cost:liability:import',
    '#',
    'admin',
    SYSDATE(),
    '',
    NULL,
    ''
);
