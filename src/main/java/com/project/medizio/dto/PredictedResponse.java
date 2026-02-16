package com.project.medizio.dto;

import com.project.medizio.enums.RiskClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PredictedResponse {
    private Boolean result;
    private String message;
    private RiskClass riskClass;
}
