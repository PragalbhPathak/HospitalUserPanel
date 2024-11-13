package com.example.hmsUser.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentRequest {
    private Long appointmentId;
    private Date dateOfAppointment;        // MM/dd/yyyy HH:mm format

    private Long doctorId;
    private Long patientId;
    private String doctorEmail;
    private String patientEmail;
}
