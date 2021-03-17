package com.example.controller;

import com.example.api.response.CheckAuthorizationResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final CheckAuthorizationResponse checkAuthorizationResponse;

    public ApiAuthController(CheckAuthorizationResponse checkAuthorizationResponse) {
        this.checkAuthorizationResponse = checkAuthorizationResponse;
        checkAuthorizationResponse.setResult(false);
    }

    @GetMapping("/check")
    public CheckAuthorizationResponse check(){
        return checkAuthorizationResponse;
    }

}
