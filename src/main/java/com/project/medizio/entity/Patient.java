package com.project.medizio.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.medizio.components.EntityDetails;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Patient extends EntityDetails {
        private String name;
        private String password;

        @Column(unique = true, nullable = false)
        private String phone;
        private String dob;
}
