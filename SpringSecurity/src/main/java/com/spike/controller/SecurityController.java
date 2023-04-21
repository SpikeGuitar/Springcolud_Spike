package com.spike.controller;

import com.spike.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/SecurityController")
public class SecurityController {

    @Resource
    private UserMapper userMapper;

    @GetMapping("/hello")
    public String hello() {
        System.out.println(userMapper.findUserByName("spike").toString());
        return "欢迎访问 hangge.com";
    }

}
