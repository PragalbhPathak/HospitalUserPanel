package com.example.hmsUser.service;

import com.example.hmsUser.dto.requestDto.EmergencyRequest;
import com.example.hmsUser.dto.responseDto.BaseApiResponse;
import com.example.hmsUser.dto.responseDto.BillingResponse;
import com.example.hmsUser.dto.responseDto.EmergencyResponse;
import com.example.hmsUser.entity.Billing;
import com.example.hmsUser.entity.Emergency;
import com.example.hmsUser.implementation.EmergencyImpl;
import com.example.hmsUser.repository.EmergencyRepository;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.MESSAGE_NAMES.*;
import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.STATUS_CODES.*;
import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.SUCCESS_CODES.FAILURE;
import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.SUCCESS_CODES.SUCCESS;

//import static com.example.hmsUser.util.ResponseConstants.*;

@Service
public class EmergencyService implements EmergencyImpl {


    @Autowired
    private EmergencyRepository emergencyRepository;

    @Autowired
    private JwtService jwtService;

    @Transactional
    // Create or update emergency
    @Override
    public BaseApiResponse createOrUpdateEmergency(EmergencyRequest emergencyRequest) {
        try {
            // Check if we have an existing emergency record based on the emergencyId
            Emergency emergency = emergencyRequest.getEmergencyId() != null ?
                    emergencyRepository.findById(emergencyRequest.getEmergencyId()).orElse(new Emergency()) : new Emergency();

            // Set the properties from the request
            emergency.setEmergencyType(emergencyRequest.getEmergencyType());
            emergency.setAvailability(emergencyRequest.getAvailability());
            emergency.setRelatedContact(emergencyRequest.getRelatedContact());
            emergency.setPatientId(emergencyRequest.getPatientId());
            emergency.setEmail(emergencyRequest.getEmail());

            // Save the emergency record (this will either create a new one or update an existing one)
            Emergency savedEmergency = emergencyRepository.save(emergency);

            // Create the response object
            EmergencyResponse emergencyResponse = new EmergencyResponse(
                    savedEmergency.getEmergencyId(),
                    savedEmergency.getEmergencyType(),
                    savedEmergency.getAvailability(),
                    savedEmergency.getRelatedContact(),
                    savedEmergency.getPatientId(),
                    savedEmergency.getEmail()
            );

            return new BaseApiResponse(SUCCESS_OK, SUCCESS,
                    emergencyRequest.getEmergencyId() == null ?
                            "Emergency record created successfully" : "Emergency record updated successfully", emergencyResponse);
        } catch (Exception e) {
            return new BaseApiResponse(INTERNAL_SERVER_ERROR, FAILURE, "An unexpected error occurred", Collections.emptyList());
        }
    }


//    public BaseApiResponse getEmergencyById(Long emergencyId, String token) {
//        try {
//            // Extract patient ID and role from the token
//            Long loggedInPatientId = jwtService.extractId(token);
//            String loggedInRole = jwtService.extractRole(token);
//            System.out.println("Logged in role: " + loggedInRole);  // Debugging line
//            System.out.println("Logged in patient ID: " + loggedInPatientId);  // Debugging line
//
//            // Fetch the emergency record
//            Emergency emergency = emergencyRepository.findById(emergencyId).orElse(null);
//
//            if (emergency == null) {
//                return new BaseApiResponse(NOT_FOUND, FAILURE, "Emergency record not found", Collections.emptyList());
//            }
//
//            // Only create the response object if the user is authorized to see the record
//            EmergencyResponse emergencyResponse = new EmergencyResponse(
//                    emergency.getEmergencyId(),
//                    emergency.getEmergencyType(),
//                    emergency.getAvailability(),
//                    emergency.getRelatedContact(),
//                    emergency.getPatientId(),
//                    emergency.getEmail()
//            );
//
//            // Check if the user is a receptionist or a patient
//            if ("Receptionist".equalsIgnoreCase(loggedInRole)) {
//                // Receptionist can access all emergency records
//                return new BaseApiResponse(SUCCESS_OK, SUCCESS, "Emergency record fetched successfully", emergencyResponse);
//            } else if ("Patient".equalsIgnoreCase(loggedInRole)) {
//                // Patient can only access their own emergency record
//                if (emergency.getPatientId().equals(loggedInPatientId)) {
//                    return new BaseApiResponse(SUCCESS_OK, SUCCESS, "Emergency record fetched successfully", emergencyResponse);
//                } else {
//                    // Unauthorized if the patient tries to access another patient's emergency record
//                    return new BaseApiResponse(UNAUTHORIZED, FAILURE, "You are not authorized to access this record", Collections.emptyList());
//                }
//            } else {
//                // If the role is not recognized (error case)
//                return new BaseApiResponse(UNAUTHORIZED, FAILURE, "Unauthorized role", Collections.emptyList());
//            }
//        } catch (JwtException ex) {
//            // Specific exception handling for invalid token
//            return new BaseApiResponse(UNAUTHORIZED, FAILURE, "Invalid token", Collections.emptyList());
//        } catch (Exception e) {
//            e.printStackTrace();
//            // Catch unexpected errors
//            return new BaseApiResponse(INTERNAL_SERVER_ERROR, FAILURE, "An unexpected error occurred", Collections.emptyList());
//        }
//    }

