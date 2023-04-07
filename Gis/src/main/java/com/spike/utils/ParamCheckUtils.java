package com.spike.utils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @PACKAGE_NAME: com.spike.utils
 * @NAME: ParamCheckUtils
 * @USER: spike
 * @DATE: 2023/4/7 10:37
 * @PROJECT_NAME: Springcolud_Spike
 */

public class ParamCheckUtils {

    /**
     * 校验map中的空值
     *
     * @param map 需要校验的map对象
     * @param stringList map中需要校验的key
     * @return
     */
    public static boolean checkMapParamHasNull(Map<String, Object> map, List<String> stringList){
        for (String key:stringList){
            Object obj = map.get(key);
            if(Objects.isNull(obj)||!Objects.isNull(obj)&&obj instanceof String&&((String) obj).isEmpty()){
                return false;
            }
        }
        return true;
    }
}
