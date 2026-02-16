package com.project.medizio.service;

import com.project.medizio.components.JwtUtil;
import com.project.medizio.dto.Login;
import com.project.medizio.entity.Doctor;
import com.project.medizio.entity.Patient;
import com.project.medizio.repository.DoctorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public String saveDoctor(Doctor doctor) {
        doctorRepository.save(doctor);
        return jwtUtil.generateToken(doctor.getEmail());
    }

    public Login loginDoctor(Login login) {
        Doctor existing = doctorRepository.findByEmail(login.getLoginId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: "+login.getLoginId()));
        if(passwordEncoder.matches(login.getPassword(), existing.getPassword())) {
            return new Login("", jwtUtil.generateToken(existing.getEmail()));
        }
        throw new IllegalArgumentException("User id or password must be wrong");
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor getDoctorByToken(String token) {
        String email = jwtUtil.extractKey(token);
        return doctorRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Doctor not found"));
    }

    public Doctor updateDoctor(Long id, Doctor updatedDoctor) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with id: " + id));

        // update only availability fields
        doctor.setAvailability(updatedDoctor.getAvailability());
        doctor.setFromTime(updatedDoctor.getFromTime());
        doctor.setToTime(updatedDoctor.getToTime());


        doctor.setUnavailableDates(updatedDoctor.getUnavailableDates());

        return doctorRepository.save(doctor);
    }

    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
}
}