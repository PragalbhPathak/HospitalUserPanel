package com.example.hmsUser.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@Entity
@Table(name = "Appointment")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointmentId")
    private Long appointmentId;
    @Column(name = "dateOfAppointment")
    private Date dateOfAppointment;
    @Column(name = "doctorId")
    private Long doctorId;
    @Column(name = "patientId")
    private Long patientId;

//    @Enumerated(EnumType.STRING)
//    @Column(name = "appointmentStatus")
//    private AppointmentStatus appointmentStatus; // Add appointment status

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email can't be empty")
    @Column(name = "doctorEmail",nullable = false,unique = true)
    private String doctorEmail;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email can't be empty")
    @Column(name = "patientEmail",nullable = false,unique = true)
    private String patientEmail;
}
