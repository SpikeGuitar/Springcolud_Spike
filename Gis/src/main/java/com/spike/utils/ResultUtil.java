package com.spike.utils;


import cn.dev33.satoken.util.SaResult;

import java.util.List;
import java.util.Map;

/**
 * @PACKAGE_NAME: com.example.fxpgJava.utils
 * @NAME: ResultUtil 返回结果包装类
 * @USER: spike
 * @DATE: 2023/4/10 11:39
 * @PROJECT_NAME: FXPG_Java
 */

public class ResultUtil {

//    令牌
    public final static String TOKEN="token";

//    页码
    public final static String PAGE_INDEX="pageIndex";

//    页内行数
    public final static String PAGE_SIZE="pageSize";

//    总数
    public final static String TOTAL="total";

    public static SaResult initResult(SaResult saResult){
        saResult.set("error", "");
        saResult.set("warn", "");
        saResult.set("log", null);
        saResult.set("message", "");
        saResult.set("oper_id", null);
        saResult.set("oper_name", null);
        return saResult;
    }

    public static SaResult data(Object data){
        SaResult saResult = SaResult.data(data);
        saResult = ResultUtil.initResult(saResult);
        saResult.set("status", "success");
        saResult.set("success", true);
        return saResult;
    }

    public static SaResult error(String msg){
        SaResult saResult = SaResult.error(msg);
        saResult = ResultUtil.initResult(saResult);
        saResult.set("status", "error");
        saResult.set("success", false);
        return saResult;
    }

    public static SaResult setTotal(SaResult saResult,List<Map<String, Object>> list){
        // 总数初始值
        Long total = 0L;
        if(!list.isEmpty()){
            // 从list中获取第一个的总数
            total = (Long) list.get(0).get(ResultUtil.TOTAL);
        }
        saResult.set(ResultUtil.TOTAL, total);
        for(Map<String, Object> map : list){
            map.remove(ResultUtil.TOTAL);
        }
        return saResult;
    }
}
