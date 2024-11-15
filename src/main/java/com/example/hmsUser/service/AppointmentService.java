package com.example.hmsUser.service;

import com.example.hmsUser.dto.requestDto.AppointmentRequest;
import com.example.hmsUser.dto.responseDto.AppointmentResponse;
import com.example.hmsUser.dto.responseDto.BaseApiResponse;
import com.example.hmsUser.entity.Appointment;
import com.example.hmsUser.implementation.AppointmentImpl;
import com.example.hmsUser.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.MESSAGE_NAMES.*;
import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.STATUS_CODES.*;
import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.SUCCESS_CODES.FAILURE;
import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.SUCCESS_CODES.SUCCESS;

@Service
public class AppointmentService implements AppointmentImpl {

    @Autowired
    private AppointmentRepository appointmentRepository;

    // Method to create or update an appointment
    @Override
    @Transactional
    public BaseApiResponse createOrUpdateAppointment(AppointmentRequest appointmentRequest) {
        try {
            // Check if the appointmentRequest is valid
            if (appointmentRequest == null) {
                return new BaseApiResponse(BAD_REQUEST, FAILURE, "Invalid appointment data.", Collections.emptyList());
            }

            // Check if the appointment already exists (by ID for update scenario)
            Optional<Appointment> existingAppointment = appointmentRepository.findById(appointmentRequest.getAppointmentId());
            Appointment appointment;

            if (existingAppointment.isPresent()) {
                // Update the existing appointment
                appointment = existingAppointment.get();
                appointment.setDateOfAppointment(appointmentRequest.getDateOfAppointment());
                appointment.setDoctorId(appointmentRequest.getDoctorId());
                appointment.setPatientId(appointmentRequest.getPatientId());
                appointment.setDoctorEmail(appointmentRequest.getDoctorEmail());
                appointment.setPatientEmail(appointmentRequest.getPatientEmail());
            } else {
                // Create a new appointment
                appointment = new Appointment();
                appointment.setDateOfAppointment(appointmentRequest.getDateOfAppointment());
                appointment.setDoctorId(appointmentRequest.getDoctorId());
                appointment.setPatientId(appointmentRequest.getPatientId());
                appointment.setDoctorEmail(appointmentRequest.getDoctorEmail());
                appointment.setPatientEmail(appointmentRequest.getPatientEmail());
            }

            // Save the appointment to the database
            Appointment savedAppointment = appointmentRepository.save(appointment);

            // Prepare the response
            AppointmentResponse appointmentResponse = new AppointmentResponse(
                    savedAppointment.getAppointmentId(),
                    savedAppointment.getDateOfAppointment(),
                    savedAppointment.getDoctorId(),
                    savedAppointment.getPatientId(),
                    savedAppointment.getDoctorEmail(),
                    savedAppointment.getPatientEmail()
            );

            return new BaseApiResponse(SUCCESS_OK, SUCCESS, "Appointment created/updated successfully.", appointmentResponse);

        } catch (Exception e) {
            return new BaseApiResponse(INTERNAL_SERVER_ERROR, FAILURE, "Error occurred while creating/updating the appointment.", Collections.emptyList());
        }
    }
//-------------------------------------------------------------------------------------------------------------------------------

    // Method to fetch an appointment by ID
    @Override
    public BaseApiResponse fetchAppointment(Long appointmentId, String loggedInUserEmail, String role) {
        try {
            Optional<Appointment> appointmentOpt = appointmentRepository.findById(appointmentId);

            if (appointmentOpt.isPresent()) {
                Appointment appointment = appointmentOpt.get();

                // If the user is a Receptionist, allow access to any billing record
                if ("Receptionist".equalsIgnoreCase(role)) {
                    AppointmentResponse appointmentResponse = new AppointmentResponse(
                        appointment.getAppointmentId(),
                        appointment.getDateOfAppointment(),
                        appointment.getDoctorId(),
                        appointment.getPatientId(),
                        appointment.getDoctorEmail(),
                        appointment.getPatientEmail()
                    );
                    return new BaseApiResponse(SUCCESS_OK, SUCCESS, COMMON_MESSAGE_DATA_FETCHED, appointmentResponse);
                }

                // If the user is a Patient or Doctor, they can only access their own billing record
                if (appointment.getPatientEmail().equals(loggedInUserEmail) || appointment.getDoctorEmail().equals(loggedInUserEmail)) {
                    AppointmentResponse appointmentResponse = new AppointmentResponse(
                            appointment.getAppointmentId(),
                            appointment.getDateOfAppointment(),
                            appointment.getDoctorId(),
                            appointment.getPatientId(),
                            appointment.getDoctorEmail(),
                            appointment.getPatientEmail()
                    );
                    return new BaseApiResponse(SUCCESS_OK, SUCCESS, COMMON_MESSAGE_DATA_FETCHED, appointmentResponse);
                }else {
                    return new BaseApiResponse(UNAUTHORIZED, FAILURE, "You can only fetch your own billing data.", Collections.emptyList());
                }

            } else {
                return new BaseApiResponse(NOT_FOUND, FAILURE, NOT_PRESENT, Collections.emptyList());
            }
        } catch (Exception e) {
            return new BaseApiResponse(INTERNAL_SERVER_ERROR, FAILURE, COMMON_ERROR , Collections.emptyList());
        }
    }

}
