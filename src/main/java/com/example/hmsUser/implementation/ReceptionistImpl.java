package com.example.hmsUser.implementation;

import com.example.hmsUser.dto.requestDto.NurseRequest;
import com.example.hmsUser.dto.requestDto.ReceptionistRequest;
import com.example.hmsUser.dto.responseDto.BaseApiResponse;

public interface ReceptionistImpl {
    BaseApiResponse updateReceptionist(Long receptionistId, ReceptionistRequest receptionistRequest,String token);
    BaseApiResponse getReceptionistById(Long receptionistId);
}
