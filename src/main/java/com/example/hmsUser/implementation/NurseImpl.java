package com.example.hmsUser.implementation;

import com.example.hmsUser.dto.requestDto.NurseRequest;
import com.example.hmsUser.dto.responseDto.BaseApiResponse;

public interface NurseImpl {
    BaseApiResponse updateNurse(Long nurseId, NurseRequest nurseRequest, String token);
    BaseApiResponse getNurseById(Long nurseId,String token);


}
