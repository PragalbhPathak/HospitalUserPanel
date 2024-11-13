package com.example.hmsUser.dto.requestDto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UsersRequest {
    private Long userId;
    private String username;
    private String email;
    private String password;
    private String status;
    private String role;


}
