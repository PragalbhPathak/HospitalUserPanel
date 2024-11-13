package com.example.hmsUser.implementation;

import com.example.hmsUser.dto.requestDto.DoctorRequest;
import com.example.hmsUser.dto.responseDto.BaseApiResponse;

public interface DoctorImpl {
    BaseApiResponse updateDoctor(Long doctorId, DoctorRequest doctorRequest,String token);
    BaseApiResponse getDoctorById(Long doctorId,String token);
}
