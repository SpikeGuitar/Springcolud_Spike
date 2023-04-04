package com.spike;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.spike.mapper")
public class SpikeSecurityApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpikeSecurityApplication.class, args);
    }
}
