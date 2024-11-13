package com.example.hmsUser.service;

import com.example.hmsUser.entity.Nurse;
import com.example.hmsUser.implementation.PatientImpl;
import com.example.hmsUser.repository.NurseRepository;
import com.example.hmsUser.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.hmsUser.dto.requestDto.PatientRequest;
import com.example.hmsUser.dto.responseDto.BaseApiResponse;
import com.example.hmsUser.dto.responseDto.PatientResponse;
import com.example.hmsUser.entity.Patient;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.MESSAGE_NAMES.*;
import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.STATUS_CODES.*;
import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.SUCCESS_CODES.*;

@Service
public class PatientService implements PatientImpl {

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private NurseRepository nurseRepository;

    @Autowired
    private JwtService jwtService;

    @Override
    public BaseApiResponse getPatientById(Long patientId, String token) {
        try {
            // Extract email and role from the JWT token
            String email = jwtService.extractEmail(token);
            String role = jwtService.extractRole(token);

            PatientResponse response = new PatientResponse();  // Default response

            // Check role and fetch patient data accordingly
            if ("Patient".equalsIgnoreCase(role)) {
                // Only allow access to the patient's own data
                Optional<Patient> patientOpt = patientRepository.findById(patientId);
                if (patientOpt.isPresent()) {
                    // Check if the patient in the database matches the logged-in patient (email check)
                    if (patientOpt.get().getEmail().equalsIgnoreCase(email)) {
                        response = mapToPatientResponse(patientOpt.get());
                    } else {
                        return new BaseApiResponse(UNAUTHORIZED, FAILURE, "You can only access your own data", Collections.emptyList());
                    }
                } else {
                    return new BaseApiResponse(NOT_FOUND, FAILURE, "Patient not found", Collections.emptyList());
                }
            } else if ("Nurse".equalsIgnoreCase(role)) {
                // Ensure the nurse can access the patient's data
                Optional<Nurse> nurseOpt = nurseRepository.findByEmail(email);
                if (nurseOpt.isPresent()) {
                    Optional<Patient> patientOpt1 = patientRepository.findById(patientId);
                    if (patientOpt1.isPresent()) {
                        Patient patient = patientOpt1.get();
                        // Nurse can access this patient's data if they are assigned to this patient
                        if (patient.getNurseId().equals(nurseOpt.get().getNurseId())) {
                            response = mapToPatientResponse(patient);
                        } else {
                            return new BaseApiResponse(UNAUTHORIZED, FAILURE, "Unauthorized access", Collections.emptyList());
                        }
                    } else {
                        return new BaseApiResponse(NOT_FOUND, FAILURE, "Patient not found", Collections.emptyList());
                    }
                } else {
                    return new BaseApiResponse(UNAUTHORIZED, FAILURE, "Nurse not found", Collections.emptyList());
                }
            } else {
                return new BaseApiResponse(UNAUTHORIZED, FAILURE, "Invalid role", Collections.emptyList());
            }

            // Return the response with the fetched patient data
            return new BaseApiResponse(SUCCESS_OK, SUCCESS, "Data fetched successfully", response);

        } catch (Exception ex) {
            return new BaseApiResponse(INTERNAL_SERVER_ERROR, FAILURE, COMMON_ERROR, Collections.emptyList());
        }
    }

    // Helper method to map Patient entity to PatientResponse DTO
    private PatientResponse mapToPatientResponse(Patient patient) {
        return new PatientResponse(
                patient.getPatientId(),
                patient.getName(),
                patient.getAge(),
                patient.getGender(),
                patient.getAddress(),
                patient.getMedicalHistory(),
                patient.getContact(),
                patient.getEmail(),
                patient.getDoctorId(),
                patient.getNurseId()
        );
    }
//--------------------------------------------------------------------------------------------------------------------------------------

    // Update Patient Information
    @Override
    public BaseApiResponse updatePatient(Long patientId, PatientRequest patientRequest) {
        try {
            Optional<Patient> patientOpt = patientRepository.findById(patientId);
            if (patientOpt.isPresent()) {
                Patient patient = patientOpt.get();

                // Update fields that are not related to user entity with validation
                if (patientRequest.getAge() > 0) {
                    patient.setAge(patientRequest.getAge());
                }
                if (patientRequest.getGender() != null && !patientRequest.getGender().isEmpty()) {
                    patient.setGender(patientRequest.getGender());
                }
                if (patientRequest.getAddress() != null && !patientRequest.getAddress().isEmpty()) {
                    patient.setAddress(patientRequest.getAddress());
                }
                if (patientRequest.getMedicalHistory() != null && !patientRequest.getMedicalHistory().isEmpty()) {
                    patient.setMedicalHistory(patientRequest.getMedicalHistory());
                }
                if (patientRequest.getContact() != null && !patientRequest.getContact().isEmpty()) {
                    patient.setContact(patientRequest.getContact());
                }
                if (patientRequest.getDoctorId() != null && !(patientRequest.getDoctorId() == 0)) {
                    patient.setDoctorId(patientRequest.getDoctorId());
                }
                if (patientRequest.getNurseId() != null && !(patientRequest.getNurseId() == 0)) {
                    patient.setNurseId(patientRequest.getNurseId());
                }

                // Save the updated patient
                patientRepository.save(patient);
                return new BaseApiResponse(SUCCESS_OK, SUCCESS, COMMON_MESSAGE_UPDATION, Collections.emptyList());
            } else {
                return new BaseApiResponse(NOT_FOUND, FAILURE, NOT_PRESENT, Collections.emptyList());
            }
        } catch (Exception ex) {
            return new BaseApiResponse(INTERNAL_SERVER_ERROR, FAILURE, COMMON_ERROR, Collections.emptyList());
        }
    }

}
