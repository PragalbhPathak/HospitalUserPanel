package com.example.hmsUser.implementation;

import com.example.hmsUser.dto.requestDto.PatientRequest;
import com.example.hmsUser.dto.responseDto.BaseApiResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface PatientImpl {
    BaseApiResponse updatePatient(Long patientId, PatientRequest patientRequest);
    BaseApiResponse getPatientById(Long patientId,String token);

}
