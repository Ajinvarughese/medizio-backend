package com.project.medizio.service;

import com.project.medizio.dto.Availability;
import com.project.medizio.entity.Appointment;
import com.project.medizio.enums.AppointmentStatus;
import com.project.medizio.exception.DoctorUnavailableException;
import com.project.medizio.repository.AppointmentRepository;
import com.project.medizio.repository.DoctorRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
@AllArgsConstructor
public class AppointmentService {
    private final AppointmentRepository repository;
    private final DoctorRepository doctorRepository;

    public Appointment book(Appointment appointment) {
        Availability availability = new Availability();
        availability.setDate(appointment.getDate());
        availability.setTime(appointment.getTime());
        availability.setDoctorId(appointment.getDoctor().getId());

        if(getAvailability(availability)) {
             appointment.setStatus(AppointmentStatus.BOOKED);
            return repository.save(appointment);
        }
        throw new DoctorUnavailableException("Appointment not available");
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

    public Appointment updateAppointment(Appointment appointment) {
        Appointment existing = repository.findById(appointment.getId())
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found with id: "+appointment.getId()));

        if(appointment.getNote() != null) {
            existing.setNote(appointment.getNote());
        }
        if (appointment.getDocument() != null) {
            existing.setDocument(appointment.getDocument());
        }

        return repository.save(existing);
    }

    public List<Appointment> getAppointmentByUser(Long id) {
        return repository.findByPatientId(id);
    }

    public Boolean getAvailability(Availability availability) {

        doctorRepository.findById(availability.getDoctorId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Doctor not found with id: " + availability.getDoctorId()));

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        LocalDate requestedDate = LocalDate.parse(
                availability.getDate(),
                dateFormatter
        );

        LocalTime requestedTime = LocalTime.parse(
                availability.getTime().trim(),
                DateTimeFormatter.ofPattern("h:mm a").withLocale(Locale.ENGLISH)
        );

        List<Appointment> appointments =
                repository.findByDoctorId(availability.getDoctorId());

        if (appointments.size() == 20) {
            return false;
        }

        for (Appointment appt : appointments) {

            LocalDate apptDate = LocalDate.parse(
                    appt.getDate(),
                    dateFormatter
            );

            LocalTime apptTime = LocalTime.parse(
                    appt.getTime(),
                    DateTimeFormatter.ofPattern("h:mm a").withLocale(Locale.ENGLISH)
            );

            if (apptDate.equals(requestedDate)) {

                long minutesDiff = Math.abs(
                        Duration.between(apptTime, requestedTime).toMinutes()
                );

                if (minutesDiff < 10) {
                    return false;
                }
            }
        }

        return true;
    }

    @Transactional
    public void deleteAppointment(Long id) {
        repository.deleteById(id);
    }
}




