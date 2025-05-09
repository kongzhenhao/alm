package com.xl.alm.app;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 菜单SQL生成工具
 * 根据Vue文件路径和v-hasPermi权限标识自动生成菜单SQL语句
 *
 * @author admin
 */
public class MenuSqlGenerator {

    /**
     * 父菜单ID
     */
    private static final Long PARENT_MENU_ID = 2000L;

    /**
     * 创建者和更新者
     */
    private static final String ADMIN = "admin";

    /**
     * 当前时间
     */
    private static final String CURRENT_TIME = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    /**
     * 中文按钮名称映射
     */
    private static final Map<String, String> PERM_NAME_MAP = new HashMap<>();

    public static void main(String[] args) {
        // Vue文件路径
        String vueFilePath = "web/src/views/dur/liability/cash/flow/index.vue";

        try {
            // 解析Vue文件中的权限标识
            Set<String> permSet = extractPermissions(vueFilePath);

            // 提取路径信息
            Map<String, String> pathInfo = extractPathInfo(vueFilePath);

            // 生成SQL语句
            String sql = generateMenuSql(pathInfo, permSet);

            // 输出SQL语句
            System.out.println(sql);

            // 可选：将SQL保存到文件
            saveToFile(sql, "menu_sql.sql");

            System.out.println("SQL生成成功，已保存到 menu_sql.sql 文件");
        } catch (IOException e) {
            System.err.println("生成SQL失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 解析Vue文件中的权限标识
     *
     * @param filePath Vue文件路径
     * @return 权限标识集合
     * @throws IOException 读取文件异常
     */
    private static Set<String> extractPermissions(String filePath) throws IOException {
        Set<String> permSet = new HashSet<>();
        // 读取文件内容
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        // 使用正则表达式提取v-hasPermi和按钮文本
        Pattern pattern = Pattern.compile("<el-button[^>]*v-hasPermi=\"\\['([^']+)'\\]\"[^>]*>([^<]+)</el-button>");
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            String perm = matcher.group(1);
            String text = matcher.group(2).trim();
            permSet.add(text + "," + perm);
        }
        return permSet;
    }

    /**
     * 从文件路径提取相关信息
     *
     * @param filePath Vue文件路径
     * @return 路径信息Map
     */
    private static Map<String, String> extractPathInfo(String filePath) {
        Map<String, String> pathInfo = new HashMap<>();

        // 移除头部的web/src/views/
        String component = filePath;
        if (component.startsWith("web/src/views/")) {
            component = component.substring("web/src/views/".length());
        }

        // 移除文件扩展名
        if (component.endsWith(".vue")) {
            component = component.substring(0, component.length() - 4);
        }

        // 获取路径部分
        String[] pathParts = component.split("/");
        String routePath = "";
        String menuName = "现金流管理"; // 默认菜单名

        if (pathParts.length > 0) {
            // 以最后一级目录作为路由路径
            // 如果存在多级目录可以用驼峰命名
            StringBuilder pathBuilder = new StringBuilder();
            for (int i = 0; i < pathParts.length - 1; i++) {
                if (i > 0) {
                    pathBuilder.append(Character.toUpperCase(pathParts[i].charAt(0)))
                            .append(pathParts[i].substring(1));
                } else {
                    pathBuilder.append(pathParts[i]);
                }
            }

            // 如果最后一部分是index，则使用前一部分
            if ("index".equals(pathParts[pathParts.length - 1])) {
                routePath = pathBuilder.toString();
            } else {
                routePath = pathBuilder.toString() + Character.toUpperCase(pathParts[pathParts.length - 1].charAt(0))
                        + pathParts[pathParts.length - 1].substring(1);
            }
        }

        // 基础权限前缀
        String permPrefix = component.replace("/", ":");

        pathInfo.put("component", component);
        pathInfo.put("routePath", routePath);
        pathInfo.put("menuName", menuName);
        pathInfo.put("permPrefix", permPrefix);

        return pathInfo;
    }

    /**
     * 生成菜单SQL语句
     *
     * @param pathInfo 路径信息
     * @param permSet 权限标识集合
     * @return SQL语句
     */
    private static String generateMenuSql(Map<String, String> pathInfo, Set<String> permSet) {
        StringBuilder sql = new StringBuilder();
        sql.append("-- 菜单SQL\n");
        sql.append("-- 表名称：sys_menu\n\n");

        // 当前时间
        String currentTime = CURRENT_TIME;

        // 基础权限前缀
        String permPrefix = "dur:liability:cash:flow";

        // 生成父菜单SQL
        String parentMenuSql = String.format(
                "INSERT INTO alm.sys_menu (menu_name,parent_id,order_num,`path`,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES\n" +
                        "('%s',%d,%d,'%s','%s',NULL,'',1,0,'C','0','0','%s:list','#','%s','%s','%s','%s','');\n",
                "现金流管理", PARENT_MENU_ID, 3, "liabilityCashFlow", "dur/liability/cash/flow/index",
                permPrefix, ADMIN, currentTime, ADMIN, currentTime
        );

        sql.append(parentMenuSql).append("\n");

        // 获取父菜单ID
        String parentMenuIdSql = "-- 获取菜单ID\nSELECT @parentId := LAST_INSERT_ID();\n\n";
        sql.append(parentMenuIdSql);

        // 生成子菜单SQL
        sql.append("-- 按钮父菜单ID\n");
        int orderNum = 1;

        // 按钮菜单SQL模板
        String buttonSqlTemplate =
                "INSERT INTO alm.sys_menu (menu_name,parent_id,order_num,`path`,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES\n" +
                        "('%s',@parentId,%d,'#','',NULL,'',1,0,'F','0','0','%s','#','%s','%s','%s','%s','');\n";

        // 为每个权限标识生成子菜单SQL
        List<String> sortedPerms = new ArrayList<>(permSet);
        sortedPerms.sort((a, b) -> {
            // 提取权限操作部分（最后一部分）
            String opA = a.substring(a.lastIndexOf(":") + 1);
            String opB = b.substring(b.lastIndexOf(":") + 1);

            // 定义操作顺序
            List<String> opOrder = Arrays.asList("query", "list", "add", "edit", "remove", "export", "import");
            return Integer.compare(opOrder.indexOf(opA), opOrder.indexOf(opB));
        });

        for (String perm : sortedPerms) {
            // 提取权限操作部分（最后一部分）
            String operation = perm.substring(perm.lastIndexOf(":") + 1);

            // 获取按钮名称
            String buttonName = PERM_NAME_MAP.getOrDefault(operation, operation);

            // 生成按钮菜单SQL
            String buttonSql = String.format(
                    buttonSqlTemplate,
                    buttonName, orderNum++, perm, ADMIN, currentTime, ADMIN, currentTime
            );

            sql.append(buttonSql);
        }

        return sql.toString();
    }

    /**
     * 将SQL保存到文件
     *
     * @param sql SQL语句
     * @param fileName 文件名
     * @throws IOException 保存文件异常
     */
    private static void saveToFile(String sql, String fileName) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.print(sql);
        }
    }
}
