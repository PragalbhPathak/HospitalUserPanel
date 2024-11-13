package com.example.hmsUser.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NurseRequest {
    private Long nurseId;
    //private String name;
    private int age;
    private String gender;
    private String address;
    private String shift;
    private String contact;
    //private String email;
    //private String password;

    //private Long userId;
    private Long doctorId;
}
