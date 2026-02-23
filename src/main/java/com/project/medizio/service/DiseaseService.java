package com.project.medizio.service;

import com.project.medizio.dto.PredictedResponse;
import com.project.medizio.entity.Disease;
import com.project.medizio.enums.DiseaseName;
import com.project.medizio.model.Diabetes;
import com.project.medizio.model.DiseaseInput;
import com.project.medizio.model.Heart;
import com.project.medizio.model.Parkinson;
import com.project.medizio.repository.DiseaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.management.InstanceNotFoundException;

@Service
@RequiredArgsConstructor
public class DiseaseService {

    private final DiseaseRepository diseaseRepository;
    private final RestTemplate restTemplate;
    private final PatientService patientService;

    private static final String BASE_URL = "http://127.0.0.1:8000/ai";

    /**
     * Generic AI caller
     */
    private Disease callAI(String endpoint, Object body, DiseaseName diseaseName, Long id) {
        try {
            String url = BASE_URL + endpoint;
            PredictedResponse predictedResponse = restTemplate.postForObject(
                    url,
                    body,
                    PredictedResponse.class
            );

            if (predictedResponse == null) {
                throw new RuntimeException("AI service returned empty response");
            }

            return new Disease(
                    predictedResponse.getResult(),
                    predictedResponse.getMessage(),
                    predictedResponse.getRiskClass(),
                    diseaseName,
                    patientService.getPatientById(id)
            );
        } catch (RestClientException e) {
            throw new RuntimeException("AI service failed", e);
        }
    }

    /**
     * Main Prediction Method
     */
    public Disease predictDisease(DiseaseInput diseaseInput, Long id) throws InstanceNotFoundException {

        Disease prediction = switch (diseaseInput) {
            case Diabetes diabetes -> callAI("/diabetes", diabetes, DiseaseName.DIABETES, id);
            case Heart heart -> callAI("/heart", heart, DiseaseName.HEART, id);
            case Parkinson parkinson -> callAI("/parkinson", parkinson, DiseaseName.PARKINSON, id);
            case null, default -> throw new InstanceNotFoundException("Disease not supported");
        };

        // Save prediction result in database
        return diseaseRepository.save(prediction);
    }
}
