package com.example.hmsUser.controller;

import com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT;
import com.example.hmsUser.dto.requestDto.UserLoginRequest;
import com.example.hmsUser.dto.responseDto.BaseApiResponse;

import com.example.hmsUser.implementation.UsersImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.BASE_URL;
import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.STATUS_CODES.BAD_REQUEST;
import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.STATUS_CODES.INTERNAL_SERVER_ERROR;
import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.SUCCESS_CODES.FAILURE;

@RestController
@RequestMapping(BASE_URL)
public class UsersController {

    @Autowired
    public UsersImpl userImpl;

    @PostMapping(REST_MAPPING_CONSTRAINT.DEFINE_API.LOGIN_USER)
    public ResponseEntity<BaseApiResponse> loginUser(@RequestBody UserLoginRequest userLoginRequest) {
        try {
            if (userLoginRequest.getEmail() == null || userLoginRequest.getPassword() == null) {
                // Return bad request if email or password is missing
                BaseApiResponse errorResponse = new BaseApiResponse(BAD_REQUEST,FAILURE, "Email and password are required", Collections.emptyList());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
            BaseApiResponse response = userImpl.loginUser(userLoginRequest);

            // Check if the login was successful
            if (response.getSuccess() == 1) {
                response.setStatus(String.valueOf(HttpStatus.OK.value()));  // Set HTTP status code as string
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                response.setStatus(String.valueOf(HttpStatus.UNAUTHORIZED.value()));  // Unauthorized status
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

        } catch (Exception e) {
            BaseApiResponse errorResponse = new BaseApiResponse(INTERNAL_SERVER_ERROR, FAILURE, "An error occurred", Collections.emptyList());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

}
