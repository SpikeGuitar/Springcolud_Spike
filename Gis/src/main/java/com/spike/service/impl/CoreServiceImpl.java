package com.spike.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.spike.entity.Field;
import com.spike.mapper.CoreMapper;
import com.spike.service.CoreService;
import com.spike.utils.DynamicFormsUtil;
import com.spike.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${tableName:spike_}")
    private String tablePrefix;

    @Override
    public List<Map<String, Object>> getTableHeadOrderBy(String tableName, String[] orderList) {
        List<Map<String, Object>> list = coreMapper.getTableHeadPGSql(tableName);
        List<Map<String, Object>> tableHeadList = DynamicFormsUtil.getTableHeadOrderBy(list, orderList);
        return tableHeadList;
    }

    @Override
    public SaResult saveTable(String id, String dataName, String tableName, List<Field> addFieldList, List<Field> updateFieldList, List<Field> deletedFieldList) {
        //判断表单新增还是修改
        if (id == null || id.isEmpty()) {
            //新增
            return  addTable(dataName, tableName, addFieldList);
        } else {
            //修改
            return updateTable(id, dataName, tableName, addFieldList, updateFieldList, deletedFieldList);
        }
    }

    private SaResult updateTable(String id, String dataName, String tableName, List<Field> addFieldList, List<Field> updateFieldList, List<Field> deletedFieldList) {
        //保存时 查询修改后表名是否存在
        if (checkTableAbsent(tableName)) {
            //修改字段需要判断有数据无法修改类型的情况
            //修改表单时 三个数组 新增的字段 删除的字段 修改的字段
            return ResultUtil.data("表单保存成功");
        }else {
            //存在返回错误
            return ResultUtil.error("表单已存在");
        }
    }

    private SaResult addTable(String dataName, String tableName, List<Field> addFieldList) {
        //保存时 查询表名是否存在
        if (checkTableAbsent(tableName)) {
            //不存在创建表单
            tableName = tablePrefix + tableName;
            Integer addNum = coreMapper.addTable(dataName, tableName, addFieldList);
            return ResultUtil.data("表单保存成功");
        } else {
            //存在返回错误
            return ResultUtil.error("表单已存在");
        }
    }

    /**
     * check table absent
     *
     * @param tableName
     * @return boolean
     */
    private boolean checkTableAbsent(String tableName) {
        List<Map<String, Object>> tableList = coreMapper.checkTableNameAbsent(tableName);
        if (tableList.isEmpty()) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }


}
