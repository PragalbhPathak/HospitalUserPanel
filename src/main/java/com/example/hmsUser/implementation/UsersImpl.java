package com.example.hmsUser.implementation;

import com.example.hmsUser.dto.requestDto.UserLoginRequest;
import com.example.hmsUser.dto.responseDto.BaseApiResponse;

public interface UsersImpl {
    BaseApiResponse loginUser(UserLoginRequest userLoginRequest);
}
