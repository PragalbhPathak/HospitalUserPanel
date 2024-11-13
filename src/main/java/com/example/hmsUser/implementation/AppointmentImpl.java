package com.example.hmsUser.implementation;

import com.example.hmsUser.dto.requestDto.AppointmentRequest;
import com.example.hmsUser.dto.responseDto.AppointmentResponse;
import com.example.hmsUser.dto.responseDto.BaseApiResponse;

public interface AppointmentImpl {
    BaseApiResponse createOrUpdateAppointment(AppointmentRequest request);
    BaseApiResponse fetchAppointment(Long appointmentId, String loggedInUserEmail, String role);
}
