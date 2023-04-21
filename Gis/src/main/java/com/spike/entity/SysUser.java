package com.spike.entity;

import lombok.Data;

/**
 * @PACKAGE_NAME: com.example.fxpgJava.entity
 * @NAME: SysUser
 * @USER: spike
 * @DATE: 2023/4/20 16:34
 * @PROJECT_NAME: FXPG_Java
 */
@Data
public class SysUser {
    private int id;
    private String userName;
    private String password;
    private String role;
    private String name;
    private String phone;
    private String company;

    public String toString() {
        return "{\"id\":" + this.id + "," +
                "\"userName\":\""+this.userName+"\"," +
                "\"password\":\""+this.password+"\"," +
                "\"role\":\""+this.role+"\"," +
                "\"name\":\""+this.name+"\"," +
                "\"phone\":"+this.phone+"," +
                "\"company\":"+this.company+"}";
    }
}
