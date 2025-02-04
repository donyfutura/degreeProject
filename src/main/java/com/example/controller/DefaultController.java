package com.example.controller;

import com.example.api.response.InitResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DefaultController {
    private final InitResponse initResponse;

    public DefaultController(InitResponse initResponse) {
        this.initResponse = initResponse;
    }

    @GetMapping("/")
    public String mainPage(){

        return "index";
    }



}
