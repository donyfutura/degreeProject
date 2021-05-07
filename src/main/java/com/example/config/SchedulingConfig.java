package com.example.config;


import com.example.model.CaptchaCode;
import com.example.repository.CaptchaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Calendar;

@Configuration
@EnableScheduling
public class SchedulingConfig {

    private final CaptchaRepository captchaRepository;

    @Autowired
    public SchedulingConfig(CaptchaRepository captchaRepository) {
        this.captchaRepository = captchaRepository;
    }

    @Scheduled(fixedRate = 600000)
    public void checkCatches(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -60);

        Iterable<CaptchaCode> captchaCodes = captchaRepository.findAll();
        for(CaptchaCode captchaCode : captchaCodes){
            if (captchaCode.getDate().before(calendar.getTime())){
                captchaRepository.delete(captchaCode);
            }
        }
    }
}
