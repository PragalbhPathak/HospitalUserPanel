package com.example.hmsUser.controller;

import com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT;
import com.example.hmsUser.dto.requestDto.BillingRequest;
import com.example.hmsUser.dto.requestDto.SearchRequest;
import com.example.hmsUser.dto.responseDto.BaseApiResponse;
import com.example.hmsUser.implementation.BillingImpl;
import com.example.hmsUser.service.BillingService;
import com.example.hmsUser.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
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
public class BillingController {

    @Autowired
    private BillingImpl billingImpl;
    @Autowired
    private JwtService jwtService;

     //Creates or updates a billing record.
    @PreAuthorize("hasRole('Receptionist')")
    @PostMapping(REST_MAPPING_CONSTRAINT.DEFINE_API.UPDATE_BILLING)
    public ResponseEntity<BaseApiResponse> createOrUpdateBill(@RequestBody BillingRequest billingRequest) {
        if (billingRequest == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new BaseApiResponse(BAD_REQUEST, FAILURE, COMMON_MESSAGE_INVALID, Collections.emptyList()));
        }
        if (billingRequest.getEmail() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new BaseApiResponse(BAD_REQUEST, FAILURE, COMMON_MESSAGE_FORMAT + "email must be valid", Collections.emptyList()));
        }
        try {
            BaseApiResponse response = billingImpl.createOrUpdateBill(billingRequest);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            BaseApiResponse baseApiResponse = new BaseApiResponse(INTERNAL_SERVER_ERROR, FAILURE, COMMON_ERROR, Collections.emptyList());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseApiResponse);
        }
    }
    //----------------------------------------------------------------------------------------------------------------------------------------

    // Fetch record by any receptionist or logged in patient
    @PreAuthorize("hasAnyRole('Patient','Receptionist')")
    @PostMapping(REST_MAPPING_CONSTRAINT.DEFINE_API.FETCH_BILLING)
    public ResponseEntity<BaseApiResponse> fetchBill(@RequestBody SearchRequest searchRequest, HttpServletRequest request) {
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
            BaseApiResponse response = billingImpl.fetchBill(searchRequest.getId(), loggedInUserEmail, role);

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
