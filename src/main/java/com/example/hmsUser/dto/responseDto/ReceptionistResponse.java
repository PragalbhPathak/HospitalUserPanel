package com.example.hmsUser.dto.responseDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceptionistResponse {
    private Long nurseId;
    private String name;
    private int age;
    private String gender;
    private String address;
    private String shift;
    private String contact;
    private String email;

//    private String password;

}
