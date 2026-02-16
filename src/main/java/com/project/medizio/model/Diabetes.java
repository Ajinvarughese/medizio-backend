package com.project.medizio.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Diabetes implements DiseaseInput {

    @JsonProperty("Pregnancies")
    private double pregnancies;

    @JsonProperty("Glucose")
    private double glucose;

    @JsonProperty("BloodPressure")
    private double bloodPressure;

    @JsonProperty("SkinThickness")
    private double skinThickness;

    @JsonProperty("Insulin")
    private double insulin;

    @JsonProperty("BMI")
    private double bmi;

    @JsonProperty("DiabetesPedigreeFunction")
    private double diabetesPedigreeFunction;

    @JsonProperty("Age")
    private double age;
}

