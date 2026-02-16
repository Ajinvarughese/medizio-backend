package com.project.medizio.entity;

import com.project.medizio.components.EntityDetails;
import com.project.medizio.enums.DiseaseName;
import com.project.medizio.enums.RiskClass;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Disease extends EntityDetails {

    private boolean affected;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String aiAnalysis;

    @Enumerated(EnumType.STRING)
    private RiskClass riskClass;

    private DiseaseName disease;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private Patient patient;
}
