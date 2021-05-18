package com.example.repository;

import com.example.model.GlobalSetting;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SettingsRepository extends CrudRepository<GlobalSetting, Integer> {

    List<GlobalSetting> findAll();

    GlobalSetting findByCodeEquals(String code);

}
