package com.example.controller;

import com.example.api.response.InitResponse;
import com.example.api.response.SettingsResponse;
import com.example.service.SettingsService;
import jdk.jfr.SettingControl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {


    private final InitResponse initResponse;
    private final SettingsService settingsService;

    public ApiGeneralController(InitResponse initResponse, SettingsService settingsService) {
        this.initResponse = initResponse;
        this.settingsService = settingsService;
    }


    @GetMapping("/settings")
    private SettingsResponse settingsResponse(){
        return settingsService.getSettings();
    }

    @GetMapping("/init")
    private InitResponse init(){
        return initResponse;
    }



    // /api/auth/check
}
