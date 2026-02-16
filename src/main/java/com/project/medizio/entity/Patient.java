package com.project.medizio.entity;


import com.project.medizio.components.EntityDetails;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Patient extends EntityDetails {
        private String name;
        @Column(unique = true, nullable = false)
        private String email;
        private String password;
        private String dob;
}
