package com.example.hmsUser.dto.requestDto;

import com.example.hmsUser.entity.Receptionist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceptionistRequest {
    private Long receptionistId;
    private int age;
    private String gender;
    private String address;
    private String shift;
    private String contact;

}
