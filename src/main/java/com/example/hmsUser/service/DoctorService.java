package com.example.hmsUser.service;

import com.example.hmsUser.dto.requestDto.DoctorRequest;
import com.example.hmsUser.dto.responseDto.BaseApiResponse;
import com.example.hmsUser.dto.responseDto.DoctorResponse;
import com.example.hmsUser.entity.Doctor;
import com.example.hmsUser.implementation.DoctorImpl;
import com.example.hmsUser.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.MESSAGE_NAMES.*;
import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.STATUS_CODES.*;
import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.SUCCESS_CODES.FAILURE;
import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.SUCCESS_CODES.SUCCESS;

@Service
public class DoctorService implements DoctorImpl {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private JwtService jwtService;


    // Update Doctor Information
    @Override
    public BaseApiResponse updateDoctor(Long doctorId, DoctorRequest doctorRequest, String token) {
        try {
            // Extract email from the JWT token to identify the logged-in doctor
            String loggedInEmail = jwtService.extractEmail(token);

            // Find the doctor by ID
            Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
            if (doctorOpt.isPresent()) {
                Doctor doctor = doctorOpt.get();

                // Check if the logged-in nurse's email matches the nurse's email in the database
                if (!doctor.getEmail().equals(loggedInEmail)) {
                    return new BaseApiResponse(UNAUTHORIZED, FAILURE, "Unauthorized access", Collections.emptyList());
                }

                // Check if the logged-in doctor is trying to update their own data
                if (doctor.getEmail().equalsIgnoreCase(loggedInEmail)) {
                    // Proceed with updating the doctor information
                    if (doctorRequest.getSpecialization() != null && !doctorRequest.getSpecialization().isEmpty()) {
                        doctor.setSpecialization(doctorRequest.getSpecialization());
                    }
                    if (doctorRequest.getQualification() != null && !doctorRequest.getQualification().isEmpty()) {
                        doctor.setQualification(doctorRequest.getQualification());
                    }
                    if (doctorRequest.getExperience() != 0) {
                        doctor.setExperience(doctorRequest.getExperience());
                    }
                    if (doctorRequest.getAddress() != null && !doctorRequest.getAddress().isEmpty()) {
                        doctor.setAddress(doctorRequest.getAddress());
                    }
                    if (doctorRequest.getContact() != null && !doctorRequest.getContact().isEmpty()) {
                        doctor.setContact(doctorRequest.getContact());
                    }
                    if (doctorRequest.getShift() != null && !doctorRequest.getShift().isEmpty()) {
                        doctor.setShift(doctorRequest.getShift());
                    }

                    // Save the updated doctor
                    doctorRepository.save(doctor);
                    return new BaseApiResponse(SUCCESS_OK, SUCCESS, "Doctor data updated successfully", Collections.emptyList());
                } else {
                    return new BaseApiResponse(UNAUTHORIZED, FAILURE, "You can only update your own data", Collections.emptyList());
                }
            } else {
                return new BaseApiResponse(NOT_FOUND, FAILURE, "Doctor not found", Collections.emptyList());
            }
        } catch (Exception ex) {
            return new BaseApiResponse(INTERNAL_SERVER_ERROR, FAILURE, COMMON_ERROR, Collections.emptyList());
        }
    }
    //-------------------------------------------------------------------------------------------------------------------------------

    // Fetch Doctor by Id
    @Override
    public BaseApiResponse getDoctorById(Long doctorId, String token) {
        try {
            // Extract email from the JWT token
            String email = jwtService.extractEmail(token);

            Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
            if (doctorOpt.isPresent()) {
                // Ensure that the logged-in doctor is trying to access their own data
                if (doctorOpt.get().getEmail().equalsIgnoreCase(email)) {
                    DoctorResponse response = new DoctorResponse(
                            doctorOpt.get().getDoctorId(),
                            doctorOpt.get().getName(),
                            doctorOpt.get().getExperience(),
                            doctorOpt.get().getQualification(),
                            doctorOpt.get().getSpecialization(),
                            doctorOpt.get().getAddress(),
                            doctorOpt.get().getShift(),
                            doctorOpt.get().getContact(),
                            doctorOpt.get().getEmail()
                    );
                    return new BaseApiResponse(SUCCESS_OK, SUCCESS, "Doctor data fetched successfully", response);
                } else {
                    return new BaseApiResponse(UNAUTHORIZED, FAILURE, "You can only access your own data", Collections.emptyList());
                }
            } else {
                return new BaseApiResponse(NOT_FOUND, FAILURE, "Doctor not found", Collections.emptyList());
            }
        } catch (Exception ex) {
            return new BaseApiResponse(INTERNAL_SERVER_ERROR, FAILURE, COMMON_ERROR, Collections.emptyList());
        }
    }

}