    // Fetch emergency by ID
    @Override
    public BaseApiResponse getEmergencyById(Long emergencyId, String loggedInUserEmail, String role) {
        try {
            Optional<Emergency> emergencyOpt = emergencyRepository.findById(emergencyId);

            if (emergencyOpt.isPresent()) {
                Emergency emergency = emergencyOpt.get();

                // If the user is a Receptionist, allow access to any billing record
                if ("Receptionist".equalsIgnoreCase(role)) {
                    EmergencyResponse emergencyResponse = new EmergencyResponse(
                            emergency.getEmergencyId(),
                            emergency.getEmergencyType(),
                            emergency.getAvailability(),
                            emergency.getRelatedContact(),
                            emergency.getPatientId(),
                            emergency.getEmail()
                    );
                    return new BaseApiResponse(SUCCESS_OK, SUCCESS, COMMON_MESSAGE_DATA_FETCHED, emergencyResponse);
                }

                // If the user is a Patient, they can only access their own billing record
                if (!emergency.getEmail().equals(loggedInUserEmail)) {
                    return new BaseApiResponse(UNAUTHORIZED, FAILURE, "You can only fetch your own billing data.", Collections.emptyList());
                }

                // If the user is authorized (either Receptionist or Patient accessing their own record), return the data
                EmergencyResponse emergencyResponse = new EmergencyResponse(
                        emergency.getEmergencyId(),
                        emergency.getEmergencyType(),
                        emergency.getAvailability(),
                        emergency.getRelatedContact(),
                        emergency.getPatientId(),
                        emergency.getEmail()
                );
                return new BaseApiResponse(SUCCESS_OK, SUCCESS, COMMON_MESSAGE_DATA_FETCHED, emergencyResponse);
            } else {
                return new BaseApiResponse(NOT_FOUND, FAILURE, NOT_PRESENT, Collections.emptyList());
            }
        } catch (Exception e) {
            return new BaseApiResponse(INTERNAL_SERVER_ERROR, FAILURE, COMMON_ERROR , Collections.emptyList());
        }
    }



//
//    // Fetch emergency by ID
//    public BaseApiResponse getEmergencyById(Long emergencyId) {
//        try {
//            // Retrieve the emergency record by ID
//            Emergency emergency = emergencyRepository.findById(emergencyId).orElse(null);
//
//            if (emergency == null) {
//                // If the emergency doesn't exist, return an error response
//                return new BaseApiResponse(NOT_FOUND, FAILURE, "Emergency record not found", Collections.emptyList());
//            }
//
//            // Create the response object
//            EmergencyResponse emergencyResponse = new EmergencyResponse(
//                    emergency.getEmergencyId(),
//                    emergency.getEmergencyType(),
//                    emergency.getAvailability(),
//                    emergency.getRelatedContact(),
//                    emergency.getPatientId()
//            );
//
//            return new BaseApiResponse(SUCCESS_OK, SUCCESS, "Emergency record fetched successfully", emergencyResponse);
//        } catch (Exception e) {
//            return new BaseApiResponse(INTERNAL_SERVER_ERROR, FAILURE, "An unexpected error occurred", Collections.emptyList());
//        }
//    }

}
