package com.project.medizio.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Heart implements DiseaseInput {

    private double age;
    private double sex;
    private double cp;
    private double trestbps;
    private double chol;
    private double fbs;
    private double restecg;
    private double thalach;
    private double exang;
    private double oldpeak;
    private double slope;
    private double ca;
    private double thal;
}
