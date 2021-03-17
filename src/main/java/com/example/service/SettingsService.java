package com.example.service;

import com.example.api.response.SettingsResponse;
import com.example.repository.SettingsRepository;
import org.springframework.stereotype.Service;

@Service
public class SettingsService {

    private final SettingsRepository settingsRepository;

    public SettingsService(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    public SettingsResponse getSettings(){
        SettingsResponse settingsResponse = new SettingsResponse();
        settingsResponse.setMultiuserMode(settingsRepository.findByCode("MULTIUSER_MODE").isValue());
        settingsResponse.setMultiuserMode(settingsRepository.findByCode("POST_PREMODERATION").isValue());
        settingsResponse.setMultiuserMode(settingsRepository.findByCode("STATISTICS_IS_PUBLIC").isValue());
        return settingsResponse;
    }

}
