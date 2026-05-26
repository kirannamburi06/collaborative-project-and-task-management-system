package com.kiran.collaborativeprojectandtaskmanagementsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CollaborativeProjectAndTaskManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(CollaborativeProjectAndTaskManagementSystemApplication.class, args);
    }

}
