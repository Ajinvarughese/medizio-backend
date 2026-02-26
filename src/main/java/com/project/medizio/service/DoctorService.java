package com.project.medizio.service;

import com.project.medizio.components.JwtUtil;
import com.project.medizio.dto.DoctorRegisterRequest;
import com.project.medizio.dto.Login;
import com.project.medizio.entity.Doctor;
import com.project.medizio.entity.Patient;
import com.project.medizio.entity.Speciality;
import com.project.medizio.repository.DoctorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final SpecialityService specialityService;

    public Login saveDoctor(DoctorRegisterRequest request) {
        Speciality speciality = specialityService.fetchSpeciality(request.getSpecialityId());
        Doctor doctor = new Doctor();

        doctor.setName(request.getName());
        doctor.setEmail(request.getEmail());
        doctor.setPassword(passwordEncoder.encode(request.getPassword()));
        doctor.setPicture(request.getPicture());
        doctor.setLocation(request.getLocation());
        doctor.setDob(request.getDob());
        doctor.setExperience(request.getExperience());
        doctor.setStartTime("9:00 AM");
        doctor.setEndTime("6:00 PM");
        doctor.setAvailability(true);

        doctor.setSpeciality(speciality);

        doctorRepository.save(doctor);
        return new Login("", jwtUtil.generateToken(doctor.getEmail()));
    }

    public Login loginDoctor(Login login) {

        Doctor existing = doctorRepository.findByEmail(login.getLoginId())
                .orElseThrow(() ->
                        new EntityNotFoundException("User not found with id: " + login.getLoginId())
                );
        if (passwordEncoder.matches(login.getPassword(), existing.getPassword())) {
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

    public Doctor updateDoctor(Doctor updatedDoctor) {
        Doctor doctor = doctorRepository.findById(updatedDoctor.getId())
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with id: " + updatedDoctor.getId()));

        // update only availability fields
        doctor.setUnavailableDays(updatedDoctor.getUnavailableDays());
        doctor.setAvailability(updatedDoctor.getAvailability());
        doctor.setStartTime(updatedDoctor.getStartTime());
        doctor.setEndTime(updatedDoctor.getEndTime());


        doctor.setUnavailableDates(updatedDoctor.getUnavailableDates());

        return doctorRepository.save(doctor);
    }

    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
}
}