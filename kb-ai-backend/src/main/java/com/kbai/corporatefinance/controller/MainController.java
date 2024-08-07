package com.kbai.corporatefinance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String mainPage() {
        return "chat";
    }
    @GetMapping("/chat")
    public String chat() {
        return "chat";
    }
    @GetMapping("/company")
    public String company() {
        return "company";
    }
    @GetMapping("/report1")
    public String report1() {
        return "report1";
    }
    @GetMapping("/report2")
    public String report2() {
        return "report2";
    }
}

