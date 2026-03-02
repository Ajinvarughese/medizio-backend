package com.project.medizio.controller;

import com.project.medizio.dto.Login;
import com.project.medizio.entity.Admin;
import com.project.medizio.service.AdminService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/admin")
@AllArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping
    public ResponseEntity<Login> saveAdmin(@RequestBody Admin admin) {
        return ResponseEntity.ok(adminService.saveAdmin(admin));
    }

    @PostMapping("/login")
    public ResponseEntity<Login> loginAdmin(@RequestBody Login login) {
        return ResponseEntity.ok(adminService.loginAdmin(login));
    }

    @GetMapping("/auth/token")
    public ResponseEntity<?> getAdmin(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "").trim();


        try {
            Admin admin = adminService.getAdmin(token);
            return ResponseEntity.ok(admin);

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
}
