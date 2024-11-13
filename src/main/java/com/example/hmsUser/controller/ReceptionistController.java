package com.example.hmsUser.controller;

import com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT;
import com.example.hmsUser.dto.requestDto.NurseRequest;
import com.example.hmsUser.dto.requestDto.ReceptionistRequest;
import com.example.hmsUser.dto.responseDto.BaseApiResponse;
import com.example.hmsUser.implementation.NurseImpl;
import com.example.hmsUser.implementation.ReceptionistImpl;
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
@CrossOrigin("*")

public class ReceptionistController {

    @Autowired
    private ReceptionistImpl receptionistImpl; // Use the interface

    // Create or Update Receptionist Information
    @PreAuthorize("hasRole('Receptionist')")
    @PostMapping(REST_MAPPING_CONSTRAINT.DEFINE_API.UPDATE_RECEPTIONIST)
    public ResponseEntity<BaseApiResponse> updateReceptionist(@Valid @RequestBody ReceptionistRequest receptionistRequest, HttpServletRequest request) {
        try {

            String token = request.getHeader("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new BaseApiResponse(BAD_REQUEST, FAILURE, "Token missing or malformed", Collections.emptyList()));
            }
            token = token.substring(7); // Extract token part after "Bearer "

            // Validation for fields
            if (receptionistRequest.getReceptionistId() == null) {
                return ResponseEntity.badRequest()
                        .body(new BaseApiResponse(BAD_REQUEST, FAILURE, FIELD_REQUIRED_MESSAGE + " id", Collections.emptyList()));
            }
            if (receptionistRequest.getAge() < 0) {
                return ResponseEntity.badRequest()
                        .body(new BaseApiResponse(BAD_REQUEST, FAILURE, COMMON_MESSAGE_FORMAT + " age", Collections.emptyList()));
            }
            if (receptionistRequest.getGender() != null && !receptionistRequest.getGender().matches("(?i)Male|Female|Other")) {
                return ResponseEntity.badRequest()
                        .body(new BaseApiResponse(BAD_REQUEST, FAILURE, FIELD_REQUIRED_MESSAGE + " gender", Collections.emptyList()));
            }
            if (receptionistRequest.getAddress() == null || receptionistRequest.getAddress().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new BaseApiResponse(BAD_REQUEST, FAILURE, FIELD_REQUIRED_MESSAGE +" address", Collections.emptyList()));
            }
            if (receptionistRequest.getContact() == null || !receptionistRequest.getContact().matches("^[0-9]{10,12}$")) {
                return ResponseEntity.badRequest()
                        .body(new BaseApiResponse(BAD_REQUEST, FAILURE, FIELD_REQUIRED_MESSAGE + " contact", Collections.emptyList()));
            }
            if (receptionistRequest.getShift() == null || receptionistRequest.getShift().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new BaseApiResponse(BAD_REQUEST, FAILURE, FIELD_REQUIRED_MESSAGE + " shift", Collections.emptyList()));
            }

            // If validation passes, proceed with the update
            BaseApiResponse response = receptionistImpl.updateReceptionist(receptionistRequest.getReceptionistId(), receptionistRequest,token);
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
    //-----------------------------------------------------------------------------------------------------------------------------

    // Fetch Receptionist by ID
    @PreAuthorize("hasRole('Receptionist')")
    @PostMapping(REST_MAPPING_CONSTRAINT.DEFINE_API.FETCH_RECEPTIONIST)
    public ResponseEntity<BaseApiResponse> getReceptionist(@Valid @RequestBody ReceptionistRequest receptionistRequest) {
        try {
            BaseApiResponse response = receptionistImpl.getReceptionistById(receptionistRequest.getReceptionistId());
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
