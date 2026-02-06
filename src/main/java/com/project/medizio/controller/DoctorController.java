package com.project.medizio.controller;


import com.project.medizio.components.FileUpload;
import com.project.medizio.entity.Doctor;
import com.project.medizio.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {
    private final DoctorService doctorService;
    private final FileUpload fileUpload;

    @Autowired
    public DoctorController(DoctorService doctorService, FileUpload fileUpload) {
        this.doctorService = doctorService;
        this.fileUpload = fileUpload;
    }

    @PostMapping(
        path = "/file/upload",
        consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<String>> addFiles(@RequestParam("file") List<MultipartFile> files) throws IOException {
        List<String> fileUrls = fileUpload.uploadFiles(files);
        return ResponseEntity.ok(fileUrls);
    }

    @PostMapping
    public ResponseEntity<Doctor> addDoctor(@RequestBody Doctor doctor) {
        return ResponseEntity.ok(doctorService.saveDoctor(doctor));
    }

    @GetMapping
    public List<Doctor> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    @PostMapping("/login")
    public ResponseEntity<Doctor> loginDoctor(@RequestBody Doctor doctor) {
        return ResponseEntity.ok(doctorService.loginDoctor(doctor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable Long id, @RequestBody Doctor updatedDoctor) {
        return ResponseEntity.ok(doctorService.updateDoctor(id, updatedDoctor));
    }


    @GetMapping("/{id}")
    public Doctor getDoctor(@PathVariable Long id) {
        return doctorService.getDoctorById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
    }
}
