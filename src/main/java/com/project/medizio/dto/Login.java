package com.project.medizio.dto;

import com.project.mediquest.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Login {
    private String name;
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Boolean encrypted = Boolean.valueOf("false");
}
