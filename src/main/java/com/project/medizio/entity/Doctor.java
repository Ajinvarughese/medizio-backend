package com.project.medizio.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.medizio.components.EntityDetails;
import com.project.medizio.enums.DaysOfWeek;
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

        private String name;
        @Column(unique = true, nullable = false)
        private String email;
        private String password;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "speciality_id", nullable = false)
        private Speciality speciality;

        private Boolean availability;
        private String picture;
        private Integer experience;
        private String startTime;
        private String endTime;
        private String document;

        private String location;
        private String dob;

        @ElementCollection
        private List<String> unavailableDates;

        @ElementCollection(targetClass = DaysOfWeek.class)
        @Enumerated(EnumType.STRING)
        @CollectionTable(name = "doctor_unavailable_days",
                         joinColumns = @JoinColumn(name = "doctor_id"))
        @Column(name = "day")
        private List<DaysOfWeek> unavailableDays;
}

