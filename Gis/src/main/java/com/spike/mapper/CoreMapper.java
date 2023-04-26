package com.spike.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @PACKAGE_NAME: com.spike.mapper
 * @NAME: CoreMapper
 * @USER: spike
 * @DATE: 2023/4/26 10:08
 * @PROJECT_NAME: Springcolud_Spike
 */
@Mapper
public interface CoreMapper {
    List<Map<String,Object>> getTableHeadPGSql(String tableName);
}
