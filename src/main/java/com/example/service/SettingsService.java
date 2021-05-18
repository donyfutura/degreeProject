package com.example.service;

import com.example.api.request.SettingsRequest;
import com.example.api.response.SettingsResponse;
import com.example.model.GlobalSetting;
import com.example.repository.SettingsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SettingsService {

    private final SettingsRepository settingsRepository;

    public SettingsService(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    public SettingsResponse getSettings(){
        List<GlobalSetting> globalSettingList = settingsRepository.findAll();

        SettingsResponse settingsResponse = new SettingsResponse();

        GlobalSetting mult = settingsRepository.findById(1).get();
        settingsResponse.setMultiuserMode(mult.isValue());

        GlobalSetting premoderation = settingsRepository.findById(2).get();
        settingsResponse.setPostPremoderation(premoderation.isValue());

        GlobalSetting stats = settingsRepository.findById(3).get();
        settingsResponse.setStatisticsIsPublic(stats.isValue());

        System.out.println(settingsResponse.isMultiuserMode() + " " + settingsResponse.isPostPremoderation() + " " + settingsResponse.isStatisticsIsPublic());

        return settingsResponse;
    }

    public Map<String, Boolean> editSettings(SettingsRequest settingsRequest){

        GlobalSetting mult = settingsRepository.findByCodeEquals("MULTIUSER_MODE");
        mult.setValue(settingsRequest.isMultiUser());
        settingsRepository.save(mult);

        GlobalSetting premoderation = settingsRepository.findByCodeEquals("POST_PREMODERATION");
        premoderation.setValue(settingsRequest.isPreModeration());
        settingsRepository.save(premoderation);

        GlobalSetting stats = settingsRepository.findByCodeEquals("STATISTICS_IS_PUBLIC");
        stats.setValue(settingsRequest.isPublicStats());
        settingsRepository.save(stats);

        return Map.of("result", true);
    }

}
