package com.project.medizio.controller;

import com.project.medizio.entity.Speciality;
import com.project.medizio.service.SpecialityService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/speciality")
@AllArgsConstructor
public class SpecialityController {
    private final SpecialityService specialityService;

    @GetMapping
    public ResponseEntity<List<Speciality>> fetchSpecialities() {
        return ResponseEntity.ok(specialityService.fetchAllSpecialities());
    }
}
