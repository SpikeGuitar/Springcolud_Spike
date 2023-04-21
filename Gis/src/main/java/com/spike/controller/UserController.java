package com.spike.controller;

import cn.dev33.satoken.util.SaResult;

import com.spike.service.UserService;
import com.spike.utils.ParamCheckUtils;
import com.spike.utils.ResultUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @PACKAGE_NAME: com.example.fxpgJava.controller
 * @NAME: UserController 用户管理
 * @USER: spike
 * @DATE: 2023/4/10 17:37
 * @PROJECT_NAME: FXPG_Java
 */

@RestController
@RequestMapping("/User")
public class UserController {
    @Autowired
    UserService userService;

//    账号
    private final static String USERNAME = "userName";

//    密码
    private final static String PASSWORD = "password";

//    角色
    private final static String ROLE = "role";

//    用户ID
    private final static String ID = "id";

    /**
     * 用户登录
     *
     * @param map
     * @return
     */
    @ApiOperation(value = "用户登录", tags = "User 用户管理")
    @RequestMapping(value = "/Login", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "map", value = "{\"userName\": \"账号\", \"password\": \"密码\"}", dataType = "Map", paramType = "body"),
    })
    public SaResult login(@ApiIgnore @RequestBody Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<String> stringList = new ArrayList<>();
        stringList.add(USERNAME);
        stringList.add(PASSWORD);
        boolean check = ParamCheckUtils.checkMapParamHasNull(map, stringList);
        if (!check) {
            return ResultUtil.error("请输入用户名username或密码pwd");
        }
        String userName = map.get(USERNAME).toString();
        String password = map.get(PASSWORD).toString();
        return userService.login(userName, password, request, response);
    }

    /**
     * 用户注册
     *
     * @param map
     * @return
     */
    @ApiOperation(value = "用户注册", tags = "User 用户管理")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "map", value = "{\"userName\": \"账号\", \"password\": \"密码\",\"role\":true(是否为管理员)}", dataType = "Map", paramType = "body"),
    })
    @RequestMapping(value = "/Register", method = RequestMethod.POST)
    public SaResult register(@RequestBody Map<String, Object> map) {
        List<String> stringList = new ArrayList<>();
        stringList.add(USERNAME);
        stringList.add(PASSWORD);
        stringList.add(ROLE);
        boolean check = ParamCheckUtils.checkMapParamHasNull(map, stringList);
        if (!check) {
            return ResultUtil.error("请输入用户名username,密码pwd,用户权限role");
        }
        String userName = map.get(USERNAME).toString();
        String password = map.get(PASSWORD).toString();
        boolean role = (boolean) map.get(ROLE);
        return userService.register(userName, password, role);
    }

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return
     */
    @ApiOperation(value = "删除用户", tags = "User 用户管理")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", dataType = "Integer", paramType = "query"),
    })
    @RequestMapping(value = "/Delete", method = RequestMethod.POST)
    public SaResult delete(@RequestParam(value = "id") Integer id) {
        return userService.delete(id);
    }

    /**
     * 用户修改信息
     *
     * @param id 用户ID
     * @return
     */
    @ApiOperation(value = "用户修改信息", tags = "User 用户管理")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "map", value = "" +
                    "{\n" +
                    "  \"oldPassword\": \"string\",\n" +
                    "  \"newPassword\": \"string\",\n" +
                    "  \"role\": true,\n" +
                    "  \"name\": \"string\",\n" +
                    "  \"phone\": \"string\",\n" +
                    "  \"company\": \"string\"" +
                    "}", dataType = "Map", paramType = "body"),
    })
    @RequestMapping(value = "/Update", method = RequestMethod.POST)
    public SaResult update(@RequestParam(value = "id") Integer id, @RequestBody Map<String, Object> map) {
        return userService.update(id, map);
    }

    /**
     * 用户列表
     *
     * @param pageSize 分页大小
     * @param pageIndex 当前页
     * @return
     */
    @ApiOperation(value = "用户列表", tags = "User 用户管理")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pagesize",//参数名字
                    value = "分页大小",
                    required = true,
                    paramType = "query"
            ),
            @ApiImplicitParam(name = "page",
                    value = "当前页",
                    required = true,
                    paramType = "query"
            )
    })
    @RequestMapping(value = "/List", method = RequestMethod.GET)
    public SaResult list(@RequestParam(value = "pagesize") Integer pageSize,@RequestParam(value = "page") Integer pageIndex){
        if (pageIndex <= 0 || pageSize < 0) {
            return ResultUtil.error("PageSize或PageIndex取值错误");
        }
        return userService.list(pageSize,pageIndex);
    }


    /**
     * 用户详情
     *
     * @param id 用户ID
     * @return
     */
    @ApiOperation(value = "用户详情", tags = "User 用户管理")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", dataType = "Integer",  paramType = "query"),
    })
    @RequestMapping(value = "/FindByID", method = RequestMethod.GET)
    public SaResult findByID(@RequestParam(value = "id") Integer id){
        return userService.queryUserById(id);
    }

}
