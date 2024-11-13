    package com.example.hmsUser.implementation;

    import com.example.hmsUser.dto.requestDto.EmergencyRequest;
    import com.example.hmsUser.dto.responseDto.BaseApiResponse;

    public interface EmergencyImpl {
        BaseApiResponse createOrUpdateEmergency(EmergencyRequest emergencyRequest);
        BaseApiResponse getEmergencyById(Long billingId, String loggedInUserEmail, String role);
    }
