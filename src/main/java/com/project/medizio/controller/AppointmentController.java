package com.project.medizio.controller;
import com.project.medizio.components.FileUpload;
import com.project.medizio.dto.Availability;
import com.project.medizio.entity.Appointment;
import com.project.medizio.enums.AppointmentStatus;
import com.project.medizio.service.AppointmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/appointment")
@AllArgsConstructor
public class AppointmentController {

    private final AppointmentService service;
    private final FileUpload fileUpload;

    @PostMapping
    public ResponseEntity<Appointment> bookAppointment(@RequestBody Appointment appointment) {
        return ResponseEntity.ok(service.book(appointment));
    }

    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Appointment>> getAppointmentByUser(@PathVariable Long id) {
        return ResponseEntity.ok(service.getAppointmentByUser(id));
    }

    @PostMapping("/availability")
    public ResponseEntity<Boolean> isAvailable(@RequestBody Availability availability) {
        return ResponseEntity.ok(service.getAvailability(availability));
    }

    @PatchMapping("/update/rating")
    public ResponseEntity<Appointment> updateRatingOfAppointment(@RequestBody Appointment appointment) {
        return ResponseEntity.ok(service.addRating(appointment));
    }

    @GetMapping("/doctor/{id}")
    public ResponseEntity<List<Appointment>> getByDoctor(@PathVariable Long id) {
        return ResponseEntity.ok(service.getByDoctor(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Appointment> updateStatus(@PathVariable Long id, @RequestParam AppointmentStatus status) {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }

    @PatchMapping("/update")
    public ResponseEntity<Appointment> updateAppointment(@RequestBody Appointment appointment) {
        return ResponseEntity.ok(service.updateAppointment(appointment));
    }

    @PostMapping(path = "/file/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<String>> addFiles(@RequestPart("file") List<MultipartFile> files) throws IOException {
        List<String> fileUrls = fileUpload.uploadFiles(files);
        return ResponseEntity.ok(fileUrls);
    }

    @DeleteMapping
    public void deleteAppointment(@RequestParam("id") Long id) {
        service.deleteAppointment(id);
    }
}
