package com.example.controller;

import com.example.api.request.ChangePasswordRequest;
import com.example.api.request.LoginRequest;
import com.example.api.request.RegisterRequest;
import com.example.api.request.RestoreRequest;
import com.example.api.response.*;
import com.example.api.response.Error;
import com.example.model.ModerationStatus;
import com.example.repository.PostRepository;
import com.example.repository.UserRepository;
import com.example.service.CaptchaService;
import com.example.service.EmailService;
import com.example.service.ProfileService;
import com.example.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final CheckAuthorizationResponse checkAuthorizationResponse;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final CaptchaService captchaService;
    private final RegisterService registerService;
    private final PostRepository postRepository;
    private final ProfileService profileService;
    private final EmailService emailService;

    @Autowired
    public ApiAuthController(CheckAuthorizationResponse checkAuthorizationResponse, AuthenticationManager authenticationManager, UserRepository userRepository, CaptchaService captchaService, RegisterService registerService, PostRepository postRepository, ProfileService profileService, EmailService emailService) {
        this.checkAuthorizationResponse = checkAuthorizationResponse;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.captchaService = captchaService;
        this.registerService = registerService;
        this.postRepository = postRepository;
        this.profileService = profileService;
        this.emailService = emailService;
    }

    @GetMapping("/check")
    public ResponseEntity<LoginResponse> check(Principal principal){

        if (principal == null){
            return ResponseEntity.ok(new LoginResponse());
        }
        return ResponseEntity.ok(getLoginResponse(principal.getName()));
    }

    @GetMapping("/logout")
    @PreAuthorize("hasAnyAuthority('user:write')")
    public ResponseEntity<CheckAuthorizationResponse> logout(){
        CheckAuthorizationResponse checkAuthorizationResponse = new CheckAuthorizationResponse();
        checkAuthorizationResponse.setResult(true);
        return ResponseEntity.ok(checkAuthorizationResponse);
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                                                                    loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();
        com.example.model.User currentUser = userRepository.findByEmail(user.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("User " + loginRequest.getEmail() + "not found"));
        LoginResponse loginResponse = getLoginResponse(user.getUsername());
        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/captcha")
    public CaptchaResponse captcha(){
        return captchaService.generateCaptcha();
    }

    private LoginResponse getLoginResponse(String email){
        com.example.model.User userRepo = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));

        UserLoginResponse userLoginResponse = new UserLoginResponse();
        userLoginResponse.setEmail(userRepo.getEmail());
        userLoginResponse.setName(userRepo.getName());
        userLoginResponse.setModearation(userRepo.isModerator());
        userLoginResponse.setId(userRepo.getId());
        userLoginResponse.setSettings(true);
        userLoginResponse.setPhoto(userRepo.getPhoto());
        if (userRepo.isModerator()){
            userLoginResponse.setModerationCount(postRepository
                    .findPostsByStatus(ModerationStatus.NEW).size());
        }
        else {
            userLoginResponse.setModerationCount(0);
        }
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setResult(true);
        loginResponse.setUserLoginResponse(userLoginResponse);
        return loginResponse;
    }

    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegisterRequest registerRequest){

        return registerService.checkUserInfo(registerRequest);

    }

    @PostMapping("/restore")
    public ResponseEntity<Error> restorePass(@RequestBody RestoreRequest restoreRequest){
        return profileService.restorePass(restoreRequest);
    }

    @PostMapping("/password")
    public ResponseEntity<Error> password(@RequestBody ChangePasswordRequest changePasswordRequest){
        return profileService.changePass(changePasswordRequest);
    }
}
