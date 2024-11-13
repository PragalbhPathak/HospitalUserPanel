package com.example.hmsUser.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NurseRequest {
    private Long nurseId;
    private int age;
    private String gender;
    private String address;
    private String shift;
    private String contact;

    private Long doctorId;
}
