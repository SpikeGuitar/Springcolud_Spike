package com.spike.service.impl;

import com.spike.mapper.CoreMapper;
import com.spike.service.CoreService;
import com.spike.utils.DynamicFormsUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @PACKAGE_NAME: com.spike.service.impl
 * @NAME: CoreServiceImpl
 * @USER: spike
 * @DATE: 2023/4/26 10:13
 * @PROJECT_NAME: Springcolud_Spike
 */
@Service
public class CoreServiceImpl implements CoreService {
    @Resource
    CoreMapper coreMapper;
    
    @Override
    public List<Map<String, Object>> getTableHeadOrderBy(String tableName, String[] orderList) {
        List<Map<String, Object>> list = coreMapper.getTableHeadPGSql(tableName);
        List<Map<String, Object>> tableHeadList = DynamicFormsUtil.getTableHeadOrderBy(list,orderList);
        return tableHeadList;
    }
}
