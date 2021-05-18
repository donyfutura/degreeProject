package com.example.service;

import com.example.api.request.ChangePasswordRequest;
import com.example.api.request.EditProfileRequest;
import com.example.api.request.RestoreRequest;
import com.example.api.response.Error;
import com.example.model.CaptchaCode;
import com.example.model.User;
import com.example.repository.CaptchaRepository;
import com.example.repository.UserRepository;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;
    private final EmailService emailService;
    private final CaptchaRepository captchaRepository;

    @Autowired
    public ProfileService(UserRepository userRepository, PasswordEncoder passwordEncoder, ImageService imageService, EmailService emailService, CaptchaRepository captchaRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.imageService = imageService;
        this.emailService = emailService;
        this.captchaRepository = captchaRepository;
    }


    public ResponseEntity<Error> editProfileWithPhoto(String email, int removePhoto, MultipartFile file, String name, String password, Authentication authentication) throws IOException {
        Error error = new Error();
        error.setResult(true);
        Map<String, String> errors = new HashMap<>();
        User user = userRepository.findByEmail(authentication.getName()).get();
        EditProfileRequest editProfileRequest = new EditProfileRequest(file, name, email, password, removePhoto);

        if (removePhoto == 1){
            user.setPhoto(null);
        }

        if (file.getSize() > 5_242_880){
            error.setResult(false);
            errors.put("photo", "Фото слишком большое, нужно не более 5 Мб");
        } else {
            String path = imageService.generatePath(file.getBytes(), file);
            File fileImg = new File(path);
            BufferedImage bufferedImage = ImageIO.read(fileImg);
            BufferedImage newBufferedImage = Scalr.resize(bufferedImage,36,36);
            ImageIO.write(newBufferedImage, fileImg.getName().split("\\.")[1], fileImg);
            System.out.println("OK");
            user.setPhoto(path);
        }

        if (editProfileRequest.getName() != null){
            if (editProfileRequest.getName().length() < 5){
                error.setResult(false);
                errors.put("name", "Имя указано неверно");
            } else {
                user.setName(editProfileRequest.getName());
            }
        }

        if (!user.getEmail().equals(editProfileRequest.getEmail())){
            if (userRepository.findByEmail(editProfileRequest.getEmail()).isPresent()){
                error.setResult(false);
                errors.put("email", "Этот e-mail уже зарегистрирован");
            }
            else {
                user.setEmail(editProfileRequest.getEmail());
            }
        }

        if (editProfileRequest.getPassword() != null){
            if (editProfileRequest.getPassword().length() < 6){
                error.setResult(false);
                errors.put("password", "Пароль короче 6-ти символов");
            } else {
                user.setPassword(passwordEncoder.encode(editProfileRequest.getPassword()));
            }
        }

        if (error.isResult()){
            userRepository.save(user);
        } else {
            error.setErrors(errors);
        }

        userRepository.save(user);
        return new ResponseEntity<>(error, HttpStatus.OK);
    }

    public ResponseEntity<Error> editProfileWithoutPhoto(EditProfileRequest editProfileRequest, Authentication authentication) {
        Error error = new Error();
        error.setResult(true);
        Map<String, String> errors = new HashMap<>();
        User user = userRepository.findByEmail(authentication.getName()).get();

        if (editProfileRequest.getName() != null){
            if (editProfileRequest.getName().length() < 5){
                error.setResult(false);
                errors.put("name", "Имя указано неверно");
            } else {
                user.setName(editProfileRequest.getName());
            }
        }

        if (!user.getEmail().equals(editProfileRequest.getEmail())){
            if (userRepository.findByEmail(editProfileRequest.getEmail()).isPresent()){
                error.setResult(false);
                errors.put("email", "Этот e-mail уже зарегистрирован");
            }
            else {
                user.setEmail(editProfileRequest.getEmail());
            }
        }

        if (editProfileRequest.getPassword() != null){
            if (editProfileRequest.getPassword().length() < 6){
                error.setResult(false);
                errors.put("password", "Пароль короче 6-ти символов");
            } else {
                user.setPassword(passwordEncoder.encode(editProfileRequest.getPassword()));
            }
        }
        if (editProfileRequest.getRemovePhoto() == 1){
            user.setPhoto(null);
        }

        if (error.isResult()){
            userRepository.save(user);
        } else {
            error.setErrors(errors);
        }

        return new ResponseEntity<>(error, HttpStatus.OK);
    }


    public ResponseEntity<Error> restorePass(RestoreRequest restoreRequest) {
        Error error = new Error();
        Optional<User> user = userRepository.findByEmail(restoreRequest.getEmail());
        if (user.isPresent()){
            error.setResult(true);
            User userRepo = user.get();
            String code = UUID.randomUUID().toString();
            userRepo.setCode(code);
            emailService.send(restoreRequest.getEmail(), "Test", "http://localhost:8080/login/change-password/" + code);
            userRepository.save(userRepo);
        } else {
            error.setResult(false);
        }
        return new ResponseEntity<>(error, HttpStatus.OK);
    }

    public ResponseEntity<Error> changePass(ChangePasswordRequest changePasswordRequest){
        Error error = new Error();
        Map<String, String> errors = new HashMap<>();
        error.setResult(true);
        Optional<User> user = userRepository.findByCode(changePasswordRequest.getCode());
        CaptchaCode captchaCode = captchaRepository.findBySecretCode(changePasswordRequest.getCaptchaSecret());
        if (changePasswordRequest.getCaptcha().equals(changePasswordRequest.getCaptchaSecret())
                && user.isPresent()
                && changePasswordRequest.getPassword().length() > 5
                && captchaCode != null){
            captchaRepository.delete(captchaCode);
            User userRepo = user.get();
            userRepo.setCode(null);
            userRepo.setPassword(passwordEncoder.encode(changePasswordRequest.getPassword()));
            userRepository.save(userRepo);
            return new ResponseEntity<>(error, HttpStatus.OK);
        } else if (changePasswordRequest.getPassword().length() < 6){
            error.setResult(false);
            errors.put("password", "Пароль короче 6-ти символов");
        } if (!changePasswordRequest.getCaptcha().equals(changePasswordRequest.getCaptchaSecret())){
            error.setResult(false);
            errors.put("captcha", "Код с картинки введен неверно");
        } if (user.isEmpty()){
            error.setResult(false);
            errors.put("code", "Ссылка для восстановления пароля устарела." +
                    "        <a href=        \\\"/auth/restore\\\">Запросить ссылку снова</a>");
        }
        error.setErrors(errors);

        return new ResponseEntity<>(error, HttpStatus.OK);

    }
}
