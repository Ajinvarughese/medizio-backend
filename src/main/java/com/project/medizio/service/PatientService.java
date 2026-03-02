package com.project.medizio.service;



import com.project.medizio.components.JwtUtil;
import com.project.medizio.dto.Login;
import com.project.medizio.entity.Patient;
import com.project.medizio.enums.AccountStatus;
import com.project.medizio.exception.AccountSuspended;
import com.project.medizio.exception.DoctorNotVerified;
import com.project.medizio.repository.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public Login savePatient(Patient patient) {
        patient.setPassword(passwordEncoder.encode(patient.getPassword()));
        patient.setAccountStatus(AccountStatus.ACTIVE);
        patientRepository.save(patient);
        return new Login("", jwtUtil.generateToken(patient.getEmail()));
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatientById(Long id) {

        return patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: "+id));
    }

    public Patient getPatientByToken(String token) {
        String email = jwtUtil.extractKey(token);
        Patient patient = patientRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found"));

        if(patient.getAccountStatus() == AccountStatus.SUSPENDED)
            throw new AccountSuspended("Patient is suspended");

        return patient;
    }

    public Login loginPatient(Login login) {
        Patient existing = patientRepository.findByEmail(login.getLoginId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: "+login.getLoginId()));

        Login res = new Login();
        if(passwordEncoder.matches(login.getPassword(), existing.getPassword())) {
            res.setLoginId("");
            res.setPassword(jwtUtil.generateToken(existing.getEmail()));
        } else {
           throw new IllegalArgumentException("User name or password is wrong");
        }

        if(existing.getAccountStatus() == AccountStatus.SUSPENDED)
            throw new AccountSuspended("Account temporarily suspended");

        return res;
    }

    public Patient updatePatientStatus(Patient patient) {
        Patient existing = getPatientById(patient.getId());
        existing.setAccountStatus(patient.getAccountStatus());
        return patientRepository.save(existing);
    }

    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }
}