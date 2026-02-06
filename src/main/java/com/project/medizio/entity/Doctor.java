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
public class Doctor extends EntityDetails {

        @Column(unique = true)
        private Long doctorId;
        private String password;

        private String specialization;
        private Boolean availability;
        private String picture;
        private Integer experience;
        private String fromTime;
        private String toTime;
        private String document;
        @ElementCollection
        private List<String> unavailableDates;
}

