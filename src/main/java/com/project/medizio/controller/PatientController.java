package com.project.medizio.controller;


import com.project.medizio.dto.Login;
import com.project.medizio.entity.Patient;
import com.project.medizio.service.PatientService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/patient")
@AllArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<Login> addPatient(@RequestBody Patient patient) {
        return ResponseEntity.ok(patientService.savePatient(patient));
    }
    @PostMapping("/login")
    public ResponseEntity<Login> loginPatient(@RequestBody Login login) {
        return  ResponseEntity.ok(patientService.loginPatient(login));
    }
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @GetMapping("/auth/token")
     public ResponseEntity<?> getUserByToken(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "").trim();


        try {
            Patient patient = patientService.getPatientByToken(token);
            return ResponseEntity.ok(patient);

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
    public void deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
}
}