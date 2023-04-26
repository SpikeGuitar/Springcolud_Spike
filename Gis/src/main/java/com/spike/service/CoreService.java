package com.spike.service;

import cn.dev33.satoken.util.SaResult;
import com.spike.entity.Field;

import java.util.List;
import java.util.Map;

/**
 * @PACKAGE_NAME: com.spike.service
 * @NAME: CoreService
 * @USER: spike
 * @DATE: 2023/4/26 10:12
 * @PROJECT_NAME: Springcolud_Spike
 */
public interface CoreService {
     List<Map<String, Object>> getTableHeadOrderBy(String tableName, String[] orderList);

     SaResult saveTable(String id,String dataName, String tableName, List<Field> addFieldList, List<Field> updateFieldList, List<Field> deletedFieldList);
}
