package com.example.repository;

import com.example.model.CaptchaCode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaptchaRepository extends CrudRepository<CaptchaCode, Integer> {

    CaptchaCode findBySecretCode(String secret);

}
