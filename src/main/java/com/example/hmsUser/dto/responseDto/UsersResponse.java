package com.example.hmsUser.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UsersResponse {
    private Long userId;
    private String username;
    private String email;
//    private String password;
    private String status;
    private String role;
}
