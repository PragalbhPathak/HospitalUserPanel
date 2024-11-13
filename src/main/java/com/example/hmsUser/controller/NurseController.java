package com.example.hmsUser.controller;

import com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT;
import com.example.hmsUser.dto.requestDto.NurseRequest;
import com.example.hmsUser.dto.requestDto.PatientRequest;
import com.example.hmsUser.dto.responseDto.BaseApiResponse;
import com.example.hmsUser.implementation.NurseImpl;
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
import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.STATUS_CODES.*;
import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.SUCCESS_CODES.FAILURE;

@RestController
@RequestMapping(BASE_URL)
public class NurseController {

    @Autowired
    private NurseImpl nurseImpl; // Use the interface

    // Fetch Nurse Information
    @PreAuthorize("hasAnyRole('Nurse','Doctor')")
    @PostMapping(REST_MAPPING_CONSTRAINT.DEFINE_API.FETCH_NURSE)
    public ResponseEntity<BaseApiResponse> getNurse(@Valid @RequestBody NurseRequest nurseRequest, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new BaseApiResponse(BAD_REQUEST, FAILURE, "Token missing or malformed", Collections.emptyList()));
            }
            token = token.substring(7); // Extract token part after "Bearer "

            BaseApiResponse response = nurseImpl.getNurseById(nurseRequest.getNurseId(), token);
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


    //--------------------------------------------------------------------------------------------------------------------------------------

//    // Update Nurse Information
//    @PreAuthorize("hasRole('Nurse')")
//    @PostMapping(REST_MAPPING_CONSTRAINT.DEFINE_API.UPDATE_NURSE)
//    public ResponseEntity<BaseApiResponse> updateNurse(@Valid @RequestBody NurseRequest nurseRequest) {
//        try {
//            // Validation for fields
//            if (nurseRequest.getNurseId() == null) {
//                return ResponseEntity.badRequest()
//                        .body(new BaseApiResponse(BAD_REQUEST, FAILURE, FIELD_REQUIRED_MESSAGE + "id", Collections.emptyList()));
//            }
//            if (nurseRequest.getAge() < 0) {
//                return ResponseEntity.badRequest()
//                        .body(new BaseApiResponse(BAD_REQUEST, FAILURE, COMMON_MESSAGE_FORMAT + "age", Collections.emptyList()));
//            }
//            if (nurseRequest.getGender() != null && !nurseRequest.getGender().matches("(?i)Male|Female|Other")) {
//                return ResponseEntity.badRequest()
//                        .body(new BaseApiResponse(BAD_REQUEST, FAILURE, FIELD_REQUIRED_MESSAGE + "gender", Collections.emptyList()));
//            }
//            if (nurseRequest.getAddress() == null || nurseRequest.getAddress().isEmpty()) {
//                return ResponseEntity.badRequest()
//                        .body(new BaseApiResponse(BAD_REQUEST, FAILURE, FIELD_REQUIRED_MESSAGE +"address", Collections.emptyList()));
//            }
//            if (nurseRequest.getContact() == null || !nurseRequest.getContact().matches("^[0-9]{10,12}$")) {
//                return ResponseEntity.badRequest()
//                        .body(new BaseApiResponse(BAD_REQUEST, FAILURE, FIELD_REQUIRED_MESSAGE + "contact", Collections.emptyList()));
//            }
//            if (nurseRequest.getShift() == null || nurseRequest.getShift().isEmpty()) {
//                return ResponseEntity.badRequest()
//                        .body(new BaseApiResponse(BAD_REQUEST, FAILURE, FIELD_REQUIRED_MESSAGE + "shift", Collections.emptyList()));
//            }
//
//            // If validation passes, proceed with the update
//            BaseApiResponse response = nurseImpl.updateNurse(nurseRequest.getNurseId(), nurseRequest);
//            if (response.getSuccess() == 1) {
//                return ResponseEntity.ok(response);
//            } else {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
//            }
//        } catch (Exception ex) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new BaseApiResponse(INTERNAL_SERVER_ERROR, FAILURE, COMMON_ERROR, Collections.emptyList()));
//        }
//    }

    // Update Nurse Information
    @PreAuthorize("hasRole('Nurse')")
    @PostMapping(REST_MAPPING_CONSTRAINT.DEFINE_API.UPDATE_NURSE)
    public ResponseEntity<BaseApiResponse> updateNurse(@Valid @RequestBody NurseRequest nurseRequest, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new BaseApiResponse(BAD_REQUEST, FAILURE, "Token missing or malformed", Collections.emptyList()));
            }
            token = token.substring(7); // Extract token part after "Bearer "

            // Validation for fields before proceeding with the update
            if (nurseRequest.getNurseId() == null) {
                return ResponseEntity.badRequest()
                        .body(new BaseApiResponse(BAD_REQUEST, FAILURE, FIELD_REQUIRED_MESSAGE + " id", Collections.emptyList()));
            }
            if (nurseRequest.getAge() < 0) {
                return ResponseEntity.badRequest()
                        .body(new BaseApiResponse(BAD_REQUEST, FAILURE, COMMON_MESSAGE_FORMAT + " age", Collections.emptyList()));
            }
            if (nurseRequest.getGender() != null && !nurseRequest.getGender().matches("(?i)Male|Female|Other")) {
                return ResponseEntity.badRequest()
                        .body(new BaseApiResponse(BAD_REQUEST, FAILURE, FIELD_REQUIRED_MESSAGE + " gender", Collections.emptyList()));
            }
            if (nurseRequest.getAddress() == null || nurseRequest.getAddress().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new BaseApiResponse(BAD_REQUEST, FAILURE, FIELD_REQUIRED_MESSAGE + " address", Collections.emptyList()));
            }
            if (nurseRequest.getContact() == null || !nurseRequest.getContact().matches("^[0-9]{10,12}$")) {
                return ResponseEntity.badRequest()
                        .body(new BaseApiResponse(BAD_REQUEST, FAILURE, FIELD_REQUIRED_MESSAGE + " contact", Collections.emptyList()));
            }
            if (nurseRequest.getShift() == null || nurseRequest.getShift().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new BaseApiResponse(BAD_REQUEST, FAILURE, FIELD_REQUIRED_MESSAGE + " shift", Collections.emptyList()));
            }
            if (nurseRequest.getDoctorId() == null || nurseRequest.getDoctorId() == 0) {
                return ResponseEntity.badRequest()
                        .body(new BaseApiResponse(BAD_REQUEST, FAILURE, FIELD_REQUIRED_MESSAGE, Collections.emptyList()));
            }

            // If validation passes, proceed with the update by passing the token
            BaseApiResponse response = nurseImpl.updateNurse(nurseRequest.getNurseId(), nurseRequest, token);
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
