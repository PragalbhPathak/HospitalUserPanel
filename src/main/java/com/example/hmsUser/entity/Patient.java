package com.example.hmsUser.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Data
@Entity
@Table(name = "Patient")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patientId")
    private Long patientId;

    @NotBlank(message = "name can't be blank")
    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "age")
    private int age;

    @Column(name = "gender")
    private String gender;

    @Column(name = "status")
    private String status;

    @Column(name = "address")
    private String address;

    @Column(name = "medicalHistory")
    private String medicalHistory;

    @Pattern(regexp = "^[0-9]{10,12}$", message = "Phone number must be a valid phone number with 10 to 12 digits")
    @Column(name = "contact")
    private String contact;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email can't be empty")
    @Column(name = "email",unique = true)
    private String email;

    @NotBlank(message = "Password can't be null or empty")
    @Size(min = 6,message = "Password should have atLeast 6 characters")
    @Column(name = "password")
    private String password;

    @Column(name = "userId")
    private Long userId;
    @Column(name = "doctorId")
    private Long doctorId;
    @Column(name = "nurseId")
    private Long nurseId;
}
