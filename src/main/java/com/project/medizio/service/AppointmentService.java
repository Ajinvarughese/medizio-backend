package com.project.medizio.service;

import com.project.medizio.dto.Availability;
import com.project.medizio.entity.Appointment;
import com.project.medizio.entity.Doctor;
import com.project.medizio.enums.AppointmentStatus;
import com.project.medizio.repository.AppointmentRepository;
import com.project.medizio.repository.DoctorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
public class AppointmentService {
    private final AppointmentRepository repository;
    private final DoctorRepository doctorRepository;

    public Appointment book(Appointment appointment) {
        appointment.setStatus(AppointmentStatus.BOOKED);
        return repository.save(appointment);
    }

    public List<Appointment> getAll() {
        return repository.findAll();
    }

    public List<Appointment> getByDoctor(Long id) {
        return repository.findByDoctorId(id);
    }

    public Appointment updateStatus(Long id, AppointmentStatus newStatus) {
        Appointment appointment = repository.findById(id).orElseThrow();
        appointment.setStatus(newStatus);
        return repository.save(appointment);
    }

    public List<Appointment> getAppointmentByUser(Long id) {
        return repository.findByPatientId(id);
    }

    public Boolean getAvailability(Long id, Availability availability) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with id: "+id));

        // Parse date and time from string
        LocalDate requestedDate = LocalDate.parse(availability.getDate(), DateTimeFormatter.ISO_DATE); // e.g., "2025-06-13"
        LocalTime requestedTime = LocalTime.parse(availability.getTime(), DateTimeFormatter.ISO_TIME); // e.g., "14:15"

        List<Appointment> appointments = doctor.getAppointments();

        if(appointments.size() == 20) {
            return false;
        }

        for (Appointment appt : appointments) {
            // Parse appointment date and time
            LocalDate apptDate = LocalDate.parse(appt.getDate(), DateTimeFormatter.ISO_DATE);
            LocalTime apptTime = LocalTime.parse(appt.getTime(), DateTimeFormatter.ISO_TIME);

            if (apptDate.equals(requestedDate)) {
                long minutesDiff = Math.abs(Duration.between(apptTime, requestedTime).toMinutes());
                if (minutesDiff < 15) {
                    return false; // Conflict
                }
            }
        }

        return true; // No conflict
    }
}




