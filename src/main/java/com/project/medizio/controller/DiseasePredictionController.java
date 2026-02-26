package com.project.medizio.controller;

import com.project.medizio.entity.Disease;
import com.project.medizio.entity.Patient;
import com.project.medizio.model.Diabetes;
import com.project.medizio.model.DiseaseInput;
import com.project.medizio.model.Heart;
import com.project.medizio.model.Parkinson;
import com.project.medizio.service.DiseaseService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.management.InstanceNotFoundException;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/disease")
@AllArgsConstructor
public class DiseasePredictionController {
    private final DiseaseService diseaseService;

    @PostMapping("/predict/diabetes")
    public ResponseEntity<Disease> predictDiabetes(
        @RequestBody Diabetes diabetes,
        @RequestParam Long patientId) throws InstanceNotFoundException {
        return ResponseEntity.ok(diseaseService.predictDisease(diabetes, patientId));
    }

    @PostMapping("/predict/heart")
    public ResponseEntity<Disease> predictHeart(@RequestBody Heart heart, @RequestParam Long patientId) throws InstanceNotFoundException {
        return ResponseEntity.ok(diseaseService.predictDisease(heart, patientId));
    }

    @PostMapping("/predict/parkinson")
    public ResponseEntity<Disease> predictParkinson(@RequestBody Parkinson parkinson, @RequestParam Long patientId) throws InstanceNotFoundException {
        return ResponseEntity.ok(diseaseService.predictDisease(parkinson, patientId));
    }

    @PostMapping(path = "/file/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DiseaseInput> uploadDoc(
            @RequestParam("file") MultipartFile file,
            @RequestParam("disease") String disease
    ) throws IOException {

        DiseaseInput result = diseaseService.extractDetailsFromPdf(file, disease);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Disease> saveDisease(@RequestBody Disease disease) {
        return ResponseEntity.ok(diseaseService.saveDisease(disease));
    }

    @GetMapping
    public ResponseEntity<List<Disease>> fetchAllDisease(@RequestParam Long patientId) {
        return ResponseEntity.ok(diseaseService.fetchDiseaseByUser(patientId));
    }
}
