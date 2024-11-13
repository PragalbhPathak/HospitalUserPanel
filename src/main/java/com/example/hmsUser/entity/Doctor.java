package com.example.hmsUser.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@Entity
@Table(name = "Doctor")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctorId")
    private Long doctorId;
    @Column(name = "name")
    private String name;
    @Column(name = "experience")
    private int experience;
    @Column(name = "specialization")
    private String specialization;
    @Column(name = "address")
    private String address;
    @Column(name = "qualification")
    private String qualification;

    @Column(name = "shift")
    private String shift;

    @Pattern(regexp = "^[0-9]{10,12}$", message = "Phone number must be a valid phone number with 10 to 12 digits")
    @Column(name = "contact")
    private String contact;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email can't be empty")
    @Column(name = "email",nullable = false,unique = true)
    private String email;

    @NotBlank(message = "Password can't be null or empty")
    @Size(min = 6,message = "Password should have atLeast 6 characters")
    @Column(name = "password",nullable = false)
    private String password;

    @Column(name = "status")
    private String status;

    @Column(name = "userId")
    private Long userId;

}
