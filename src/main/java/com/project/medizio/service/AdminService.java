package com.project.medizio.service;

import com.project.medizio.components.JwtUtil;
import com.project.medizio.dto.Login;
import com.project.medizio.entity.Admin;
import com.project.medizio.repository.AdminRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public Login saveAdmin(Admin admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        adminRepository.save(admin);
        return new Login("", jwtUtil.generateToken(admin.getEmail()));
    }

    public Login loginAdmin(Login login) {
        Admin existing = adminRepository.findByEmail(login.getLoginId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: "+login.getLoginId()));
        Login res = new Login();
        if(passwordEncoder.matches(login.getPassword(), existing.getPassword())) {
            res.setLoginId("");
            res.setPassword(jwtUtil.generateToken(existing.getEmail()));
            return res;
        }
        throw new IllegalArgumentException("Email id or password must be wrong");
    }

    public Admin getAdmin(String token) {
        String email = jwtUtil.extractKey(token);
        return adminRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Admin not found"));
    }

}
