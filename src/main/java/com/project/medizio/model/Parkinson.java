package com.project.medizio.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Parkinson implements DiseaseInput {

    private double fo;
    private double fhi;
    private double flo;

    @JsonProperty("Jitter_percent")
    private double jitterPercent;

    @JsonProperty("Jitter_Abs")
    private double jitterAbs;

    @JsonProperty("RAP")
    private double rap;

    @JsonProperty("PPQ")
    private double ppq;

    @JsonProperty("DDP")
    private double ddp;

    @JsonProperty("Shimmer")
    private double shimmer;

    @JsonProperty("Shimmer_dB")
    private double shimmerDb;

    @JsonProperty("APQ3")
    private double apq3;

    @JsonProperty("APQ5")
    private double apq5;

    @JsonProperty("APQ")
    private double apq;

    @JsonProperty("DDA")
    private double dda;

    @JsonProperty("NHR")
    private double nhr;

    @JsonProperty("HNR")
    private double hnr;

    @JsonProperty("RPDE")
    private double rpde;

    @JsonProperty("DFA")
    private double dfa;

    private double spread1;
    private double spread2;

    @JsonProperty("D2")
    private double d2;

    @JsonProperty("PPE")
    private double ppe;
}

