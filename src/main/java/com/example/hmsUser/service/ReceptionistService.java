package com.example.hmsUser.service;

import com.example.hmsUser.dto.requestDto.ReceptionistRequest;
import com.example.hmsUser.dto.responseDto.BaseApiResponse;
import com.example.hmsUser.dto.responseDto.ReceptionistResponse;
import com.example.hmsUser.entity.Receptionist;
import com.example.hmsUser.implementation.ReceptionistImpl;
import com.example.hmsUser.repository.ReceptionistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.MESSAGE_NAMES.*;
import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.STATUS_CODES.*;
import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.SUCCESS_CODES.FAILURE;
import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.SUCCESS_CODES.SUCCESS;

@Service
public class ReceptionistService implements ReceptionistImpl {
    @Autowired
    private ReceptionistRepository receptionistRepository;
    @Autowired
    private JwtService jwtService;

    // Update Receptionist Information
    @Override
    public BaseApiResponse updateReceptionist(Long receptionistId, ReceptionistRequest receptionistRequest, String token) {

        try {
            String emailFromToken = jwtService.extractEmail(token);

            Optional<Receptionist> receptionistOpt = receptionistRepository.findById(receptionistId);
            if (receptionistOpt.isPresent()) {
                Receptionist receptionist = receptionistOpt.get();

                // Check if the logged-in nurse's email matches the nurse's email in the database
                if (!receptionist.getEmail().equals(emailFromToken)) {
                    return new BaseApiResponse(UNAUTHORIZED, FAILURE, "Unauthorized access", Collections.emptyList());
                }

                // Update fields with validation
                if (receptionistRequest.getAge() > 0) {
                    receptionist.setAge(receptionistRequest.getAge());
                }
                if (receptionistRequest.getGender() != null && !receptionistRequest.getGender().isEmpty()) {
                    receptionist.setGender(receptionistRequest.getGender());
                }
                if (receptionistRequest.getAddress() != null && !receptionistRequest.getAddress().isEmpty()) {
                    receptionist.setAddress(receptionistRequest.getAddress());
                }
                if (receptionistRequest.getContact() != null && !receptionistRequest.getContact().isEmpty()) {
                    receptionist.setContact(receptionistRequest.getContact());
                }
                if (receptionistRequest.getShift() != null && !receptionistRequest.getShift().isEmpty()) {
                    receptionist.setShift(receptionistRequest.getShift());
                }

                // Save the updated nurse
                receptionistRepository.save(receptionist);
                return new BaseApiResponse(SUCCESS_OK, SUCCESS, COMMON_MESSAGE_UPDATION, Collections.emptyList());
            } else {
                return new BaseApiResponse(NOT_FOUND, FAILURE, NOT_PRESENT, Collections.emptyList());
            }
        } catch (Exception ex) {
            return new BaseApiResponse(INTERNAL_SERVER_ERROR, FAILURE, COMMON_ERROR, Collections.emptyList());
        }
    }
    //---------------------------------------------------------------------------------------------------------------------

    //Fetch Receptionist by id
    @Override
    public BaseApiResponse getReceptionistById(Long receptionistId) {
        try {
            Optional<Receptionist> receptionistOpt = receptionistRepository.findById(receptionistId);
            if (receptionistOpt.isPresent()) {
                ReceptionistResponse response = new ReceptionistResponse(
                        receptionistOpt.get().getReceptionistId(),
                        receptionistOpt.get().getName(),
                        receptionistOpt.get().getAge(),
                        receptionistOpt.get().getGender(),
                        receptionistOpt.get().getAddress(),
                        receptionistOpt.get().getShift(),
                        receptionistOpt.get().getContact(),
                        receptionistOpt.get().getEmail()
                );
                return new BaseApiResponse(SUCCESS_OK, SUCCESS, COMMON_MESSAGE_DATA_FETCHED, response);
            } else {
                return new BaseApiResponse(NOT_FOUND, FAILURE, NOT_PRESENT, Collections.emptyList());
            }
        } catch (Exception ex) {
            return new BaseApiResponse(INTERNAL_SERVER_ERROR, FAILURE, COMMON_ERROR, Collections.emptyList());
        }
    }

}
