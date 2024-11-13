package com.example.hmsUser.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorRequest {
    private Long doctorId;
   // private String name;
    private int experience;
    private String specialization;
    private String address;
    private String qualification;
    private String shift;
    private String contact;
    //private String email;
   // private String password;

    //private Long userId;
}
