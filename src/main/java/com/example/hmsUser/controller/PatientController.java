package com.example.hmsUser.controller;

import com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT;
import com.example.hmsUser.dto.requestDto.PatientRequest;
import com.example.hmsUser.dto.requestDto.SearchRequest;
import com.example.hmsUser.dto.responseDto.BaseApiResponse;
import com.example.hmsUser.entity.Patient;
import com.example.hmsUser.implementation.PatientImpl;
import com.example.hmsUser.repository.PatientRepository;
import com.example.hmsUser.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.BASE_URL;
import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.MESSAGE_NAMES.*;
import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.STATUS_CODES.*;
import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.SUCCESS_CODES.FAILURE;
import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.SUCCESS_CODES.SUCCESS;

@RestController
@RequestMapping(BASE_URL)

public class PatientController {

    @Autowired
    public PatientImpl patientImpl;
    @Autowired
    public JwtService jwtService;
    @Autowired
    public PatientRepository patientRepository;

    // Create or Update Patient Information
    @PreAuthorize("hasRole('Patient')")
    @PostMapping(REST_MAPPING_CONSTRAINT.DEFINE_API.UPDATE_PATIENT)
    public ResponseEntity<BaseApiResponse> updatePatient(@Valid @RequestBody PatientRequest patientRequest, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization").substring(7);  // Extract token from Authorization header
            String emailFromToken = jwtService.extractEmail(token);  // Extract email from JWT token

            // Assuming you are fetching the patient by their ID from the repository:
            Optional<Patient> patientOpt = patientRepository.findById(patientRequest.getPatientId());
            // Check if the patient exists
            if (patientOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new BaseApiResponse(NOT_FOUND, FAILURE, "Patient not found", Collections.emptyList()));
            }
            // Retrieve the patient's email from the database
            String patientEmail = patientOpt.get().getEmail();
            // Check if the logged-in user's email matches the patient's email
            if (!patientEmail.equals(emailFromToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new BaseApiResponse(UNAUTHORIZED, FAILURE, "Unauthorized access", Collections.emptyList()));
            }

            //Validation for fields
            if (patientRequest.getPatientId() == null) {
                return ResponseEntity.badRequest()
                        .body(new BaseApiResponse(BAD_REQUEST, FAILURE, FIELD_REQUIRED_MESSAGE, Collections.emptyList()));
            }
            if (patientRequest.getAge() < 0) {
                return ResponseEntity.badRequest()
                        .body(new BaseApiResponse(BAD_REQUEST, FAILURE, FIELD_REQUIRED_MESSAGE, Collections.emptyList()));
            }
            if (patientRequest.getGender() != null && !patientRequest.getGender().matches("Male|Female|Other")) {
                return ResponseEntity.badRequest()
                        .body(new BaseApiResponse(BAD_REQUEST, FAILURE, FIELD_REQUIRED_MESSAGE, Collections.emptyList()));
            }
            if (patientRequest.getAddress() == null || patientRequest.getAddress().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new BaseApiResponse(BAD_REQUEST, FAILURE, FIELD_REQUIRED_MESSAGE, Collections.emptyList()));
            }
            if ((patientRequest.getMedicalHistory() == null) || patientRequest.getMedicalHistory().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new BaseApiResponse(BAD_REQUEST, FAILURE, FIELD_REQUIRED_MESSAGE, Collections.emptyList()));
            }
            if (patientRequest.getContact() == null || !patientRequest.getContact().matches("^[0-9]{10,12}$")) {
                return ResponseEntity.badRequest()
                        .body(new BaseApiResponse(BAD_REQUEST, FAILURE, FIELD_REQUIRED_MESSAGE, Collections.emptyList()));
            }
            if (patientRequest.getDoctorId() == null || patientRequest.getDoctorId() == 0) {
                return ResponseEntity.badRequest()
                        .body(new BaseApiResponse(BAD_REQUEST, FAILURE, FIELD_REQUIRED_MESSAGE, Collections.emptyList()));
            }
            if (patientRequest.getNurseId() == null || patientRequest.getNurseId() == 0) {
                return ResponseEntity.badRequest()
                        .body(new BaseApiResponse(BAD_REQUEST, FAILURE, FIELD_REQUIRED_MESSAGE, Collections.emptyList()));
            }

            // Proceed with validation and update patient logic
            BaseApiResponse response = patientImpl.updatePatient(patientRequest.getPatientId(), patientRequest);
            if (response.getSuccess() == 1) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new BaseApiResponse(SUCCESS_OK, SUCCESS, COMMON_MESSAGE_DATA_FETCHED, response));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new BaseApiResponse(NOT_FOUND, FAILURE, NOT_PRESENT, Collections.emptyList()));
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseApiResponse(INTERNAL_SERVER_ERROR, FAILURE, COMMON_ERROR, Collections.emptyList()));
        }
    }
    //--------------------------------------------------------------------------------------------------------------------------

    // Get Patient Information
    @PostMapping(REST_MAPPING_CONSTRAINT.DEFINE_API.FETCH_PATIENT)
    @PreAuthorize("hasAnyRole('Patient','Nurse')")
    public ResponseEntity<BaseApiResponse> getPatient(@Valid @RequestBody SearchRequest searchRequest, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new BaseApiResponse(BAD_REQUEST, FAILURE, "Token missing or malformed", Collections.emptyList()));
            }

            token = token.substring(7); // Extract token part after "Bearer "
            BaseApiResponse response = patientImpl.getPatientById(searchRequest.getId(), token);

            if (response.getSuccess() == 1) {
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseApiResponse(INTERNAL_SERVER_ERROR, FAILURE, "Internal server error", Collections.emptyList()));
        }
    }

}
