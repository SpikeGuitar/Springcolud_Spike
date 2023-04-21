package com.spike.filter;

import cn.dev33.satoken.util.SaResult;
import com.alibaba.fastjson.JSON;
import com.spike.entity.SysUser;
import com.spike.utils.EncryptAndDecrypte;
import com.spike.utils.ResultUtil;
import lombok.SneakyThrows;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;

/**
 * @PACKAGE_NAME: com.example.fxpgJava.filter
 * @NAME: ManageApiFilter 经理用户的过滤
 * @USER: spike
 * @DATE: 2023/4/12 8:55
 * @PROJECT_NAME: FXPG_Java
 */
@Order(1)
public class ManageApiFilter implements Filter {

    //权限不通过错误提升
    private final static String AUTH_FAILED = "请求来源非法";

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @SneakyThrows
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        HttpServletRequest req = (HttpServletRequest) request;
        Cookie[] cookies = req.getCookies();
        if (Objects.isNull(cookies)) {
            response = sendErrorMsg(AUTH_FAILED, response);
            return;
        }
        SysUser user = null;
        for (Cookie cookie : cookies) {
            String key = cookie.getName();
            if (key.equals(ResultUtil.TOKEN) && cookie.getValue() != null) {
                String token = cookie.getValue();
                user = JSON.parseObject(EncryptAndDecrypte.decrypteString(token), SysUser.class);
                continue;
            }
            // 判断token是否为空
            if (key.equals(ResultUtil.TOKEN) && cookie.getValue() == null) {
                response = sendErrorMsg(AUTH_FAILED, response);
                return;
            }
        }
        //判断 用户对象为空 或 用户账号为空 或 用户角色为空 或 用户角色不为管理员
        if (user == null || Objects.isNull(user.getUserName()) || Objects.isNull(user.getRole()) || !user.getRole().equals("管理员")) {
            response = sendErrorMsg(AUTH_FAILED, response);
            return;
        }
        chain.doFilter(request, response);
    }

    private ServletResponse sendErrorMsg(String msg, ServletResponse response) throws IOException {
        SaResult saResult = ResultUtil.error(msg);
        //没有带token的接口会被拦截，固定返回code为-99
        saResult.setCode(-99);
        String jsonString = JSON.toJSONString(saResult);
        ServletOutputStream outputStream = response.getOutputStream();
        //json格式返回
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        outputStream.write(jsonString.getBytes());
        outputStream.flush();
        return response;
    }

    @Override
    public void destroy() {

    }
}
