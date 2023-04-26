package com.spike.entity;

import lombok.Data;

/**
 * @PACKAGE_NAME: com.spike.entity
 * @NAME: Field
 * @USER: spike
 * @DATE: 2023/4/26 14:51
 * @PROJECT_NAME: Springcolud_Spike
 */
@Data
public class Field {
    //字段名
    private String name;
    
    //字段键值
    private String key;
    
    //数据类型
    private String dataType;
    
    //是否为空
    private boolean isNotNull;
    
    //是否主键
    private boolean isPkey;
}
