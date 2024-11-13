package com.example.hmsUser.dto.responseDto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmergencyResponse {
    private Long emergencyId;
    private String emergencyType;
    private String availability;
    private String relatedContact;

    private Long patientId;
    private String email;
}


