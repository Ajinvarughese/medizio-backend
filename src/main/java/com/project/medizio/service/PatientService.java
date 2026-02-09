package com.project.medizio.service;



import com.project.medizio.components.JwtUtil;
import com.project.medizio.dto.Login;
import com.project.medizio.entity.Patient;
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
        patientRepository.save(patient);
        return new Login("", jwtUtil.generateToken(patient));
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatientById(Long id) {
        return patientRepository.findById(id).orElse(null);
    }

    public Patient getPatientByToken(String token) {
        String phone = jwtUtil.extractPhone(token);
        return  patientRepository.findByPhone(phone).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public Login loginPatient(Login login) {
        Patient existing = patientRepository.findByPhone(login.getLoginId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: "+login.getLoginId()));
        if(existing.getPassword().equals(login.getPassword())) {
            return new Login("", jwtUtil.generateToken(existing));
        }
        throw new IllegalArgumentException("User name or password is wrong");
    }

    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }
}