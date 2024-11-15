package com.example.hmsUser.controller;

import com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT;
import com.example.hmsUser.dto.requestDto.DoctorRequest;
import com.example.hmsUser.dto.requestDto.SearchRequest;
import com.example.hmsUser.dto.responseDto.BaseApiResponse;
import com.example.hmsUser.implementation.DoctorImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.BASE_URL;
import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.MESSAGE_NAMES.*;
import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.MESSAGE_NAMES.FIELD_REQUIRED_MESSAGE;
import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.STATUS_CODES.BAD_REQUEST;
import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.STATUS_CODES.INTERNAL_SERVER_ERROR;
import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.SUCCESS_CODES.FAILURE;

@RestController
@RequestMapping(BASE_URL)
public class DoctorController {

    @Autowired
    private DoctorImpl doctorImpl; // Use the interface

    // Update Doctor Information
    @PreAuthorize("hasRole('Doctor')")
    @PostMapping(REST_MAPPING_CONSTRAINT.DEFINE_API.UPDATE_DOCTOR)
    public ResponseEntity<BaseApiResponse> updateDoctor(@Valid @RequestBody DoctorRequest doctorRequest, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new BaseApiResponse(BAD_REQUEST, FAILURE, "Token missing or malformed", Collections.emptyList()));
            }
            token = token.substring(7); // Extract token part after "Bearer "

            // Validation for fields
            if (doctorRequest.getDoctorId() == null) {
                return ResponseEntity.badRequest()
                        .body(new BaseApiResponse(BAD_REQUEST, FAILURE, FIELD_REQUIRED_MESSAGE + "id", Collections.emptyList()));
            }
            if (doctorRequest.getSpecialization() == null || doctorRequest.getSpecialization().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new BaseApiResponse(BAD_REQUEST, FAILURE, FIELD_REQUIRED_MESSAGE + "specialization", Collections.emptyList()));
            }
            if (doctorRequest.getQualification() == null || doctorRequest.getQualification().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new BaseApiResponse(BAD_REQUEST, FAILURE, FIELD_REQUIRED_MESSAGE + "qualification", Collections.emptyList()));
            }
            if (doctorRequest.getExperience() == 0) {
                return ResponseEntity.badRequest()
                        .body(new BaseApiResponse(BAD_REQUEST, FAILURE, FIELD_REQUIRED_MESSAGE + "experience", Collections.emptyList()));
            }
            if (doctorRequest.getAddress() == null || doctorRequest.getAddress().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new BaseApiResponse(BAD_REQUEST, FAILURE, FIELD_REQUIRED_MESSAGE + "address", Collections.emptyList()));
            }
            if (doctorRequest.getContact() == null || !doctorRequest.getContact().matches("^[0-9]{10,12}$")) {
                return ResponseEntity.badRequest()
                        .body(new BaseApiResponse(BAD_REQUEST, FAILURE, FIELD_REQUIRED_MESSAGE + "contact", Collections.emptyList()));
            }
            if (doctorRequest.getShift() == null || doctorRequest.getShift().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new BaseApiResponse(BAD_REQUEST, FAILURE, FIELD_REQUIRED_MESSAGE + "shift", Collections.emptyList()));
            }

            // Since the service method already handles extracting the email from the token, pass the token directly to the service
            BaseApiResponse response = doctorImpl.updateDoctor(doctorRequest.getDoctorId(), doctorRequest, token);
            if (response.getSuccess() == 1) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);  // Unauthorized if doctor tries to update another doctor's data
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseApiResponse(INTERNAL_SERVER_ERROR, FAILURE, COMMON_ERROR, Collections.emptyList()));
        }
    }
    //--------------------------------------------------------------------------------------------------------------------------

    // Fetch Doctor by ID
    @PreAuthorize("hasRole('Doctor')")
    @PostMapping(REST_MAPPING_CONSTRAINT.DEFINE_API.FETCH_DOCTOR)
    public ResponseEntity<BaseApiResponse> getDoctor(@Valid @RequestBody SearchRequest searchRequest, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new BaseApiResponse(BAD_REQUEST, FAILURE, "Token missing or malformed", Collections.emptyList()));
            }
            token = token.substring(7); // Extract token part after "Bearer "
            // Pass token to service layer to ensure the logged-in doctor is validated
            BaseApiResponse response = doctorImpl.getDoctorById(searchRequest.getId(), token);
            if (response.getSuccess() == 1) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseApiResponse(INTERNAL_SERVER_ERROR, FAILURE, COMMON_ERROR, Collections.emptyList()));
        }
    }

}
