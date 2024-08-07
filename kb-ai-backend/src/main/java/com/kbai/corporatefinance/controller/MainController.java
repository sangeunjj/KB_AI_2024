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
}

