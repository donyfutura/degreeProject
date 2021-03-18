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

        for(GlobalSetting s : globalSettingList){
            if (s.getName().equals("MULTIUSER_MODE")){
                settingsResponse.setMultiuserMode(s.isValue());
            }
            if (s.getName().equals("POST_PREMODERATION")){
                settingsResponse.setPostPremoderation(s.isValue());
            }
            if (s.getName().equals("STATISTICS_IS_PUBLIC")){
                settingsResponse.setStatisticsIsPublic(s.isValue());
            }
        }

        return settingsResponse;
    }

}
