package com.project.medizio.dto;

import com.project.mediquest.entities.Doctor;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Availability {
    private String date;
    private String time;
}
