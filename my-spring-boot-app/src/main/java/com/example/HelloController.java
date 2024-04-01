package com.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        // 觸發一個故意的錯誤
        int i = 1 / 0;
        return "Hello, World!";
    }
}

