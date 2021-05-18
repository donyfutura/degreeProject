package com.example.controller;

import com.example.api.request.SettingsRequest;
import com.example.api.response.InitResponse;
import com.example.api.response.SettingsResponse;
import com.example.service.ImageService;
import com.example.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

    private final ImageService imageService;
    private final SettingsService settingsService;

    @Autowired
    public ApiGeneralController(ImageService imageService, SettingsService settingsService) {
        this.imageService = imageService;
        this.settingsService = settingsService;
    }


    @GetMapping("/settings")
    public SettingsResponse settingsResponse(){
        return settingsService.getSettings();
    }

    @PutMapping("/settings")
    @PreAuthorize("hasAnyAuthority('user:moderate')")
    public Map<String, Boolean> editSettings(@RequestBody SettingsRequest settingsRequest){
        return settingsService.editSettings(settingsRequest);
    }

    @GetMapping("/init")
    private InitResponse init(){
        InitResponse initResponse = new InitResponse();
        initResponse.setEmail("danilakanash999@bk.ru");
        initResponse.setCopyright("donyfutura");
        initResponse.setCopyrightFrom("2019");
        initResponse.setPhone("+7 (999) 999-99-99");
        initResponse.setTitle("DevPub");
        initResponse.setSubtitle("Programming blog");
        return initResponse;
    }

    @PostMapping("/image")
    @PreAuthorize("hasAnyAuthority('user:write')")
    public String addImage(@RequestParam MultipartFile image) throws IOException {
        return imageService.generatePath(image.getBytes(), image);
    }



    // /api/auth/check
}
