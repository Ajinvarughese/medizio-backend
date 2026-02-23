package com.project.medizio.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DoctorRegisterRequest {
    private String name;
    private String email;
    private String password;
    private String location;
    private String dob;
    private String picture;
    private Integer experience;
    private Long specialityId;
}