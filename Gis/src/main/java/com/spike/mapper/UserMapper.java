package com.spike.mapper;

import com.spike.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {
    List<SysUser> queryUser(String userName, String password);

    Integer queryUserMaxId();

    Integer addUser(Integer newId, String userName, String password, String roleName);

    List<Map<String, Object>> queryUserByName(String userName);

    Integer delete(Integer id);

    List<Map<String, Object>> queryUserById(Integer id);

    Integer update(Map<String, Object> map);

    List<Map<String, Object>> list(Integer pageIndex, Integer pageSize);
}
