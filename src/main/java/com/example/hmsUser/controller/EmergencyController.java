package com.example.hmsUser.controller;

import com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT;
import com.example.hmsUser.dto.requestDto.EmergencyRequest;
import com.example.hmsUser.dto.responseDto.BaseApiResponse;
import com.example.hmsUser.implementation.EmergencyImpl;
import com.example.hmsUser.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.BASE_URL;
import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.MESSAGE_NAMES.COMMON_ERROR;
import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.MESSAGE_NAMES.COMMON_MESSAGE_FORMAT;
import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.STATUS_CODES.BAD_REQUEST;
import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.STATUS_CODES.INTERNAL_SERVER_ERROR;
import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.SUCCESS_CODES.FAILURE;

@RestController
@RequestMapping(BASE_URL)
public class EmergencyController {

    @Autowired
    private EmergencyImpl emergencyImpl;
    @Autowired
    private JwtService jwtService;

    // creating or updating emergency
    @PreAuthorize("hasRole('Receptionist')")
    @PostMapping(REST_MAPPING_CONSTRAINT.DEFINE_API.UPDATE_EMERGENCY)
    public ResponseEntity<BaseApiResponse> createOrUpdateEmergency(@RequestBody EmergencyRequest emergencyRequest) {
        try {
        // Input validation
        if (emergencyRequest == null) {
            return ResponseEntity.badRequest().body(new BaseApiResponse(BAD_REQUEST, FAILURE, "Request body is missing",  Collections.emptyList()));
        }
        if (emergencyRequest.getEmergencyType() == null || emergencyRequest.getEmergencyType().isEmpty()) {
            return ResponseEntity.badRequest().body(new BaseApiResponse(BAD_REQUEST, FAILURE, "Emergency type is required",  Collections.emptyList()));
        }
        if (emergencyRequest.getAvailability() == null || emergencyRequest.getAvailability().isEmpty()) {
            return ResponseEntity.badRequest().body(new BaseApiResponse(BAD_REQUEST, FAILURE, "Availability is required",  Collections.emptyList()));
        }
        if (emergencyRequest.getRelatedContact() == null || !emergencyRequest.getRelatedContact().matches("^[0-9]{10,12}$")) {
            return ResponseEntity.badRequest().body(new BaseApiResponse(BAD_REQUEST, FAILURE, "Related contact is required",  Collections.emptyList()));
        }
            if (emergencyRequest.getEmail() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new BaseApiResponse(BAD_REQUEST, FAILURE, COMMON_MESSAGE_FORMAT + "email must be valid", Collections.emptyList()));
            }

        // Try to create or update the emergency record

            BaseApiResponse response = emergencyImpl.createOrUpdateEmergency(emergencyRequest);

            // Return the response from the service
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Handle errors during the service call
            return ResponseEntity.status(500).body(new BaseApiResponse(INTERNAL_SERVER_ERROR, FAILURE, COMMON_ERROR,  Collections.emptyList()));
        }
    }
//--------------------------------------------------------------------------------------------------------------------------------------

    // Get Emergency Data
    @PreAuthorize("hasAnyRole('Receptionist', 'Patient')")
    @PostMapping(REST_MAPPING_CONSTRAINT.DEFINE_API.FETCH_EMERGENCY)
    public ResponseEntity<BaseApiResponse> fetchEmergency(@RequestBody EmergencyRequest emergencyRequest, HttpServletRequest request) {
        // Validate input
        if (emergencyRequest == null || emergencyRequest.getEmergencyId() == null) {
            return ResponseEntity.badRequest().body(new BaseApiResponse(BAD_REQUEST, FAILURE, "Emergency ID is required", Collections.emptyList()));
        }

        try{

        // Extract token part after "Bearer " from Authorization header
        String token = request.getHeader("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new BaseApiResponse(BAD_REQUEST, FAILURE, COMMON_ERROR, Collections.emptyList()));
            }
        token = token.substring(7); // Strip "Bearer "

        // Extract the role from the token
        String role = jwtService.extractRole(token);

        // Get the logged-in user's email from the token
        String loggedInUserEmail = jwtService.extractEmail(token);

            // Call the service method to get the emergency record, passing the token directly
            BaseApiResponse response = emergencyImpl.getEmergencyById(emergencyRequest.getEmergencyId(),loggedInUserEmail,role);
            System.out.println("Response: " + response);

            // Return the response from the service
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Handle errors during the service call
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseApiResponse(INTERNAL_SERVER_ERROR, FAILURE, COMMON_ERROR, Collections.emptyList()));
        }
    }

}








