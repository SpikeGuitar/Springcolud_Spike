package com.spike.mapper;

import com.spike.entity.User;

public interface UserMapper {
    User findUserByName(String userName);
}
