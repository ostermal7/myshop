package com.example.myshop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("")
    public String getMain(){
        return "home";
    }
    
    @GetMapping("/login")
    public String viewLoginPage(){
        return "login";
    }
}