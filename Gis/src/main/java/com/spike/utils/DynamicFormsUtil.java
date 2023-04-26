package com.spike.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @PACKAGE_NAME: com.spike.utils
 * @NAME: DynamicFormsUtil
 * @USER: spike
 * @DATE: 2023/4/26 9:33
 * @PROJECT_NAME: Springcolud_Spike
 */
public class DynamicFormsUtil {

    /**
     *
     * @param list 主要数组  结构 {"label":"字段","key":"字段字符","isActive":是否展示}
     * @param orderList 排序字段 按先后排序
     * @return
     */
    public static List<Map<String, Object>> getTableHeadOrderBy(List<Map<String, Object>> list, String[] orderList) {
        Map<String, Object> tableHeadMap = new HashMap<>();
        for (Map<String, Object> map : list) {
            String label = (String) map.get("label");
            map.put("isActive", true);
            tableHeadMap.put(label, map);
        }
        List<Map<String, Object>> headList = new ArrayList<>();
        for (String key : orderList) {
            Map<String, Object> tempMap = (Map<String, Object>) tableHeadMap.get(key);
            headList.add(tempMap);
            tableHeadMap.remove(key);
        }
        for (Object map : tableHeadMap.values()) {
            headList.add((Map<String, Object>) map);
        }
        return headList;
    }
}
