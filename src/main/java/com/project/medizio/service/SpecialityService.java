package com.project.medizio.service;

import com.project.medizio.entity.Speciality;
import com.project.medizio.repository.SpecialityRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpecialityService {
    private final SpecialityRepository specialityRepository;

    public List<Speciality> fetchAllSpecialities() {
        return specialityRepository.findAll();
    }

    public Speciality fetchSpeciality(Long id) {
        return specialityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Speciality not found"));
    }
}
