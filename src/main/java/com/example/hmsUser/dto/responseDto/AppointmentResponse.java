package com.example.hmsUser.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentResponse {
    private Long appointmentId;
    private Date dateOfAppointment;

    private Long doctorId;
    private Long patientId;
    private String doctorEmail;
    private String patientEmail;

}
