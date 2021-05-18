package com.example.controller;

import com.example.api.request.ModerationRequest;
import com.example.api.response.ModerationResponse;
import com.example.service.ModerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/moderation")
public class ApiModerationController {

    private final ModerationService moderationService;

    @Autowired
    public ApiModerationController(ModerationService moderationService) {
        this.moderationService = moderationService;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('user:moderate')")
    public ResponseEntity<ModerationResponse> setupPost(@RequestBody ModerationRequest moderationRequest,
                                                        Authentication authentication){
        return moderationService.setup(moderationRequest, authentication);
    }
}
