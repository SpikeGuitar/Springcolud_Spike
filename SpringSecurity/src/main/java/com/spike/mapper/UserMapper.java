package com.spike.mapper;

import com.spike.Entity.User;

public interface UserMapper {
    User findUserByName(String userName);
}
