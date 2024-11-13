package com.example.hmsUser.service;

import com.example.hmsUser.dto.requestDto.NurseRequest;
import com.example.hmsUser.dto.responseDto.BaseApiResponse;
import com.example.hmsUser.dto.responseDto.NurseResponse;
import com.example.hmsUser.dto.responseDto.PatientResponse;
import com.example.hmsUser.entity.Doctor;
import com.example.hmsUser.entity.Nurse;
import com.example.hmsUser.entity.Patient;
import com.example.hmsUser.implementation.NurseImpl;
import com.example.hmsUser.repository.DoctorRepository;
import com.example.hmsUser.repository.NurseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.MESSAGE_NAMES.*;
import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.STATUS_CODES.*;
import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.SUCCESS_CODES.*;

@Service
public class NurseService implements NurseImpl {

    @Autowired
    private NurseRepository nurseRepository;
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private JwtService jwtService;

    // Fetch Nurse by ID

    @Override
    public BaseApiResponse getNurseById(Long nurseId, String token) {
        try {
            // Extract email and role from the JWT token
            String email = jwtService.extractEmail(token);
            String role = jwtService.extractRole(token);

            NurseResponse response = new NurseResponse();  // Default response

            if ("Nurse".equalsIgnoreCase(role)) {
                // Only allow access to the nurse's own data
                Optional<Nurse> nurseOpt = nurseRepository.findById(nurseId);
                if (nurseOpt.isPresent()) {
                    // Check if the nurse in the database matches the logged-in nurse (email check)
                    if (nurseOpt.get().getEmail().equalsIgnoreCase(email)) {
                        response = mapToNurseResponse(nurseOpt.get());
                    } else {
                        return new BaseApiResponse(UNAUTHORIZED, FAILURE, "You can only access your own data", Collections.emptyList());
                    }
                } else {
                    return new BaseApiResponse(NOT_FOUND, FAILURE, "Nurse not found", Collections.emptyList());
                }

            } else if ("Doctor".equalsIgnoreCase(role)) {
                // Doctor can access the nurse data if they are associated
                Optional<Doctor> doctorOpt = doctorRepository.findByEmail(email);
                if (doctorOpt.isPresent()) {
                    Optional<Nurse> nurseOpt = nurseRepository.findById(nurseId);
                    if (nurseOpt.isPresent()) {
                        Nurse nurse = nurseOpt.get();
                        // Ensure the nurse is associated with the doctor
                        if (nurse.getDoctorId().equals(doctorOpt.get().getDoctorId())) {
                            response = mapToNurseResponse(nurse);
                        } else {
                            return new BaseApiResponse(UNAUTHORIZED, FAILURE, "Unauthorized access", Collections.emptyList());
                        }
                    } else {
                        return new BaseApiResponse(NOT_FOUND, FAILURE, "Nurse not found", Collections.emptyList());
                    }
                } else {
                    return new BaseApiResponse(UNAUTHORIZED, FAILURE, "Doctor not found", Collections.emptyList());
                }

            } else {
                return new BaseApiResponse(UNAUTHORIZED, FAILURE, "Invalid role", Collections.emptyList());
            }

            // Return the response with the fetched nurse data
            return new BaseApiResponse(SUCCESS_OK, SUCCESS, "Data fetched successfully", response);

        } catch (Exception ex) {
            ex.printStackTrace();  // Log the exception (optional)
            return new BaseApiResponse(INTERNAL_SERVER_ERROR, FAILURE, "An unexpected error occurred", Collections.emptyList());
        }
    }

    // Helper method to map Nurse entity to NurseResponse DTO
    private NurseResponse mapToNurseResponse(Nurse nurse) {
        return new NurseResponse(
                nurse.getNurseId(),
                nurse.getName(),
                nurse.getAge(),
                nurse.getGender(),
                nurse.getAddress(),
                nurse.getShift(),
                nurse.getContact(),
                nurse.getEmail(),
                nurse.getDoctorId()  // Assuming doctorId exists in Nurse entity
        );
    }
    //-------------------------------------------------------------------------------------------------------------------------------------

    // Update Nurse Information

    @Override
    public BaseApiResponse updateNurse(Long nurseId, NurseRequest nurseRequest, String token) {
        try {
            // Extract email from the JWT token
            String emailFromToken = jwtService.extractEmail(token);

            // Fetch the nurse from the repository
            Optional<Nurse> nurseOpt = nurseRepository.findById(nurseId);
            if (nurseOpt.isPresent()) {
                Nurse nurse = nurseOpt.get();

                // Check if the logged-in nurse's email matches the nurse's email in the database
                if (!nurse.getEmail().equals(emailFromToken)) {
                    return new BaseApiResponse(UNAUTHORIZED, FAILURE, "Unauthorized access", Collections.emptyList());
                }

                // Update nurse fields with validation
                if (nurseRequest.getAge() > 0) {
                    nurse.setAge(nurseRequest.getAge());
                }
                if (nurseRequest.getGender() != null && !nurseRequest.getGender().isEmpty()) {
                    nurse.setGender(nurseRequest.getGender());
                }
                if (nurseRequest.getAddress() != null && !nurseRequest.getAddress().isEmpty()) {
                    nurse.setAddress(nurseRequest.getAddress());
                }
                if (nurseRequest.getContact() != null && !nurseRequest.getContact().isEmpty()) {
                    nurse.setContact(nurseRequest.getContact());
                }
                if (nurseRequest.getShift() != null && !nurseRequest.getShift().isEmpty()) {
                    nurse.setShift(nurseRequest.getShift());
                }
                if (nurseRequest.getDoctorId() != null && !(nurseRequest.getDoctorId() == 0)) {
                    nurse.setDoctorId(nurseRequest.getDoctorId());
                }

                // Save the updated nurse
                nurseRepository.save(nurse);
                return new BaseApiResponse(SUCCESS_OK, SUCCESS, COMMON_MESSAGE_UPDATION, Collections.emptyList());
            } else {
                return new BaseApiResponse(NOT_FOUND, FAILURE, NOT_PRESENT, Collections.emptyList());
            }
        } catch (Exception ex) {
            return new BaseApiResponse(INTERNAL_SERVER_ERROR, FAILURE, COMMON_ERROR, Collections.emptyList());
        }
    }


}
