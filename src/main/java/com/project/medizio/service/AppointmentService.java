package com.project.medizio.service;

import com.project.medizio.dto.Availability;
import com.project.medizio.entity.Appointment;
import com.project.medizio.entity.Doctor;
import com.project.medizio.enums.AppointmentStatus;
import com.project.medizio.enums.DaysOfWeek;
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
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;

    public Appointment book(Appointment appointment) {
        Availability availability = new Availability();
        availability.setDate(appointment.getDate());
        availability.setTime(appointment.getTime());
        availability.setDoctorId(appointment.getDoctor().getId());

        if(getAvailability(availability)) {
             appointment.setStatus(AppointmentStatus.BOOKED);
            return appointmentRepository.save(appointment);
        }
        throw new DoctorUnavailableException("Appointment not available");
    }

    public List<Appointment> getAll() {
        return appointmentRepository.findAll();
    }

    public List<Appointment> getByDoctor(Long id) {
        return appointmentRepository.findByDoctorId(id);
    }

    public Appointment updateStatus(Long id, AppointmentStatus newStatus) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow();
        appointment.setStatus(newStatus);
        return appointmentRepository.save(appointment);
    }

    public Appointment updateAppointment(Appointment appointment) {
        Appointment existing = appointmentRepository.findById(appointment.getId())
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found with id: "+appointment.getId()));

        if(appointment.getNote() != null) {
            existing.setNote(appointment.getNote());
        }
        if (appointment.getDocument() != null) {
            existing.setDocument(appointment.getDocument());
        }

        return appointmentRepository.save(existing);
    }

    public List<Appointment> getAppointmentByUser(Long id) {
        return appointmentRepository.findByPatientId(id);
    }

    public Boolean getAvailability(Availability availability) {

        Doctor doctor = doctorRepository.findById(availability.getDoctorId())
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


        DaysOfWeek requestedDay = DaysOfWeek.valueOf(
            requestedDate.getDayOfWeek().name()
        );

        if (doctor.getUnavailableDays() != null &&
                doctor.getUnavailableDays().contains(requestedDay)) {
            return false;
        }

        List<Appointment> appointments =
                appointmentRepository.findByDoctorId(availability.getDoctorId());

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

    public Appointment addRating(Appointment appointment) {

        Appointment existing = appointmentRepository.findById(appointment.getId())
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Appointment not found with id: " + appointment.getId()
                        ));

        existing.setRating(appointment.getRating());

        appointmentRepository.save(existing);

        // Recalculate doctor rating
        Doctor doctor = existing.getDoctor();

        Double updatedRating = calculateDoctorRating(doctor.getId());
        doctor.setRating(updatedRating);

        doctorRepository.save(doctor);

        return existing;
    }



    private Double calculateDoctorRating(Long doctorId) {

        List<Appointment> appointments =
                appointmentRepository.findByDoctorId(doctorId);

        int sum = 0;
        int count = 0;

        for (Appointment a : appointments) {
            if (a.getRating() != null && a.getRating() > 0) {
                sum += a.getRating();
                count++;
            }
        }

        if (count == 0) return 0.0;

        return (double) sum / count;
    }


    @Transactional
    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }
}




