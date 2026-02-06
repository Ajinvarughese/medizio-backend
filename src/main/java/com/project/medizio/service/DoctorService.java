package com.project.medizio.service;

import com.project.medizio.entity.Doctor;
import com.project.medizio.repository.DoctorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;

    public Doctor saveDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public Doctor loginDoctor(Doctor doctor) {
        Doctor existing = doctorRepository.findByDoctorId(doctor.getDoctorId())
                .orElseThrow(() -> new EntityNotFoundException(("Doctor not found with id: "+doctor.getDoctorId())));
        if(existing.getDoctorId().equals(doctor.getDoctorId()) && existing.getPassword().equals(doctor.getPassword())) {
            return existing;
        }
        throw new IllegalArgumentException("User id or password must be wrong");
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Doctor not found"));
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