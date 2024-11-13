package com.example.hmsUser.implementation;

import com.example.hmsUser.dto.requestDto.BillingRequest;
import com.example.hmsUser.dto.responseDto.BaseApiResponse;

public interface BillingImpl {

    BaseApiResponse createOrUpdateBill(BillingRequest billingRequest);
    BaseApiResponse fetchBill(Long billingId, String loggedInUserEmail, String role);

}
