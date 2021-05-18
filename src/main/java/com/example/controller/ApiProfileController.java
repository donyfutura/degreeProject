package com.example.controller;

import com.example.api.request.EditProfileRequest;
import com.example.api.response.Error;
import com.example.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/profile")
public class ApiProfileController {

    private final ProfileService profileService;

    @Autowired
    public ApiProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping(value = "/my", consumes = "multipart/form-data")
    public ResponseEntity<Error> editProfile(
            @RequestParam(value = "email") String email,
            @RequestParam(value = "removePhoto") int removePhoto,
            @RequestParam(value = "photo") MultipartFile file,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "password", required = false) String password,
            Authentication authentication
    ) throws IOException {
        return profileService.editProfileWithPhoto(email, removePhoto, file, name, password, authentication);
    }

    @PostMapping("/my")
    @PreAuthorize("hasAnyAuthority('user:write')")
    public ResponseEntity<Error> editProfile(@RequestBody EditProfileRequest apiRequestBody, Authentication authentication) {
        return profileService.editProfileWithoutPhoto(apiRequestBody, authentication);
    }

}
