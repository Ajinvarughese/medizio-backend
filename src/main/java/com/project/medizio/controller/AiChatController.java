package com.project.medizio.controller;

import com.project.medizio.entity.AiChat;
import com.project.medizio.entity.Patient;
import com.project.medizio.enums.TextFrom;
import com.project.medizio.service.AiChatService;
import com.project.medizio.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/ai/chat")
@RequiredArgsConstructor
public class AiChatController {

    private final AiChatService aiChatService;
    private final PatientService patientService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AiChat> newChatWithAI(
            @RequestParam("text") String text,
            @RequestParam("userId") Long userId,
            @RequestPart(value = "patient_report", required = false) MultipartFile patientReport
    ) throws IOException {

        Patient patient = patientService.getUserById(userId);

        AiChat aiChat = new AiChat();
        aiChat.setText(text);
        aiChat.setPatient(patient);
        aiChat.setTextFrom(TextFrom.USER);

        return ResponseEntity.ok(
                aiChatService.generateAiResponse(aiChat, patientReport)
        );
    }

    @GetMapping
    public ResponseEntity<List<AiChat>> getUserChat(
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "").trim();
        Patient user = patientService.getPatientByToken(token);
        return ResponseEntity.ok(aiChatService.getAllChatByUser(user.getId()));
    }

    @DeleteMapping
    public void deleteChatsByUser(
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "").trim();
        Patient patient = patientService.getPatientByToken(token);
        aiChatService.deleteAllChatsByUserId(patient.getId());
    }

    @DeleteMapping("/{id}")
    public void deleteChat(@PathVariable Long id) {
        aiChatService.deleteChat(id);
    }
}
