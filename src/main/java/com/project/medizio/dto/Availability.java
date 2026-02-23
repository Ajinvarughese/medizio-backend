package com.project.medizio.dto;


import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Availability {
    private Long doctorId;
    private String date;
    private String time;
}
