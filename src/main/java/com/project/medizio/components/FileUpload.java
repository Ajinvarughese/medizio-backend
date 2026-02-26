package com.project.medizio.components;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileUpload {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @Value("${server.url:http://localhost:8080}")
    private String serverUrl;

    private String uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("No file selected.");
        }

        String projectDir = System.getProperty("user.dir");
        String uploadDir = projectDir + File.separator + "/src/main/resources/uploads" + File.separator;

        File dir = new File(uploadDir);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Failed to create directories.");
        }

        String originalFileName = file.getOriginalFilename();
        String safeFileName = Objects.requireNonNull(originalFileName).replaceAll("[^a-zA-Z0-9._-]", "_");
        String uniqueFileName = UUID.randomUUID() + "_" + safeFileName;

        Path filePath = Paths.get(uploadDir + File.separator + uniqueFileName);
        file.transferTo(filePath.toFile());

        return serverUrl + "/api/media/" + uniqueFileName;
    }

    public List<String> uploadFiles(List<MultipartFile> files) throws IOException {
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            urls.add(uploadFile(file)); // reuse single file upload
        }
        return urls;
    }
}

