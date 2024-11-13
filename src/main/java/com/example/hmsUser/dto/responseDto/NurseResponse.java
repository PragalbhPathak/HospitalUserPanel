package com.example.hmsUser.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NurseResponse {
    private Long nurseId;
    private String name;
    private int age;
    private String gender;
    private String address;
    private String shift;
    private String contact;
    private String email;

    private Long doctorId;
}
