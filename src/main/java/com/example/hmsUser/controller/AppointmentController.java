package com.example.hmsUser.controller;

import com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT;
import com.example.hmsUser.dto.requestDto.AppointmentRequest;
import com.example.hmsUser.dto.requestDto.BillingRequest;
import com.example.hmsUser.dto.requestDto.SearchRequest;
import com.example.hmsUser.dto.responseDto.BaseApiResponse;
import com.example.hmsUser.implementation.AppointmentImpl;
import com.example.hmsUser.service.AppointmentService;
import com.example.hmsUser.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.MESSAGE_NAMES.*;
import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.STATUS_CODES.BAD_REQUEST;
import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.STATUS_CODES.INTERNAL_SERVER_ERROR;
import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.SUCCESS_CODES.FAILURE;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private AppointmentImpl appointmentImpl;

    @Autowired
    private JwtService jwtService;

    // Endpoint to create or update an appointment

    @PreAuthorize("hasRole('Receptionist')")
    @PostMapping(REST_MAPPING_CONSTRAINT.DEFINE_API.UPDATE_APPOINTMENT)
    public ResponseEntity<BaseApiResponse> createOrUpdateAppointment(@RequestBody AppointmentRequest appointmentRequest) {
        if (appointmentRequest == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new BaseApiResponse(BAD_REQUEST, FAILURE, COMMON_MESSAGE_INVALID, Collections.emptyList()));
        }
        if (appointmentRequest.getDoctorEmail() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new BaseApiResponse(BAD_REQUEST, FAILURE, COMMON_MESSAGE_FORMAT + "email must be valid", Collections.emptyList()));
        }
        if (appointmentRequest.getPatientEmail() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new BaseApiResponse(BAD_REQUEST, FAILURE, COMMON_MESSAGE_FORMAT + "email must be valid", Collections.emptyList()));
        }
        try {
            BaseApiResponse response = appointmentImpl.createOrUpdateAppointment(appointmentRequest);
//            return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            BaseApiResponse baseApiResponse = new BaseApiResponse(INTERNAL_SERVER_ERROR, FAILURE, COMMON_ERROR, Collections.emptyList());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseApiResponse);
        }
    }
    //-----------------------------------------------------------------------------------------------------------------------------------

    // Fetch record by any receptionist or logged in patient
    @PreAuthorize("hasAnyRole('Patient','Doctor','Receptionist')")
    @PostMapping(REST_MAPPING_CONSTRAINT.DEFINE_API.FETCH_APPOINTMENT)
    public ResponseEntity<BaseApiResponse> fetchAppointment(@RequestBody SearchRequest searchRequest, HttpServletRequest request) {
        if (searchRequest == null || searchRequest.getId() == null || searchRequest.getId() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new BaseApiResponse(BAD_REQUEST, FAILURE, COMMON_MESSAGE_INVALID, Collections.emptyList()));
        }

        try {
            // Extract the token from the request
            String token = getLoggedInToken(request);

            // Extract the role from the token
            String role = jwtService.extractRole(token);

            // Get the logged-in user's email from the token
            String loggedInUserEmail = jwtService.extractEmail(token);

            // Call the service with the logged-in user's email and role
            BaseApiResponse response = appointmentImpl.fetchAppointment(searchRequest.getId(), loggedInUserEmail, role);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseApiResponse(INTERNAL_SERVER_ERROR, FAILURE, COMMON_ERROR , Collections.emptyList()));
        }
    }

    // Utility method to extract the JWT token from the Authorization header
    private String getLoggedInToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7); // Remove "Bearer " prefix
        }
        return null;
    }

}
