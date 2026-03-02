package com.project.medizio.entity;

import com.project.medizio.components.EntityDetails;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Admin extends EntityDetails {

    @Column(unique = true, nullable = false)
    private String email;

    private String password;
}
