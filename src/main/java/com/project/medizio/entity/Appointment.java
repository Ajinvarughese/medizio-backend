package com.project.medizio.entity;


import com.project.medizio.components.EntityDetails;
import com.project.medizio.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.*;



@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Appointment extends EntityDetails {

    private String date;
    private String time;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String reason;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String note;

    private String document;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
}