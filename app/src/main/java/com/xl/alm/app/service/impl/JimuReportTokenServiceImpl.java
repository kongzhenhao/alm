package com.xl.alm.app.service.impl;

import com.jd.lightning.common.core.domain.entity.SysRole;
import com.jd.lightning.common.core.domain.model.LoginUser;
import com.jd.lightning.common.utils.SecurityUtils;
import com.jd.lightning.common.utils.StringUtils;
import com.jd.lightning.framework.web.service.TokenService;
import org.jeecg.modules.jmreport.api.JmReportTokenServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * 自定义积木报表鉴权(如果不进行自定义，则所有请求不做权限控制)
 * 1.自定义获取登录token
 * 2.自定义获取登录用户
 */
@Component
public class JimuReportTokenServiceImpl implements JmReportTokenServiceI {

    @Autowired
    TokenService tokenService;

    /**
     * 通过请求获取Token
     * @param request
     * @return
     */
    @Override
    public String getToken(HttpServletRequest request) {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (loginUser == null) {
            return null;
        }
        return loginUser.getToken();
    }

    /**
     * 自定义获取租户
     *
     * @return
     */
    @Override
    public String getTenantId() {
//        String headerTenantId = null;
//        HttpServletRequest request = JimuSpringContextUtils.getHttpServletRequest();
//        if (request != null) {
//            headerTenantId = request.getHeader(JmConst.HEADER_TENANT_ID);
//            if(OkConvertUtils.isEmpty(headerTenantId)){
//                headerTenantId = request.getParameter(JmConst.TENANT_ID);
//            }
//        }
//        return headerTenantId;
        return "1";
    }


    /**
     * 通过Token获取登录人用户名
     * @param token
     * @return
     */
    @Override
    public String getUsername(String token) {
        return tokenService.getUsernameFromToken(token);
    }

    /**
     * 自定义用户拥有的角色
     *
     * @param token
     * @return
     */
    @Override
    public String[] getRoles(String token) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (!StringUtils.isNull(loginUser) && !CollectionUtils.isEmpty(loginUser.getUser().getRoles())) {
            ArrayList<String> list = new ArrayList<String>();
            for(SysRole sysRole : loginUser.getUser().getRoles()) {
                String roleKey = sysRole.getRoleKey();
                if ("admin".equals(roleKey) || "lowdeveloper".equals(roleKey) || "dbadeveloper".equals(roleKey)) {
                    list.add(roleKey);
                }
            }
            if (list.size() == 0) return null;
            return list.toArray(new String[list.size()]);
        }
        return null;
    }


    /**
     * 自定义用户拥有的权限指令
     *
     * @param token
     * @return
     */
    @Override
    public String[] getPermissions(String token) {
        //drag:datasource:testConnection   仪表盘数据库连接测试
        //onl:drag:clear:recovery          清空回收站
        //drag:analysis:sql                SQL解析
        //drag:design:getTotalData         仪表盘对Online表单展示数据
        return new String[]{"drag:datasource:testConnection","onl:drag:clear:recovery","drag:analysis:sql","drag:design:getTotalData"};
    }

    /**
     * Token校验
     * @param token
     * @return
     */
    @Override
    public Boolean verifyToken(String token) {
        if (StringUtils.isEmpty(token)) {
            return false;
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (!StringUtils.isNull(loginUser)) {
            long expireTime = loginUser.getExpireTime();
            long currentTime = System.currentTimeMillis();
            if (expireTime - currentTime <= 1200000L) {
                return false;
            }
            String[] roles = getRoles(token);
            return roles != null && roles.length > 0;
        }
        return false;
    }
}
