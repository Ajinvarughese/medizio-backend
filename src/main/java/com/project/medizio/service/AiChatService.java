package com.project.medizio.service;

import com.project.medizio.dto.ChatLog;
import com.project.medizio.entity.AiChat;
import com.project.medizio.entity.Patient;
import com.project.medizio.enums.TextFrom;
import com.project.medizio.repository.AiChatRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiChatService {
    private final AiChatRepository aiChatRepository;
    private final PatientService patientService;

    public List<AiChat> getAllChatByUser(Long id) {
        patientService.getUserById(id); // check user exists
        return aiChatRepository.findByPatientId(id);
    }

    private void addNewChat(AiChat aiChat) {
        aiChatRepository.save(aiChat);
    }

    @Transactional
    public void deleteChat(Long id) {
        aiChatRepository.deleteById(id);
    }

    @Transactional
    public void deleteAllChatsByUserId(Long id) {
        aiChatRepository.deleteByPatientId(id);
    }


    public AiChat generateAiResponse(AiChat aiChat, MultipartFile patientReport) throws IOException {

        Patient patient = patientService.getUserById(aiChat.getPatient().getId());

        List<AiChat> existingAiChat =
                aiChatRepository.findByPatientId(patient.getId());

        List<ChatLog> chatLogs = new ArrayList<>();

        for (AiChat chat : existingAiChat) {
            String role = chat.getTextFrom() == TextFrom.USER
                    ? "user"
                    : "assistant";

            chatLogs.add(new ChatLog(
                    role,
                    chat.getText(),
                    chat.getCreatedAt()
            ));
        }

        String url = "http://127.0.0.1:8000/ai/chat";

        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> user = new HashMap<>();
        user.put("id", patient.getId());
        user.put("name", patient.getName());
        user.put("dob", patient.getDob());

        body.add("user", mapper.writeValueAsString(user));
        body.add("chat_log", mapper.writeValueAsString(chatLogs));
        body.add("user_input", aiChat.getText());


        // attach file if exists
        if (patientReport != null && !patientReport.isEmpty()) {

            ByteArrayResource fileResource = new ByteArrayResource(patientReport.getBytes()) {
                @Override
                public String getFilename() {
                    return patientReport.getOriginalFilename();
                }
            };

            HttpHeaders fileHeaders = new HttpHeaders();
            fileHeaders.setContentType(MediaType.parseMediaType(patientReport.getContentType()));

            HttpEntity<ByteArrayResource> fileEntity =
                    new HttpEntity<>(fileResource, fileHeaders);

            body.add("patient_report", fileEntity);

        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity =
                new HttpEntity<>(body, headers);

        try {

            addNewChat(aiChat);

            String responseText = restTemplate.postForObject(
                    url,
                    requestEntity,
                    String.class
            );

            AiChat aiResponse = new AiChat();
            aiResponse.setText(responseText);
            aiResponse.setTextFrom(TextFrom.ASSISTANT);
            aiResponse.setPatient(aiChat.getPatient());

            return aiChatRepository.save(aiResponse);

        } catch (RestClientException e) {
            throw new RuntimeException("AI service failed", e);
        }
    }
}