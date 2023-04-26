package com.spike.service;

import cn.dev33.satoken.util.SaResult;
import com.spike.entity.SysUser;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface UserService {
    List<SysUser> queryUser(String userName, String password);

    SaResult login(String userName, String password, HttpServletRequest request, HttpServletResponse response) throws Exception;

    SaResult register(String userName, String password, boolean role);

    SaResult delete(Integer id);

    SaResult update(Integer id, Map<String, Object> map);

    SaResult list(Integer pageSize, Integer pageIndex);

    SaResult queryUserById(Integer id);
}
