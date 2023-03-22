package com.spike;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class ServerTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerTestApplication.class,args);
    }
}
