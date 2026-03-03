package com.project.medizio.entity;

import com.project.medizio.components.EntityDetails;
import com.project.medizio.enums.UserType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OTP extends EntityDetails {
    private String otp;
    private String email;

    @Enumerated(EnumType.STRING)
    private UserType userType;
}


