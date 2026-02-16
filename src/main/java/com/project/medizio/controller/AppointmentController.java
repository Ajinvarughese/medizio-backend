package com.project.medizio.controller;
import com.project.medizio.dto.Availability;
import com.project.medizio.entity.Appointment;
import com.project.medizio.enums.AppointmentStatus;
import com.project.medizio.service.AppointmentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointment")
@AllArgsConstructor
public class AppointmentController {

    private final AppointmentService service;

    @PostMapping
    public Appointment bookAppointment(@RequestBody Appointment appointment) {
        return service.book(appointment);
    }

    @GetMapping
    public List<Appointment> getAllAppointments() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Appointment>> getAppointmentByUser(@PathVariable Long id) {
        return ResponseEntity.ok(service.getAppointmentByUser(id));
    }

    @PostMapping("/availability/{id}")
    public ResponseEntity<Boolean> isAvailable(@PathVariable Long id, @RequestBody Availability availability) {
        return ResponseEntity.ok(service.getAvailability(id, availability));
    }

    @GetMapping("/doctor/{id}")
    public List<Appointment> getByDoctor(@PathVariable Long id) {
        return service.getByDoctor(id);
    }

    @PutMapping("/{id}/status")
    public Appointment updateStatus(@PathVariable Long id, @RequestParam AppointmentStatus status) {
        return service.updateStatus(id, status);
    }
}
