package com.example.controller;


import com.example.model.User;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ApiChangePass {

    private final UserRepository userRepository;

    @Autowired
    public ApiChangePass(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


}
