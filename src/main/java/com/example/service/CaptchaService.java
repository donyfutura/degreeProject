package com.example.service;

import com.example.api.response.CaptchaResponse;
import com.example.model.CaptchaCode;
import com.example.repository.CaptchaRepository;
import com.github.cage.Cage;
import com.github.cage.GCage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;

@Service
public class CaptchaService {

    private final CaptchaRepository captchaRepository;

    @Autowired
    public CaptchaService(CaptchaRepository captchaRepository) {
        this.captchaRepository = captchaRepository;
    }

    public CaptchaResponse generateCaptcha(){
        Cage cage = new GCage();
        String secret = cage.getTokenGenerator().next();
        String image = Base64.getEncoder().encodeToString(cage.draw(secret));
        System.out.println("data:image/png;base64, " + image);
        CaptchaCode captchaCode = new CaptchaCode();
        captchaCode.setCode("data:image/png;base64, ");
        captchaCode.setSecretCode(secret);
        captchaCode.setDate(new Date());
        captchaRepository.save(captchaCode);
        return new CaptchaResponse(secret, "data:image/png;base64, " + image);
    }

}
