package com.project.medizio.configuration;


import com.project.medizio.dto.DoctorRegisterRequest;
import com.project.medizio.entity.Doctor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
