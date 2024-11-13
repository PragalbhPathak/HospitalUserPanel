package com.example.hmsUser.service;

import com.example.hmsUser.dto.requestDto.BillingRequest;
import com.example.hmsUser.dto.responseDto.BaseApiResponse;
import com.example.hmsUser.dto.responseDto.BillingResponse;
import com.example.hmsUser.entity.Billing;
import com.example.hmsUser.implementation.BillingImpl;
import com.example.hmsUser.repository.BillingRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.MESSAGE_NAMES.*;
import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.STATUS_CODES.*;
import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.SUCCESS_CODES.FAILURE;
import static com.example.hmsUser.baseConstraints.REST_MAPPING_CONSTRAINT.SUCCESS_CODES.SUCCESS;

@Service
public class BillingService implements BillingImpl {

    @Autowired
    private BillingRepository billingRepository;

    @Autowired
    private JwtService jwtService;

    @Transactional
    @Override
    public BaseApiResponse createOrUpdateBill(BillingRequest billingRequest) {
        // Validations
        if (billingRequest.getBillAmount() == null || billingRequest.getBillAmount() <= 0) {
            return new BaseApiResponse(BAD_REQUEST, FAILURE, COMMON_MESSAGE_FORMAT, Collections.emptyList());
        }
        if (billingRequest.getPaymentDate() == null) {
            return new BaseApiResponse(BAD_REQUEST, FAILURE, FIELD_REQUIRED_MESSAGE, Collections.emptyList());
        }
        if (billingRequest.getPatientId() == null) {
            return new BaseApiResponse(BAD_REQUEST, FAILURE, FIELD_REQUIRED_MESSAGE, Collections.emptyList());
        }
        if (billingRequest.getEmail() == null) {
            return new BaseApiResponse(BAD_REQUEST, FAILURE, FIELD_REQUIRED_MESSAGE, Collections.emptyList());
        }

        try {
            // Check for existing billing record
            Billing billing = billingRepository.findById(billingRequest.getBillingId()).orElse(new Billing());
            // Update fields
            billing.setPaymentStatus(billingRequest.isPaymentStatus());
            billing.setBillAmount(billingRequest.getBillAmount());
            billing.setPaymentDate(billingRequest.getPaymentDate());
            billing.setInsuranceCoverage(billingRequest.getInsuranceCoverage());
            billing.setPatientId(billingRequest.getPatientId());
            billing.setEmail(billingRequest.getEmail());

            Billing savedBilling = billingRepository.save(billing);
            return new BaseApiResponse(SUCCESS_OK, SUCCESS,
                    billingRequest.getBillingId() == null ?
                            "Billing record created successfully" : "Billing record updated successfully",
                    new BillingResponse(
                            savedBilling.getBillingId(),
                            savedBilling.isPaymentStatus(),
                            savedBilling.getBillAmount(),
                            savedBilling.getPaymentDate(),
                            savedBilling.getInsuranceCoverage(),
                            savedBilling.getPatientId(),
                            savedBilling.getEmail()
                    ));
        } catch (Exception e) {
            return new BaseApiResponse(INTERNAL_SERVER_ERROR, FAILURE, COMMON_ERROR, Collections.emptyList());
        }
    }

//--------------------------------------------------------------------------------------------------------------------------------
    // fetch Bill
    @Override
    public BaseApiResponse fetchBill(Long billingId, String loggedInUserEmail, String role) {
        try {
            Optional<Billing> billingOpt = billingRepository.findById(billingId);

            if (billingOpt.isPresent()) {
                Billing billing = billingOpt.get();

                // If the user is a Receptionist, allow access to any billing record
                if ("Receptionist".equalsIgnoreCase(role)) {
                    BillingResponse billingResponse = new BillingResponse(
                            billing.getBillingId(),
                            billing.isPaymentStatus(),
                            billing.getBillAmount(),
                            billing.getPaymentDate(),
                            billing.getInsuranceCoverage(),
                            billing.getPatientId(),
                            billing.getEmail()
                    );
                    return new BaseApiResponse(SUCCESS_OK, SUCCESS, COMMON_MESSAGE_DATA_FETCHED, billingResponse);
                }

                // If the user is a Patient, they can only access their own billing record
                if (!billing.getEmail().equals(loggedInUserEmail)) {
                    return new BaseApiResponse(UNAUTHORIZED, FAILURE, "You can only fetch your own billing data.", Collections.emptyList());
                }

                // If the user is authorized (either Receptionist or Patient accessing their own record), return the data
                BillingResponse billingResponse = new BillingResponse(
                        billing.getBillingId(),
                        billing.isPaymentStatus(),
                        billing.getBillAmount(),
                        billing.getPaymentDate(),
                        billing.getInsuranceCoverage(),
                        billing.getPatientId(),
                        billing.getEmail()
                );
                return new BaseApiResponse(SUCCESS_OK, SUCCESS, COMMON_MESSAGE_DATA_FETCHED, billingResponse);
            } else {
                return new BaseApiResponse(NOT_FOUND, FAILURE, NOT_PRESENT, Collections.emptyList());
            }
        } catch (Exception e) {
            return new BaseApiResponse(INTERNAL_SERVER_ERROR, FAILURE, COMMON_ERROR + " catch", Collections.emptyList());
        }
    }

}
