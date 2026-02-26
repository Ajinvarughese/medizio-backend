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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.management.InstanceNotFoundException;
import java.io.IOException;
import java.util.List;

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
                    predictedResponse.getConfidence(),
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

        // Save prediction result in database
        return switch (diseaseInput) {
            case Diabetes diabetes -> callAI("/diabetes", diabetes, DiseaseName.DIABETES, id);
            case Heart heart -> callAI("/heart", heart, DiseaseName.HEART, id);
            case Parkinson parkinson -> callAI("/parkinson", parkinson, DiseaseName.PARKINSON, id);
            case null, default -> throw new InstanceNotFoundException("Disease not supported");
        };
    }

    public Disease saveDisease(Disease disease) {
        return diseaseRepository.save(disease);
    }

    public List<Disease> fetchDiseaseByUser(Long id) {
        return diseaseRepository.findByPatientId(id);
    }

    public DiseaseInput extractDetailsFromPdf(MultipartFile file, String disease) throws IOException {

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://127.0.0.1:8000/file/extract?disease=" + disease;

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> request =
                new HttpEntity<>(body, headers);

        return switch (disease.toLowerCase()) {
            case "diabetes" -> restTemplate.postForObject(url, request, Diabetes.class);
            case "heart" -> restTemplate.postForObject(url, request, Heart.class);
            case "parkinson" -> restTemplate.postForObject(url, request, Parkinson.class);
            default -> throw new IllegalArgumentException("Invalid disease type");
        };
    }
}
