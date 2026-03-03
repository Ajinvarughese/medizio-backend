package com.project.medizio.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.medizio.components.EntityDetails;
import com.project.medizio.enums.TextFrom;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AiChat extends EntityDetails {

    @Column(columnDefinition = "MEDIUMTEXT")
    private String text;

    @Enumerated(EnumType.STRING)
    private TextFrom textFrom;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    @JsonIgnore
    private Patient patient;

}
