package com.example.service;

import com.example.api.request.RegisterRequest;
import com.example.api.response.RegisterResponse;
import com.example.model.User;
import com.example.repository.CaptchaRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;

@Service
public class RegisterService {

    private final UserRepository userRepository;
    private final CaptchaRepository captchaRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegisterService(UserRepository userRepository, CaptchaService captchaService, CaptchaRepository captchaRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.captchaRepository = captchaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public RegisterResponse checkUserInfo(RegisterRequest registerRequest){
        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setResult(true);
        HashMap<String, String> errors = new HashMap<>();

        if (captchaRepository.findBySecretCode(registerRequest.getCaptcha()) == null){
            errors.put("captcha", "Код с картинки введён неверно");
            registerResponse.setResult(false);
        }
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()){
            errors.put("email", "Этот e-mail уже зарегистрирован");
            registerResponse.setResult(false);
        }
        if (registerRequest.getPassword().length() < 6){
            errors.put("password", "Пароль короче 6-ти символов");
            registerResponse.setResult(false);
        }
        registerResponse.setErrors(errors);
        User user = new User();
        user.setDate(new Date());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setName(registerRequest.getName());
        userRepository.save(user);
        return registerResponse;

    }


}
