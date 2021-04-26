package com.example.service;

import com.example.api.response.SettingsResponse;
import com.example.model.GlobalSetting;
import com.example.repository.SettingsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingsService {

    private final SettingsRepository settingsRepository;

    public SettingsService(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    public SettingsResponse getSettings(){
        List<GlobalSetting> globalSettingList = settingsRepository.findAll();

        SettingsResponse settingsResponse = new SettingsResponse();

        settingsResponse.setMultiuserMode(true);
        settingsResponse.setStatisticsIsPublic(true);
        settingsResponse.setPostPremoderation(true);

        System.out.println(settingsResponse.isMultiuserMode() + " " + settingsResponse.isPostPremoderation() + " " + settingsResponse.isStatisticsIsPublic());

        return settingsResponse;
    }

}
