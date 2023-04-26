package com.spike.service;

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
}
