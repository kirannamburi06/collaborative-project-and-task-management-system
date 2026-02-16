package com.kiran.collaborativeprojectandtaskmanagementsystem.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DemoController {

    @GetMapping("/welcome")
    public String welcome(){
        return "Welcome to the page";
    }

}
