package com.example.hmsUser.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientRequest {
    private Long patientId;
    private int age;
    private String gender;
    private String address;
    private String medicalHistory;
    private String contact;

    private Long doctorId;
    private Long nurseId;
}
