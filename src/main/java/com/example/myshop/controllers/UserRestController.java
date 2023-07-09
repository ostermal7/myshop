package com.example.myshop.controllers;

import com.example.myshop.entity.User;
import com.example.myshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {

    @Autowired
    private UserService userService;

    @PostMapping("/users/check_email")
    public String checkDuplicateEmail(Integer id,String email){
        return userService.isEmailUnique(id,email)? "OK" : "Duplicated";
    }
}
