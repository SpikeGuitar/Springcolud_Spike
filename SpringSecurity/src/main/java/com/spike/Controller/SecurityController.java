package com.spike.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/SecurityController")
public class SecurityController {

    @GetMapping("/hello")
    public String hello() {
        return "欢迎访问 hangge.com";
    }

}
