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
    //private AppointmentStatus appointmentStatus;// MM/dd/yyyy HH:mm format

    private Long doctorId;
    private Long patientId;
    private String doctorEmail;
    private String patientEmail;

//    public AppointmentResponse(Long appointmentId, LocalDateTime dateOfAppointment, Long doctorId, Long patientId, AppointmentStatus appointmentStatus) {
//    }
}
