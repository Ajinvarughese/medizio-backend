package com.project.medizio.controller;


import com.project.medizio.components.FileUpload;
import com.project.medizio.dto.DoctorRegisterRequest;
import com.project.medizio.dto.Login;
import com.project.medizio.entity.Doctor;
import com.project.medizio.entity.Patient;
import com.project.medizio.service.DoctorService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/doctor")
@AllArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;
    private final FileUpload fileUpload;


    @PostMapping(
        path = "/file/upload",
        consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<String>> addFiles(@RequestPart("file") List<MultipartFile> files) throws IOException {
        List<String> fileUrls = fileUpload.uploadFiles(files);
        return ResponseEntity.ok(fileUrls);
    }

    @PostMapping
    public ResponseEntity<Login> addDoctor(@RequestBody DoctorRegisterRequest doctorRegisterRequest) {
        return ResponseEntity.ok(doctorService.saveDoctor(doctorRegisterRequest));
    }

    @GetMapping
    public List<Doctor> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    @PostMapping("/login")
    public ResponseEntity<Login> loginDoctor(@RequestBody Login login) {
        return ResponseEntity.ok(doctorService.loginDoctor(login));
    }

    @PutMapping
    public ResponseEntity<Doctor> updateDoctor(@RequestBody Doctor updatedDoctor) {
        return ResponseEntity.ok(doctorService.updateDoctor(updatedDoctor));
    }


    @GetMapping("/auth/token")
    public ResponseEntity<?> getDoctor(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "").trim();


        try {
            Doctor doctor = doctorService.getDoctorByToken(token);
            return ResponseEntity.ok(doctor);

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(401).body(
                Map.of(
                    "status", 401,
                    "error", "UNAUTHORIZED",
                    "message", "Token expired. Please login again."
                )
            );
        }
    }

    @DeleteMapping("/{id}")
    public void deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
    }
}
