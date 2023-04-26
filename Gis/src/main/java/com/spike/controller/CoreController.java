package com.spike.controller;

import cn.dev33.satoken.util.SaResult;
import com.spike.service.CoreService;
import com.spike.utils.ParamCheckUtils;
import com.spike.utils.ResultUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @PACKAGE_NAME: com.spike.controller
 * @NAME: CoreController
 * @USER: spike
 * @DATE: 2023/4/26 9:31
 * @PROJECT_NAME: Springcolud_Spike
 */
@Slf4j
@RestController
@RequestMapping("/Core")
public class CoreController {
    @Resource
    CoreService coreService;

    /**
     * 通过表名和排序字段获取排序后的动态表头
     *
     * @param tableName 表名
     * @param orderList 排序数组
     * @return
     */
    @ApiOperation(value = "通过表名和排序字段获取排序后的动态表头", tags = "Core 核心代码")
    @RequestMapping(value = "/GetTableHeadOrderBy", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tableName", value = "表名", paramType = "body"),
            @ApiImplicitParam(name = "orderList", value = "排序数组", dataType = "list", paramType = "body")
    })
    public SaResult getTableHeadOrderBy(@RequestParam(value = "tableName") String tableName,@RequestParam(value = "orderList") String[] orderList) {
        List<Map<String, Object>> listMap = coreService.getTableHeadOrderBy(tableName, orderList);
        return ResultUtil.data(listMap);
    }

}
