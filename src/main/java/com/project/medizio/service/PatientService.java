package com.project.medizio.service;



import com.project.medizio.entity.Patient;
import com.project.medizio.repository.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {
    @Autowired
    private PatientRepository patientRepository;

    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatientById(Long id) {
        return patientRepository.findById(id).orElse(null);
    }

    public Patient loginPatient(Patient patient) {
        Patient existing = patientRepository.findByPhone(patient.getPhone())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: "+patient.getPhone()));
        if(existing.getPhone().equals(patient.getPhone()) && existing.getPassword().equals(patient.getPassword())) {
            return existing;
        }
        throw new IllegalArgumentException("User name or password is wrong");
    }

    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }
}