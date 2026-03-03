package com.project.medizio.repository;

import com.project.medizio.entity.OTP;
import com.project.medizio.enums.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface OtpRepository extends JpaRepository<OTP, Long> {
    void deleteByCreatedAtBefore(LocalDateTime time);
    Optional<OTP> findByEmailAndUserType(String email, UserType userType);
}
